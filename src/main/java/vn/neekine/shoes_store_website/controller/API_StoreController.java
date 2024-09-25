package vn.neekine.shoes_store_website.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import vn.neekine.shoes_store_website.DTO.InforProductAddToCart;
import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.model.GioHang;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.service.CartService;
import vn.neekine.shoes_store_website.service.ProductService;
import vn.neekine.shoes_store_website.service.UserDetailsService;

@RestController
@RequestMapping("/api/products")
public class API_StoreController {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CartService cartService;

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

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(){
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        KhachHang client = new KhachHang();
        // Nếu đăng nhập bằng Google OAuth2 (qua email)
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Lấy email hoặc bất kỳ thông tin nào khác từ OAuth2User attributes
            String email = (String) oAuth2User.getAttributes().get("email");

            client = this.userDetailsService.loadUserByUsername(email).getKhachHang();
        }
        // Nếu đăng nhập thông thường bằng username
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            client = this.userDetailsService.loadUserByUsername(username).getKhachHang();
        }

        GioHang cart = client.getGioHang();

        if(cart == null){
            cart = new GioHang();
            client.setGioHang(cart);
        }

        return ResponseEntity.ok(this.cartService.getAllProductsInCart(cart));
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addProductToCart(@RequestBody InforProductAddToCart inforProduct){
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            // Trả về thông báo hoặc giỏ hàng trống
            return ResponseEntity.ok("User is not logged in or cart is empty.");
        }

        KhachHang client = new KhachHang();
        // Nếu đăng nhập bằng Google OAuth2 (qua email)
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Lấy email hoặc bất kỳ thông tin nào khác từ OAuth2User attributes
            String email = (String) oAuth2User.getAttributes().get("email");

            client = this.userDetailsService.loadUserByUsername(email).getKhachHang();
        }
        // Nếu đăng nhập thông thường bằng username
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            client = this.userDetailsService.loadUserByUsername(username).getKhachHang();
        }

        GioHang cart = this.cartService.addProductToCart(inforProduct, client);
        
        return ResponseEntity.ok("{}");
    }

    @DeleteMapping("/cart/delete")
    public ResponseEntity<?> deleteProductFromCart(@RequestBody InforProductAddToCart inforProduct){
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KhachHang client = new KhachHang();
        // Nếu đăng nhập bằng Google OAuth2 (qua email)
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Lấy email hoặc bất kỳ thông tin nào khác từ OAuth2User attributes
            String email = (String) oAuth2User.getAttributes().get("email");

            client = this.userDetailsService.loadUserByUsername(email).getKhachHang();
        }
        // Nếu đăng nhập thông thường bằng username
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            client = this.userDetailsService.loadUserByUsername(username).getKhachHang();
        }
        this.cartService.deleteProductsFromCart(inforProduct, client);

        return ResponseEntity.ok("{}");
    }

    @PutMapping("/cart/update")
    public ResponseEntity<?> updateProductsFromCart(@RequestBody List<InforProductAddToCart> inforProducts){
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KhachHang client = new KhachHang();
        // Nếu đăng nhập bằng Google OAuth2 (qua email)
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Lấy email hoặc bất kỳ thông tin nào khác từ OAuth2User attributes
            String email = (String) oAuth2User.getAttributes().get("email");

            client = this.userDetailsService.loadUserByUsername(email).getKhachHang();
        }
        // Nếu đăng nhập thông thường bằng username
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            client = this.userDetailsService.loadUserByUsername(username).getKhachHang();
        }
        
        for(InforProductAddToCart product : inforProducts){
            this.cartService.updateProductsFromCart(product, client);
        }

        return ResponseEntity.ok("{}");
    }
}
