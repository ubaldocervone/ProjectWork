package com.dashboard.controller;

import com.dashboard.service.MeteoService;
import com.dashboard.service.PrezziService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final MeteoService meteoService;
    private final PrezziService prezziService;

    @Value("${app.meteo.path:classpath:data/meteo.csv}")
    private String meteoPath;

    @Value("${app.prezzi.path:classpath:data/prezzi.csv}")
    private String prezziPath;

    public ApiController(MeteoService meteoService, PrezziService prezziService) {
        this.meteoService = meteoService;
        this.prezziService = prezziService;
    }

    @GetMapping("/yearsMeteo")
    public ResponseEntity<List<Integer>> yearsMeteo() throws IOException {
        return ResponseEntity.ok(meteoService.getAvailableYears(meteoPath));
    }

    @GetMapping("/yearsPrezzi")
    public ResponseEntity<List<Integer>> yearsPrezzi() throws IOException {
        return ResponseEntity.ok(prezziService.getAvailableYears(prezziPath));
    }
}
