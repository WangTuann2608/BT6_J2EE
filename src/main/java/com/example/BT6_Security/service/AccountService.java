package com.example.BT6_Security.service;

import com.example.BT6_Security.model.Account;
import com.example.BT6_Security.reponsitory.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements UserDetailsService { // [cite: 347]

    @Autowired // [cite: 348]
    private AccountRepository accountRepository; // [cite: 349]

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
}
