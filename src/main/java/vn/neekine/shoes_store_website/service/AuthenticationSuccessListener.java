package vn.neekine.shoes_store_website.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.Role;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {


    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService userService;

    @Autowired
    private RolesService rolesService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // Lấy thông tin người dùng từ sự kiện
        OAuth2User oAuth2User = (OAuth2User) event.getAuthentication().getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");

        // In thông tin đăng nhập
        System.out.println("Đăng nhập thành công qua Google/Facebook!");
        System.out.println("Tên: " + name);
        System.out.println("Email: " + email);

        // Kiểm tra nếu tài khoản chưa đăng nhập lần nào
        if (userService.isFirstTimeLogin(email)) {
            Account account = new Account();
            account.setPassword("{noop}dangnhapbangcachkhac"); // Mật khẩu mặc định, có thể thay đổi theo nhu cầu
            account.setEmail(email);
            account.setEnabled(true);

            KhachHang client = new KhachHang();
            client.setFullName(name);
            client.setAccount(account);
            account.setKhachHang(client);

            Role role = new Role();
            role.setAuthority("ROLE_CLIENT");
            role.setAccount(account);

            // Lưu thông tin người dùng vào cơ sở dữ liệu
            // clientService.createClient(client);
            userService.createAccount(account);
            rolesService.createRole(role);
        }
    }
}
