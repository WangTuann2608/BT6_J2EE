package com.example.BT6_Security.service;

import com.example.BT6_Security.model.*;
import com.example.BT6_Security.reponsitory.AccountRepository;
import com.example.BT6_Security.reponsitory.OrderDetailRepository;
import com.example.BT6_Security.reponsitory.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public Orders checkout(List<CartItem> cartItems, String username) {
        Account account = accountRepository.findByLoginName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = new Orders();
        order.setOrderDate(new Date());
        order.setAccount(account);
        
        long total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalAmount(total);

        Orders savedOrder = ordersRepository.save(order);

        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setProduct(productService.getProductById(item.getProductId()));
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            orderDetailRepository.save(detail);
        }

        return savedOrder;
    }
}
