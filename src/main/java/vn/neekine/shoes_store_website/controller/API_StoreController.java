package vn.neekine.shoes_store_website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.neekine.shoes_store_website.DTO.ProductDTO;
import vn.neekine.shoes_store_website.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class API_StoreController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestParam("keyword") String keyword) {
        return this.productService.searchProduct(keyword);
    }
}
