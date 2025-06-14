package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.Role;
import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // залишаємося на формі
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {
        try {
            User loggedInUser = userService.loginUser(user.getEmail(), user.getPassword());
            session.setAttribute("loggedInUser", loggedInUser);

            // перевірка ролі
            if (loggedInUser.getRole() == Role.ADMIN) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }



}




