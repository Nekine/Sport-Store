package vn.neekine.shoes_store_website.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.SanPham;

public interface ProductService {
    public List<ProductDetailsDTO> getAllProducts();
    public List<ProductDetailsDTO> searchProduct(String name);
    public Page<ProductDetailsDTO> getAllPages(int integer, int size);
}
