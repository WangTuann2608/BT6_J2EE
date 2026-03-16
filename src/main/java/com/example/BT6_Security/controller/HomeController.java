package com.example.BT6_Security.controller;

import com.example.BT6_Security.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "home";
    }
}