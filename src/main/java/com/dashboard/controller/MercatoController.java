package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MercatoController {
    @GetMapping("/mercato")
    public String mercato(Model model) {
        model.addAttribute("pageTitle", "Mercato (Indice prezzi agricoli)");
        return "mercato";
    }
}
