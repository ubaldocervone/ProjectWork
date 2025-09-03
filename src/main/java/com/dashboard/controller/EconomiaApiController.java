package com.dashboard.controller;

import com.dashboard.model.SeriesResponse;
import com.dashboard.service.EconomiaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/economia")
public class EconomiaApiController {

    private final EconomiaService svc;

    public EconomiaApiController(EconomiaService svc) {
        this.svc = svc;
    }

    /** Esempi:
     *  /api/economia/pil?Territorio=Italia&Misura=milioni di euro
     *  /api/economia/produttivita?Settore=Totale economia
     *  /api/economia/conti?Conto=Reddito disponibile&Territorio=Italia
     */
    @GetMapping("/pil")
    public SeriesResponse pil(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.pil(filtri);
    }

    @GetMapping("/produttivita")
    public SeriesResponse produttivita(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.produttivita(filtri);
    }

    @GetMapping("/conti")
    public SeriesResponse conti(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.conti(filtri);
    }
}
