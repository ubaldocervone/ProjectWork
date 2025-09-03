package com.dashboard.controller;

import com.dashboard.model.MonthValue;
import com.dashboard.service.MeteoService;
import com.dashboard.service.PrezziService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DashboardController {

    private final MeteoService meteoService;
    private final PrezziService prezziService;

    public DashboardController(MeteoService meteoService, PrezziService prezziService) {
        this.meteoService = meteoService;
        this.prezziService = prezziService;
    }

    @GetMapping("/yearsMeteo")
    public ResponseEntity<List<Integer>> yearsMeteo() {
        return ResponseEntity.ok(meteoService.getAvailableYears());
    }

    @GetMapping("/yearsPrezzi")
    public ResponseEntity<List<Integer>> yearsPrezzi() {
        return ResponseEntity.ok(prezziService.getAvailableYears());
    }

    @GetMapping("/prezzi/month-series")
    public ResponseEntity<List<MonthValue>> prezziMonthSeries() {
        return ResponseEntity.ok(prezziService.loadMonthSeries());
    }
}
