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
        // Danh sách lưu trữ các điều kiện lọc
        List<String> brands = new ArrayList<>();
        List<String> sizes = new ArrayList<>();
        List<PriceRange> priceRanges = new ArrayList<>();
        
        // Các giá trị giá
        Map<String, Long[]> priceRangeMap = new HashMap<>();
        priceRangeMap.put("under_500", new Long[]{0L, 500000L});
        priceRangeMap.put("500_to_1000", new Long[]{500000L, 1000000L});
        priceRangeMap.put("1000_to_2000", new Long[]{1000000L, 2000000L});
        priceRangeMap.put("2000_to_5000", new Long[]{2000000L, 5000000L});
        priceRangeMap.put("above_5000", new Long[]{5000000L, Long.MAX_VALUE});
        
        // Các kích thước hợp lệ
        List<String> validSizes = Arrays.asList("36", "37", "38", "39", "40", "41", "42", "1", "2", "3", "4", "5", "6");
        
        // Phân loại các điều kiện lọc từ `filters`
        for (String filter : filters) {
            if (priceRangeMap.containsKey(filter)) {
                Long[] range = priceRangeMap.get(filter);
                priceRanges.add(new PriceRange(range[0], range[1]));
            } else if (validSizes.contains(filter)) {
                sizes.add(filter);
            } else {
                brands.add(filter);
            }
        }
        
        // Lấy tất cả sản phẩm từ cơ sở dữ liệu
        List<SanPham> products = this.productRepository.findAllProducts();
        // Lưu sản phẩm đầu tiên gặp phải vào Map theo tên
        Map<String, SanPham> productMap = new HashMap<>();
        products.forEach(product -> {
            productMap.putIfAbsent(product.getTenSanPham(), product);
        });

        // Chuyển các giá trị từ Map thành List
        List<SanPham> uniqueProducts = productMap.values().stream().collect(Collectors.toList());
        
        // Lọc sản phẩm theo nhãn hiệu
        if (!brands.isEmpty()) {
            for(String brand : brands){
                uniqueProducts = uniqueProducts.stream()
                .filter(product -> product.getNhaSanXuats().stream()
                    .anyMatch(nsx -> nsx.getTen().contains(brand)))
                .collect(Collectors.toList());
            }
        }
        
        // Lọc sản phẩm theo kích thước
        if (!sizes.isEmpty()) {
            for(String size : sizes){
                uniqueProducts = uniqueProducts.stream()
                .filter(product -> {
                    // lỗi lấy size product ra null
                    String sizeProduct = product.getSize() != null ? product.getSize() : "";
                    return sizeProduct.equals(size);
                })
                .collect(Collectors.toList());
            }
        }
        
        // Lọc sản phẩm theo giá
        if (!priceRanges.isEmpty()) {
            for(PriceRange priceRange : priceRanges){
                uniqueProducts = uniqueProducts.stream()
                .filter(product -> {
                    Long discountedPrice = product.getGiaBan() * (1 - (product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0) / 100);
                    return discountedPrice >= priceRange.getMinPrice() && discountedPrice <= priceRange.getMaxPrice();
                })
                .collect(Collectors.toList());
            }
        }
        
        return uniqueProducts;
    }

    // Lớp PriceRange để lưu trữ cặp giá min và max
    public class PriceRange {
        private final Long minPrice;
        private final Long maxPrice;

        public PriceRange(Long minPrice, Long maxPrice) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
        }

        public Long getMinPrice() {
            return minPrice;
        }

        public Long getMaxPrice() {
            return maxPrice;
        }
    }

    @Override
    public Page<ProductDetailsDTO> getAllPages(int page, int size, List<String> filters) {
        List<SanPham> uniqueProducts = this.filterProducts(filters);

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
    public Page<ProductDetailsDTO> getAllShoesPages(int page, int size, List<String> filters)  {
        List<SanPham> products = this.filterProducts(filters);

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
    public Page<ProductDetailsDTO> getAllBagPages(int page, int size, List<String> filters) {
        List<SanPham> products = this.filterProducts(filters);

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
    public Page<ProductDetailsDTO> getAllClothesPages(int page, int size, List<String> filters) {
        List<SanPham> products = this.filterProducts(filters);

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
    public Page<ProductDetailsDTO> getAllSandalPages(int page, int size, List<String> filters) {
        List<SanPham> products = this.filterProducts(filters);

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
