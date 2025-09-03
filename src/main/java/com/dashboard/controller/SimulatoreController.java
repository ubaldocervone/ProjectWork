package com.dashboard.controller;

import com.dashboard.model.SimulationParams;
import com.dashboard.service.SimulatoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SimulatoreController {

    private final SimulatoreService simulatoreService;

    public SimulatoreController(SimulatoreService simulatoreService) {
        this.simulatoreService = simulatoreService;
    }

    @GetMapping("/simulatore")
    public String simulatore(Model model) {
        // Valori di default facoltativi
        SimulationParams params = new SimulationParams();
        params.setTemperaturaMedia(20.0);
        params.setUmiditaMedia(65.0);
        params.setPrecipitazioni(2.0);
        params.setRadiazioneSolare(150.0);
        params.setPressioneMedia(1013.0);

        model.addAttribute("params", params);
        return "simulatore";
    }

    @PostMapping("/simulatore/run")
    public String run(@ModelAttribute("params") SimulationParams params, Model model) {
        // Output molto semplice: un “indice produzione” fittizio
        double produzione = simulatoreService.stimaProduzione(params);
        model.addAttribute("produzioneStimata", produzione);
        return "simulatore";
    }
}
