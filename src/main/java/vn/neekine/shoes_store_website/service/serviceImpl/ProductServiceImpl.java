package vn.neekine.shoes_store_website.service.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.Anh;
import vn.neekine.shoes_store_website.model.SanPham;
import vn.neekine.shoes_store_website.repository.ProductRepository;
import vn.neekine.shoes_store_website.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDetailsDTO> getAllProducts() {
        List<SanPham> products = productRepository.findAllProducts();
        Map<String, SanPham> productMap = new HashMap<>();

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        products.forEach(product -> {
            productMap.putIfAbsent(product.getTenSanPham(), product);
        });

        // Tạo danh sách ProductDetailsDTO từ các sản phẩm trong Map
        return productMap.values().stream()
                    .map(product -> {
                        Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                        List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                        return new ProductDetailsDTO(
                            product.getId(), 
                            product.getLoai(), 
                            product.getTenSanPham(), 
                            product.getSize(), 
                            product.getSoluong(), 
                            product.getGiaBan(), 
                            phanTram, 
                            photoNames);
                    })
                    .collect(Collectors.toList());
    }

    @Override
    public List<ProductDetailsDTO> searchProduct(String name) {
        List<SanPham> products = productRepository.searchProductByName(name);

        return products.stream()
                        .map(product -> {
                            Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                            List<String> photoNames = product.getPhotos().stream()
                                                                .map(Anh::getTen)
                                                                .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                                .collect(Collectors.toList());
                            return new ProductDetailsDTO(
                                    product.getId(), 
                                    product.getLoai(), 
                                    product.getTenSanPham(), 
                                    product.getSize(), 
                                    product.getSoluong(), 
                                    product.getGiaBan(), 
                                    phanTram, 
                                    photoNames);
                        })
                        .collect(Collectors.toList());
    }

    @Override
    public List<SanPham> filterProducts(List<String> filters) {
        List<String> brands = new ArrayList<>();
        List<String> sizes = new ArrayList<>();
        List<Long> minPrices = new ArrayList<>();
        List<Long> maxPrices = new ArrayList<>();

        Map<String, Long[]> priceRanges = new HashMap<>();
        priceRanges.put("under_500", new Long[]{0L, 500000L});
        priceRanges.put("500_to_1000", new Long[]{500000L, 1000000L});
        priceRanges.put("1000_to_2000", new Long[]{1000000L, 2000000L});
        priceRanges.put("2000_to_5000", new Long[]{2000000L, 5000000L});
        priceRanges.put("above_5000", new Long[]{5000000L, Long.MAX_VALUE});


        List<String> validSizes = Arrays.asList("36", "37", "38", "39", "40", "41", "42", "1", "2", "3", "4", "5", "6");

        for (String filter : filters) {
            if (priceRanges.containsKey(filter)) {
                Long[] range = priceRanges.get(filter);
                minPrices.add(range[0]);
                maxPrices.add(range[1]);
            } else if (validSizes.contains(filter)) {
                sizes.add(filter);
            } else {
                brands.add(filter);
            }
        }


        return this.productRepository.findFilteredProducts(brands, validSizes, minPrices, maxPrices);
    }

    @Override
    public Page<ProductDetailsDTO> getAllPages(int page, int size, List<String> filters) {
        List<SanPham> products = new ArrayList<>();

        if(!filters.isEmpty()){
            products = this.filterProducts(filters);
        }
        else {
            products = this.productRepository.findAllProducts();
        }

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            productMap.putIfAbsent(product.getTenSanPham(), product);
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());

        // Phân trang danh sách các sản phẩm không trùng tên
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), uniqueProducts.size());
        List<ProductDetailsDTO> productDTOs = uniqueProducts.subList(start, end).stream()
                .map(product -> {
                    Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                    List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                    return new ProductDetailsDTO(
                            product.getId(),
                            product.getLoai(),
                            product.getTenSanPham(),
                            product.getSize(),
                            product.getSoluong(),
                            product.getGiaBan(),
                            phanTram,
                            photoNames);
                })
                .collect(Collectors.toList());

        // Trả về một trang chứa các ProductDetailsDTO
        return new PageImpl<>(productDTOs, PageRequest.of(page, size), uniqueProducts.size());
    }

    private int comparePhotoNames(String name1, String name2) {
        Double number1 = extractNumberAfterDot(name1);
        Double number2 = extractNumberAfterDot(name2);

        return Double.compare(number1, number2);
    }

    private Double extractNumberAfterDot(String name) {
        try {
            String[] parts = name.split("\\.");
            if (parts.length > 1) {
                return Double.parseDouble(parts[1]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public Page<ProductDetailsDTO> getAllShoesPages(int page, int size) {
        List<SanPham> products = this.productRepository.findAllProducts();

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            if ("giày".equals(product.getLoai())) {
                productMap.putIfAbsent(product.getTenSanPham(), product);
            }
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());

        // Phân trang danh sách các sản phẩm không trùng tên
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), uniqueProducts.size());
        List<ProductDetailsDTO> productDTOs = uniqueProducts.subList(start, end).stream()
                .map(product -> {
                    Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                    List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                    return new ProductDetailsDTO(
                            product.getId(),
                            product.getLoai(),
                            product.getTenSanPham(),
                            product.getSize(),
                            product.getSoluong(),
                            product.getGiaBan(),
                            phanTram,
                            photoNames);
                })
                .collect(Collectors.toList());

        // Trả về một trang chứa các ProductDetailsDTO
        return new PageImpl<>(productDTOs, PageRequest.of(page, size), uniqueProducts.size());
    }

    @Override
    public Page<ProductDetailsDTO> getAllBagPages(int page, int size) {
        List<SanPham> products = this.productRepository.findAllProducts();

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            if ("bag".equals(product.getLoai())) {
                productMap.putIfAbsent(product.getTenSanPham(), product);
            }
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());

        // Phân trang danh sách các sản phẩm không trùng tên
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), uniqueProducts.size());
        List<ProductDetailsDTO> productDTOs = uniqueProducts.subList(start, end).stream()
                .map(product -> {
                    Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                    List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                    return new ProductDetailsDTO(
                            product.getId(),
                            product.getLoai(),
                            product.getTenSanPham(),
                            product.getSize(),
                            product.getSoluong(),
                            product.getGiaBan(),
                            phanTram,
                            photoNames);
                })
                .collect(Collectors.toList());

        // Trả về một trang chứa các ProductDetailsDTO
        return new PageImpl<>(productDTOs, PageRequest.of(page, size), uniqueProducts.size());
    }

    @Override
    public Page<ProductDetailsDTO> getAllClothesPages(int page, int size) {
        List<SanPham> products = this.productRepository.findAllProducts();

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            if ("clothes".equals(product.getLoai())) {
                productMap.putIfAbsent(product.getTenSanPham(), product);
            }
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());

        // Phân trang danh sách các sản phẩm không trùng tên
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), uniqueProducts.size());
        List<ProductDetailsDTO> productDTOs = uniqueProducts.subList(start, end).stream()
                .map(product -> {
                    Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                    List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                    return new ProductDetailsDTO(
                            product.getId(),
                            product.getLoai(),
                            product.getTenSanPham(),
                            product.getSize(),
                            product.getSoluong(),
                            product.getGiaBan(),
                            phanTram,
                            photoNames);
                })
                .collect(Collectors.toList());

        // Trả về một trang chứa các ProductDetailsDTO
        return new PageImpl<>(productDTOs, PageRequest.of(page, size), uniqueProducts.size());
    }

    @Override
    public Page<ProductDetailsDTO> getAllSandalPages(int page, int size) {
        List<SanPham> products = this.productRepository.findAllProducts();

        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            if ("sandal".equals(product.getLoai())) {
                productMap.putIfAbsent(product.getTenSanPham(), product);
            }
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());

        // Phân trang danh sách các sản phẩm không trùng tên
        int start = (int) PageRequest.of(page, size).getOffset();
        int end = Math.min((start + size), uniqueProducts.size());
        List<ProductDetailsDTO> productDTOs = uniqueProducts.subList(start, end).stream()
                .map(product -> {
                    Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                    List<String> photoNames = product.getPhotos().stream()
                                                        .map(Anh::getTen)
                                                        .sorted(this::comparePhotoNames) // Sắp xếp theo tên với comparator tùy chỉnh
                                                        .collect(Collectors.toList());
                    return new ProductDetailsDTO(
                            product.getId(),
                            product.getLoai(),
                            product.getTenSanPham(),
                            product.getSize(),
                            product.getSoluong(),
                            product.getGiaBan(),
                            phanTram,
                            photoNames);
                })
                .collect(Collectors.toList());

        // Trả về một trang chứa các ProductDetailsDTO
        return new PageImpl<>(productDTOs, PageRequest.of(page, size), uniqueProducts.size());
    }
}
