package com.example.MySite.controllers;

import com.example.MySite.models.User;
import com.example.MySite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (user.getPassword() != null && !user.getPassword().equals(user.getPassword2())) {
            model.addAttribute("passwordDifferenceError", "Пароли не совпадают!");
            return "registration";
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = UtilsController.getErrors(bindingResult);
            System.out.println(errors);

            model.addAttribute("passwordErrors", errors.get("passwordError:"));
            model.addAttribute("usernameErrors", errors.get("usernameError:"));

            return "registration";
        }
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "Такой пользователь уже существует!");
            return "registration";
        }

        return  "redirect:/login";
    }

    // Страница отправки кода подтверждения
    @GetMapping("/activate/{code}")
    public String activate(@PathVariable(value = "code") String code, Model model) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activated code is not found");
        }

        return "login";
    }
}
