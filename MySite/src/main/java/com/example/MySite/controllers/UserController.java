package com.example.MySite.controllers;

import com.example.MySite.models.User;
import com.example.MySite.repositories.UserRepository;
import com.example.MySite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;



    @GetMapping("/user")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user";
    }

    @GetMapping("/user/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }


    @PostMapping("/user/profile")
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String password, @RequestParam String email) {

        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }


}
