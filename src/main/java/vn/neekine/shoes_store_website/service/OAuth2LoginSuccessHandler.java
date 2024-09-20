package vn.neekine.shoes_store_website.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.Role;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService userService;

    @Autowired
    private RolesService rolesService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (userService.isFirstTimeLogin(email)) {
            KhachHang client = new KhachHang();
            Account account = new Account();

            account.setPassword("{noop}dangnhapbangcachkhac");
            account.setEmail(email);
            client.setFullName(name);
            client.setAccount(account);

            Role role = new Role();
            role.setAuthority("ROLE_CLIENT");
            role.setAccount(account);

            this.clientService.createClient(client);
            this.userService.createAccount(account);
            this.rolesService.createRole(role);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

