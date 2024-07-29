package vn.neekine.shoes_store_website.service.serviceImpl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public Page<ProductDetailsDTO> getAllPages(int page, int size) {
        List<SanPham> products = this.productRepository.findAllProducts();

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
}
