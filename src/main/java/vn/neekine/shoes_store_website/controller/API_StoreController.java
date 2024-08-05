package vn.neekine.shoes_store_website.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class API_StoreController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDetailsDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/collections/all")
    public Page<ProductDetailsDTO> productsPage(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "filter", required = false) List<String> filters){
        if(filters == null){
            filters = new ArrayList<>();
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/search")
    public List<ProductDetailsDTO> searchProducts(@RequestParam("keyword") String keyword) {
        return this.productService.searchProduct(keyword);
    }
}
