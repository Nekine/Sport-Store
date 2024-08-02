package vn.neekine.shoes_store_website.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String productsPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "filter", required = false) List<String> filters){
        if(filters == null){
            filters = new ArrayList<>();
        }

        Page<ProductDetailsDTO> pages = this.productService.getAllPages(page-1, 20, filters);

        model.addAttribute("products", pages);
        return "Products";
    }

    @GetMapping("/collections/shoes")
    public String productsShoesPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        Page<ProductDetailsDTO> pages = this.productService.getAllShoesPages(page-1, 20);

        model.addAttribute("products", pages);
        return "SneakerProducts";
    }

    @GetMapping("/collections/sandal")
    public String productsSandalPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        Page<ProductDetailsDTO> pages = this.productService.getAllSandalPages(page-1, 20);

        model.addAttribute("products", pages);
        return "SandalProducts";
    }

    @GetMapping("/collections/bag")
    public String productsBagPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        Page<ProductDetailsDTO> pages = this.productService.getAllBagPages(page-1, 20);

        model.addAttribute("products", pages);
        return "BagProducts";
    }

    @GetMapping("/collections/clothes")
    public String productsClothesPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        Page<ProductDetailsDTO> pages = this.productService.getAllClothesPages(page-1, 20);

        model.addAttribute("products", pages);
        return "ClothesProducts";
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
