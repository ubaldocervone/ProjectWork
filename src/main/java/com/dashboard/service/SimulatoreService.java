package com.dashboard.service;

import com.dashboard.model.SimulationParams;
import org.springframework.stereotype.Service;

@Service
public class SimulatoreService {

    /**
     * Stima di produzione molto semplice (placeholder):
     * più radiazione → maggiore produzione, troppa umidità/precipitazioni la riducono.
     */
    public double stimaProduzione(SimulationParams p) {
        double base = p.getRadiazioneSolare() * 0.8;
        double tempAdj = 1.0 - Math.max(0, Math.abs(p.getTemperaturaMedia() - 22.0)) * 0.01; // ottimo ~22°C
        double umidAdj = 1.0 - (p.getUmiditaMedia() / 200.0); // 0..100% → -0..0.5
        double rainAdj = 1.0 - (Math.min(p.getPrecipitazioni(), 10.0) / 50.0); // 0..10mm → -0..0.2
        double pressAdj = 1.0; // non usata: puoi modularla

        double produzione = base * tempAdj * umidAdj * rainAdj * pressAdj;
        return Math.max(0, produzione);
    }
}
