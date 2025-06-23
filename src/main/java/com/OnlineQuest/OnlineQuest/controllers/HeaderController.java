package com.OnlineQuest.OnlineQuest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HeaderController {

    @GetMapping("/about")
    public String showAboutPage() {
        return "about";
    }
}
