package vn.neekine.shoes_store_website.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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
    public Page<ProductDetailsDTO> productsPage(@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request){
        List<String> filters = new ArrayList<String>();

        // Lấy chuỗi truy vấn từ request
        String queryString = request.getQueryString();

        // Kiểm tra nếu chuỗi truy vấn không rỗng và bắt đầu với "filter="
        if (queryString != null && queryString.startsWith("filter=")) {
            // Lấy phần sau "filter=" và tách các giá trị filter dựa theo dấu "&"
            String filtersString = queryString.substring("filter=".length());
            filters = Arrays.stream(filtersString.split("&"))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/collections/shoes")
    public Page<ProductDetailsDTO> productsShoesPage(@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request){
        List<String> filters = new ArrayList<String>();

        // Lấy chuỗi truy vấn từ request
        String queryString = request.getQueryString();

        // Kiểm tra nếu chuỗi truy vấn không rỗng và bắt đầu với "filter="
        if (queryString != null && queryString.startsWith("filter=")) {
            // Lấy phần sau "filter=" và tách các giá trị filter dựa theo dấu "&"
            String filtersString = queryString.substring("filter=".length());
            filters = Arrays.stream(filtersString.split("&"))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllShoesPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/collections/clothes")
    public Page<ProductDetailsDTO> productsClothesPage(@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request){
        List<String> filters = new ArrayList<String>();

        // Lấy chuỗi truy vấn từ request
        String queryString = request.getQueryString();

        // Kiểm tra nếu chuỗi truy vấn không rỗng và bắt đầu với "filter="
        if (queryString != null && queryString.startsWith("filter=")) {
            // Lấy phần sau "filter=" và tách các giá trị filter dựa theo dấu "&"
            String filtersString = queryString.substring("filter=".length());
            filters = Arrays.stream(filtersString.split("&"))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllClothesPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/collections/bag")
    public Page<ProductDetailsDTO> productsBagPage(@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request){
        List<String> filters = new ArrayList<String>();

        // Lấy chuỗi truy vấn từ request
        String queryString = request.getQueryString();

        // Kiểm tra nếu chuỗi truy vấn không rỗng và bắt đầu với "filter="
        if (queryString != null && queryString.startsWith("filter=")) {
            // Lấy phần sau "filter=" và tách các giá trị filter dựa theo dấu "&"
            String filtersString = queryString.substring("filter=".length());
            filters = Arrays.stream(filtersString.split("&"))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllBagPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/collections/sandal")
    public Page<ProductDetailsDTO> productsSandalPage(@RequestParam(name = "page", defaultValue = "1") int page, HttpServletRequest request){
        List<String> filters = new ArrayList<String>();

        // Lấy chuỗi truy vấn từ request
        String queryString = request.getQueryString();

        // Kiểm tra nếu chuỗi truy vấn không rỗng và bắt đầu với "filter="
        if (queryString != null && queryString.startsWith("filter=")) {
            // Lấy phần sau "filter=" và tách các giá trị filter dựa theo dấu "&"
            String filtersString = queryString.substring("filter=".length());
            filters = Arrays.stream(filtersString.split("&"))
                            .map(String::trim)
                            .collect(Collectors.toList());
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllSandalPages(page-1, 20, filters);

        return pages;
    }

    @GetMapping("/search")
    public List<ProductDetailsDTO> searchProducts(@RequestParam("keyword") String keyword) {
        return this.productService.searchProduct(keyword);
    }

    @GetMapping("details/{name}")
    public List<ProductDetailsDTO> products(@PathVariable String name){
        
        ProductDetailsDTO product = this.productService.getProduct(name);
        return this.productService.relatedProducts(product.getLoai(), product.getTen());
    }
}
