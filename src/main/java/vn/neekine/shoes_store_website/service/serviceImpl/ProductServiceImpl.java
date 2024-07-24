package vn.neekine.shoes_store_website.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.neekine.shoes_store_website.DTO.ProductDTO;
import vn.neekine.shoes_store_website.model.Anh;
import vn.neekine.shoes_store_website.model.SanPham;
import vn.neekine.shoes_store_website.repository.ProductRepository;
import vn.neekine.shoes_store_website.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        List<SanPham> products = productRepository.findAllProducts();

        return products.stream()
                        .map(product -> {
                            Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                            List<String> photoNames = product.getPhotos().stream()
                                                                .map(Anh::getTen)
                                                                .collect(Collectors.toList());
                            return new ProductDTO(
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
    public List<ProductDTO> searchProduct(String name) {
        List<SanPham> products = productRepository.searchProductByName(name);

        return products.stream()
                        .map(product -> {
                            Integer phanTram = product.getKhuyenMai() != null ? product.getKhuyenMai().getPhanTram() : 0;
                            List<String> photoNames = product.getPhotos().stream()
                                                                .map(Anh::getTen)
                                                                .collect(Collectors.toList());
                            return new ProductDTO(
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

}
