package com.example.BT6_Security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.BT6_Security.model.Product;
import com.example.BT6_Security.reponsitory.ProductReponsitory;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductReponsitory productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}