package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnitaAgricoleController {
    @GetMapping("/unita-agricole")
    public String unitaAgricole(Model model) {
        model.addAttribute("pageTitle", "Unità agricole - in arrivo");
        return "unita_agricole";
    }
}
