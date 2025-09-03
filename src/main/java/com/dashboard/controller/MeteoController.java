package com.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MeteoController {
    @GetMapping("/meteo")
    public String meteo() {
        // Se non hai una pagina dedicata, rimanda alla dashboard meteo
        return "redirect:/dashboard";
    }
}
