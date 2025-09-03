package com.dashboard.controller;

import com.dashboard.model.MonthValue;
import com.dashboard.service.PrezziService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PrezziController {

    private final PrezziService prezziService;

    public PrezziController(PrezziService prezziService) {
        this.prezziService = prezziService;
    }

    @GetMapping("/yearsPrezzi")
    public ResponseEntity<List<Integer>> yearsPrezzi() throws Exception {
        return ResponseEntity.ok(prezziService.getAvailableYears());
    }

    @GetMapping("/prezzi/series")
    public ResponseEntity<List<MonthValue>> seriesPrezzi() throws Exception {
        return ResponseEntity.ok(prezziService.loadMonthSeries());
    }
}
