package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TavoleController {
    @GetMapping("/tavole")
    public String tavole(Model model) {
        model.addAttribute("pageTitle", "Tavole - in arrivo");
        return "tavole";
    }
}
