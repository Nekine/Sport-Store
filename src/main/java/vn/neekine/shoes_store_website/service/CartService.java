package vn.neekine.shoes_store_website.service;

import java.util.List;

import vn.neekine.shoes_store_website.DTO.InforProductAddToCart;
import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.GioHang;
import vn.neekine.shoes_store_website.model.KhachHang;

public interface CartService {
    public GioHang addProductToCart(InforProductAddToCart addToCart, KhachHang client);
    public void deleteProductsFromCart(InforProductAddToCart productFromCart, KhachHang client);
    public List<ProductDetailsDTO> getAllProductsInCart(GioHang cart);
}
