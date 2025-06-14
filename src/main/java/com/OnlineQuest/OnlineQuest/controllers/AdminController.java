package com.OnlineQuest.OnlineQuest.controllers;

import com.OnlineQuest.OnlineQuest.model.User;
import com.OnlineQuest.OnlineQuest.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String adminForm(Model model) {
        model.addAttribute("users", adminService.findAll());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        adminService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/updateRole/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam("role") String role) {
        try {
            adminService.updateUserRole(id, role);
        } catch (Exception e) {
            e.printStackTrace(); // або додай повідомлення у модель
        }
        return "redirect:/admin";
    }

}
