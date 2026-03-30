package com.example.BT6_Security.reponsitory;

import com.example.BT6_Security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> { // [cite: 368]

    // Truy vấn lấy đối tượng Account có loginName bằng với tham số truyền vào. [cite: 369]
    Optional<Account> findByLoginName(String loginName); // [cite: 368]

    boolean existsByLoginName(String loginName);
    boolean existsByEmail(String email);
}