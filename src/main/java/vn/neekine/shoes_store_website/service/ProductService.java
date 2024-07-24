package vn.neekine.shoes_store_website.service;

import java.util.List;

import vn.neekine.shoes_store_website.DTO.ProductDTO;

public interface ProductService {
    public List<ProductDTO> getAllProducts();
    public List<ProductDTO> searchProduct(String name);
}
