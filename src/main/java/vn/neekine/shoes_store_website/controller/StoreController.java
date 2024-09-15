package vn.neekine.shoes_store_website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.neekine.shoes_store_website.DTO.ProductDetailsDTO;
import vn.neekine.shoes_store_website.DTO.RegisterClient_AccountDTO;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.Giohang_Sanpham;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.Role;
import vn.neekine.shoes_store_website.service.AccountService;
import vn.neekine.shoes_store_website.service.ClientService;
import vn.neekine.shoes_store_website.service.ProductService;
import vn.neekine.shoes_store_website.service.RolesService;

@Controller
@RequestMapping("/neekine")
public class StoreController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public String homePage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "Home";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "Home";
        }
    }

    @GetMapping("/collections/all")
    public String productsPage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "Products";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "Products";
        }
    }

    @GetMapping("/collections/shoes")
    public String productsShoesPage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "SneakerProducts";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "SneakerProducts";
        }
    }

    @GetMapping("/collections/sandal")
    public String productsSandalPage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "SandalProducts";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "SandalProducts";
        }
    }

    @GetMapping("/collections/bag")
    public String productsBagPage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "BagProducts";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "BagProducts";
        }
    }

    @GetMapping("/collections/clothes")
    public String productsClothesPage(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "ClothesProducts";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "ClothesProducts";
        }
    }

    @GetMapping("/products/{name}")
    public String productDetailsPage(@PathVariable String name, Model model){
        try {
            ProductDetailsDTO product = this.productService.getProduct(name);

            model.addAttribute("product", product);
            model.addAttribute("sizes", this.productService.getAllSizesProduct(product.getTen()));
            model.addAttribute("relatedProducts", this.productService.relatedProducts(product.getLoai(), product.getTen()));
        } catch (Exception e) {
            // TODO: handle exception
            e.getMessage();
        }

        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "DetalsProduct";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "DetalsProduct";
        }
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("username", "");
            return "Login";
        }

        // Nếu đăng nhập bằng Google OAuth2 (qua email)
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Lấy email hoặc bất kỳ thông tin nào khác từ OAuth2User attributes
            String email = (String) oAuth2User.getAttributes().get("email");
            model.addAttribute("username", email);
        }
        // Nếu đăng nhập thông thường bằng username
        else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("username", username);
        }

        return "Login";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "Admin";
    }

    @GetMapping("/account/register")
    public String registerPage(Model model){
        model.addAttribute("registerClientDTO", new RegisterClient_AccountDTO());

        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "Register";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "Register";
        }
    }

    @PostMapping("/addUser")
    public String registerProcess(@ModelAttribute("registerClientDTO") RegisterClient_AccountDTO user){
        KhachHang client = user.getClient();
        Account account = user.getAccount();

        account.setPassword("{noop}" + account.getPassword());
        account.setKhachHang(client);
        client.setAccount(account);

        Role role = new Role();
        role.setAuthority("ROLE_CLIENT");
        role.setAccount(account);

        this.clientService.createClient(client);
        this.accountService.createAccount(account);
        this.rolesService.createRole(role);
        
        return "redirect:/neekine";
    }

    @GetMapping("/cart")
    public String cart(Model model){
        // Lấy thông tin Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            model.addAttribute("checkLogin", 0);
            return "Cart";
        }
        else {
            model.addAttribute("checkLogin", 1);
            return "Cart";
        }
    }
}
