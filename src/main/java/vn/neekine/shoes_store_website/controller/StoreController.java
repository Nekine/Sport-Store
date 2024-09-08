package vn.neekine.shoes_store_website.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import vn.neekine.shoes_store_website.model.GioHang;
import vn.neekine.shoes_store_website.model.Giohang_Sanpham;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.Role;
import vn.neekine.shoes_store_website.service.AccountService;
import vn.neekine.shoes_store_website.service.CartService;
import vn.neekine.shoes_store_website.service.ClientService;
import vn.neekine.shoes_store_website.service.ProductService;
import vn.neekine.shoes_store_website.service.RolesService;
import vn.neekine.shoes_store_website.service.UserDetailsService;

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

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CartService cartService;
    
    @GetMapping
    public String homePage(){
        return "Home";
    }

    @GetMapping("/collections/all")
    public String productsPage(){
        return "Products";
    }

    @GetMapping("/collections/shoes")
    public String productsShoesPage(){
        return "SneakerProducts";
    }

    @GetMapping("/collections/sandal")
    public String productsSandalPage(){
        return "SandalProducts";
    }

    @GetMapping("/collections/bag")
    public String productsBagPage(){
        return "BagProducts";
    }

    @GetMapping("/collections/clothes")
    public String productsClothesPage(){
        return "ClothesProducts";
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
        return "DetalsProduct";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "Login";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "Admin";
    }

    @GetMapping("/account/register")
    public String registerPage(Model model){
        model.addAttribute("registerClientDTO", new RegisterClient_AccountDTO());
        return "Register";
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
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userCurrent = (UserDetails) authentication.getPrincipal();
        KhachHang khachHang = this.userDetailsService.loadUserByUsername(userCurrent.getUsername()).getKhachHang();
        GioHang cart = khachHang.getGioHang();

        if(cart == null){
            cart = new GioHang();
            khachHang.setGioHang(cart);
        }

        List<ProductDetailsDTO> products_From_Cart = this.cartService.getAllProductsInCart(cart);
        int totalQuantity = 0;
        int sumCostAllProducts = 0;
        for(ProductDetailsDTO p : products_From_Cart){
            totalQuantity += p.getSo_luong();
            sumCostAllProducts += p.getSo_luong()*p.getGia_ban()*(1 - p.getPhan_tram()/100.0);
        }

        model.addAttribute("cart", products_From_Cart);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("sumCost", sumCostAllProducts);
        return "Cart";
    }
}
