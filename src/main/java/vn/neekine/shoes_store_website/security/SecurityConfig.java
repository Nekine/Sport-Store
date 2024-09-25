package vn.neekine.shoes_store_website.security;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.neekine.shoes_store_website.model.Account;
import vn.neekine.shoes_store_website.model.KhachHang;
import vn.neekine.shoes_store_website.model.Role;
import vn.neekine.shoes_store_website.service.AccountService;
import vn.neekine.shoes_store_website.service.ClientService;
//import vn.neekine.shoes_store_website.service.OAuth2LoginSuccessHandler;
import vn.neekine.shoes_store_website.service.RolesService;

@Configuration
public class SecurityConfig {
    // @Autowired
    // private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    // truy vấn Database để lấy thông tin Spring biết để xác thực thông tin đăng nhập khi truy cập API
    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from dbo.tai_khoan where username = ?");

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, authority from dbo.roles where username =?");

        return jdbcUserDetailsManager;
    }

    @Bean // chuyển form đăng nhập mặc định sang form của mình
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // cấp quyền cho người dùng
            .authorizeHttpRequests(configurer -> 
                    configurer
                            .requestMatchers(HttpMethod.GET, "/neekine/admin/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/neekine/cart").hasRole("CLIENT")
                            .anyRequest().permitAll()    
            )
            // đường dẫn đến trang đăng nhập
            .formLogin(form ->
                    form
                                .loginPage("/neekine/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/neekine", true)
                                .permitAll()
            )
            // Tích hợp đăng nhập bằng Google OAuth2
            .oauth2Login(oauth2 -> 
                    oauth2
                                .loginPage("/neekine/login") // Trang đăng nhập chung (bao gồm Google)
                                // .successHandler(oAuth2LoginSuccessHandler)
                                .defaultSuccessUrl("/neekine") // Chuyển hướng sau khi đăng nhập bằng Google thành công
            )
            // đường dẫn đến trang đăng nhập khi muốn đăng xuất 
            // Đường dẫn đến trang đăng xuất
            .logout(logout -> 
                    logout
                            .logoutUrl("/neekine/logout") // URL để xử lý khi đăng xuất
                            .logoutSuccessUrl("/neekine/login") // Chuyển hướng sau khi đăng xuất thành công
                            .invalidateHttpSession(true) // Hủy session hiện tại sau khi đăng xuất
                            .deleteCookies("JSESSIONID") // Xóa cookie của session
                            .permitAll() // Cho phép tất cả người dùng truy cập URL này
            )
            // đường dẫn đến trang khi không truy cập được trang mong muốn
            .exceptionHandling(configurer -> 
                configurer.accessDeniedPage("/neekine")
            );
        http.httpBasic(Customizer.withDefaults()); //  là một cách đơn giản để kích hoạt HTTP Basic Authentication
        // trong Spring Security với các cài đặt mặc định.

        http.csrf(csrf -> csrf.disable()); // là cách để vô hiệu hóa bảo vệ CSRF trong Spring Security.
        return http.build();
    }
}
