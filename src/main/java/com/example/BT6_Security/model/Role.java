package com.example.BT6_Security.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // [cite: 334]

    @Column
    private String name; // [cite: 338]
}