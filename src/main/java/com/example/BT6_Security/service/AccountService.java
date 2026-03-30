package com.example.BT6_Security.service;

import com.example.BT6_Security.model.Account;
import com.example.BT6_Security.reponsitory.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BT6_Security.model.Role;
import com.example.BT6_Security.reponsitory.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService { // [cite: 347]

    @Autowired // [cite: 348]
    private AccountRepository accountRepository; // [cite: 349]

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // [cite: 351]

        // Tìm kiếm tài khoản trong database
                Account account = accountRepository.findByLoginName(username) // [cite: 352]
                .orElseThrow(() -> new UsernameNotFoundException("Could not find user")); // [cite: 353]

        // Chuyển đổi danh sách Role của Entity sang định dạng Authority mà Spring Security hiểu được
        Set<SimpleGrantedAuthority> authorities = account.getRoles().stream() // [cite: 354]
                .map(role -> new SimpleGrantedAuthority(role.getName())) // [cite: 355]
                .collect(Collectors.toSet()); // [cite: 356]

        // Trả về đối tượng User của Spring Security
        return new org.springframework.security.core.userdetails.User( // [cite: 357]
                account.getLoginName(), // [cite: 358]
                account.getPassword(), // [cite: 359]
                authorities // [cite: 360]
        ); // [cite: 361]
    }

    public Account registerAccount(Account account) {
        if (accountRepository.existsByLoginName(account.getLoginName())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        
        // Mặc định gán ROLE_USER cho tài khoản mới
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_USER");
                    return roleRepository.save(role);
                });
        
        account.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        return accountRepository.save(account);
    }
}
