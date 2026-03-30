package com.example.BT6_Security.controller;

import com.example.BT6_Security.model.CartItem;
import com.example.BT6_Security.model.Product;
import com.example.BT6_Security.service.OrderService;
import com.example.BT6_Security.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> getCart(HttpSession session) {
        Map<Long, CartItem> cart = (Map<Long, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session,
            @RequestHeader(value = "Referer", required = false) String referer) {
        Product product = productService.getProductById(id);
        if (product != null) {
            Map<Long, CartItem> cart = getCart(session);
            CartItem item = cart.get(id);
            if (item == null) {
                item = new CartItem(id, product.getName(), product.getPrice(), product.getImage(), quantity);
                cart.put(id, item);
            } else {
                item.setQuantity(item.getQuantity() + quantity);
            }
        }

        // Quay lại trang trước đó thay vì vào thẳng giỏ hàng
        String redirectUrl = (referer != null ? referer : "/products");
        // Tránh trùng lặp tham số nếu đã có ?added
        if (!redirectUrl.contains("added")) {
            redirectUrl += (redirectUrl.contains("?") ? "&" : "?") + "added";
        }
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        List<CartItem> items = new ArrayList<>(cart.values());

        long totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        model.addAttribute("cartItems", items);
        model.addAttribute("totalAmount", totalAmount);
        return "cart/view";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        Map<Long, CartItem> cart = getCart(session);
        cart.remove(id);
        return "redirect:/cart/view";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, Authentication authentication, Model model) {
        Map<Long, CartItem> cart = getCart(session);
        if (cart.isEmpty()) {
            return "redirect:/cart/view?error=empty";
        }

        List<CartItem> items = new ArrayList<>(cart.values());
        try {
            orderService.checkout(items, authentication.getName());
            session.removeAttribute("cart");
            return "redirect:/cart/success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "cart/view";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "cart/success";
    }
}
