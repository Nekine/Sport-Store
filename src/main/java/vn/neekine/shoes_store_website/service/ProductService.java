package vn.neekine.shoes_store_website.service;

import java.util.List;

import org.springframework.data.domain.Page;

import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.GioHang;
import vn.neekine.shoes_store_website.model.SanPham;

public interface ProductService {
    public List<ProductDetailsDTO> getAllProducts();
    public List<ProductDetailsDTO> searchProduct(String name);
    public List<SanPham> filterProducts(List<String> filters);
    public ProductDetailsDTO getProduct(String name);
    public List<ProductDetailsDTO> relatedProducts (String kindOfProduct, String nameProduct);
    public List<String> getAllSizesProduct(String name);
    public Page<ProductDetailsDTO> getAllPages(int integer, int size, List<String> filters);
    public Page<ProductDetailsDTO> getAllShoesPages(int integer, int size, List<String> filters);
    public Page<ProductDetailsDTO> getAllBagPages(int integer, int size, List<String> filters);
    public Page<ProductDetailsDTO> getAllClothesPages(int integer, int size, List<String> filters);
    public Page<ProductDetailsDTO> getAllSandalPages(int integer, int size, List<String> filters);
}
