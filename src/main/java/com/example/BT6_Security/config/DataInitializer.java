package com.example.BT6_Security.config;

import com.example.BT6_Security.model.Account;
import com.example.BT6_Security.model.Category;
import com.example.BT6_Security.model.Role;
import com.example.BT6_Security.reponsitory.AccountRepository;
import com.example.BT6_Security.reponsitory.CategoryReponsitory;
import com.example.BT6_Security.reponsitory.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDefaultAccounts(
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            CategoryReponsitory categoryReponsitory,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_USER");
                        return roleRepository.save(role);
                    });

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_ADMIN");
                        return roleRepository.save(role);
                    });

            if (!accountRepository.existsByLoginName("user")) {
                Account userAccount = new Account();
                userAccount.setLoginName("user");
                userAccount.setPassword(passwordEncoder.encode("123456"));
                userAccount.setRoles(Set.of(userRole));
                accountRepository.save(userAccount);
            }

            if (!accountRepository.existsByLoginName("admin")) {
                Account adminAccount = new Account();
                adminAccount.setLoginName("admin");
                adminAccount.setPassword(passwordEncoder.encode("123456"));
                adminAccount.setRoles(Set.of(adminRole));
                accountRepository.save(adminAccount);
            }

            // Seed danh mục mặc định
            List<String> defaultCategories = List.of(
                    "Điện thoại",
                    "Laptop",
                    "Máy tính bảng",
                    "Âm thanh",
                    "Phụ kiện",
                    "Đồng hồ thông minh"
            );
            for (String catName : defaultCategories) {
                if (!categoryReponsitory.existsByName(catName)) {
                    Category category = new Category();
                    category.setName(catName);
                    categoryReponsitory.save(category);
                }
            }
        };
    }
}