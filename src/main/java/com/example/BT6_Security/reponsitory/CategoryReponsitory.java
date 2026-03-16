package com.example.BT6_Security.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.BT6_Security.model.Category;

@Repository
public interface CategoryReponsitory extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
}

