package com.example.MySite.controllers;

import com.example.MySite.models.Events;
import com.example.MySite.models.User;
import com.example.MySite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.transaction.Transactional;

@Controller
@Transactional
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
        model.addAttribute("name", user.getName());
        model.addAttribute("secondName", user.getSecondName());
        model.addAttribute("country", user.getCountry());
        model.addAttribute("age", user.getAge());

        return "profile";
    }


    @PostMapping("/user/profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String name,
                                @RequestParam String secondName,
                                @RequestParam String country,
                                @RequestParam int age) {

        userService.updateProfile(user, password, email, name, secondName, country, age);

        return "redirect:/user/profile";
    }

    @GetMapping("/user/eventslist")
    @ElementCollection(fetch = FetchType.EAGER)
    public String eventsList(Model model, @AuthenticationPrincipal User user) {

        Iterable<Events> events = user.getTournaments();

        model.addAttribute("events", user.getTournaments());

        return "userEvents";
    }
}
