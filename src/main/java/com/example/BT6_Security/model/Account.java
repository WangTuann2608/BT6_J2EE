package com.example.BT6_Security.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // [cite: 257]

    @Column(name = "login_name")
    private String loginName; // [cite: 258]
    private String password; // [cite: 258]

    // Khai báo quan hệ nhiều-nhiều với Role thông qua bảng trung gian AccountRole
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"), // [cite: 280]
            inverseJoinColumns = @JoinColumn(name = "role_id") // [cite: 280]
    )
    private Set<Role> roles; // [cite: 312]
}
