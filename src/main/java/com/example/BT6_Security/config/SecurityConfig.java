package com.example.BT6_Security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Sử dụng BCrypt để mã hóa mật khẩu
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Cấu hình AuthenticationManager để xử lý xác thực
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // Cho phép truy cập công khai vào trang đăng nhập và đăng ký
                .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Quyền truy cập cho các endpoint dựa trên bảng phân quyền
                .requestMatchers("/products", "/cart/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/products/add", "/products/save",
                        "/products/edit/**", "/products/update/**",
                        "/products/delete/**").hasRole("ADMIN")
                .requestMatchers("/order").hasRole("USER")
                // Yêu cầu xác thực với tất cả các request khác
                .anyRequest().authenticated()
        )
        // Cấu hình form đăng nhập tùy chỉnh
        .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/products", true)
                .failureUrl("/login?error")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
        );

        return http.build();
    }
}
