package vn.neekine.shoes_store_website.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
}
