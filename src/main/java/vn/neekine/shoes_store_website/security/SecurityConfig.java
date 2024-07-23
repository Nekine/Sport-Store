package vn.neekine.shoes_store_website.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
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
                            .requestMatchers("/neekine/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll()    
            )
            // đường dẫn đến trang đăng nhập
            .formLogin(form ->
                    form
                                .loginPage("/neekine/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .defaultSuccessUrl("/neekine") // URL chuyển hướng sau khi đăng nhập thành công
                                .permitAll()
            )
            // đường dẫn đến trang đăng nhập khi muốn đăng xuất 
            //.logout(logout -> logout.permitAll())
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
