package vn.neekine.shoes_store_website.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.neekine.shoes_store_website.DTO.RegisterClient_AccountDTO;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.service.AccountService;
import vn.neekine.shoes_store_website.service.ClientService;

@Controller
@RequestMapping("/neekine")
public class StoreController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public String homePage(){
        return "Home";
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

    @PostMapping("/account/register")
    public String registerProcess(@ModelAttribute("registerClientDTO") RegisterClient_AccountDTO user){
        KhachHang client = user.getClient();
        Account account = user.getAccount();

        this.clientService.createClient(client);
        this.accountService.createAccount(account);
        
        return "redirect:/neekine";
    }
}
