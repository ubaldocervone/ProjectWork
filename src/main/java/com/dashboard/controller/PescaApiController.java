package com.dashboard.controller;

import com.dashboard.model.SeriesResponse;
import com.dashboard.service.PescaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pesca")
public class PescaApiController {

    private final PescaService svc;

    public PescaApiController(PescaService svc) {
        this.svc = svc;
    }

    // /api/pesca/servizi?Regione=Puglia&Tipo=Imprese
    @GetMapping("/servizi")
    public SeriesResponse servizi(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.servizi(filtri);
    }

    // /api/pesca/dati?Specie=Totale&Area=Italia
    @GetMapping("/dati")
    public SeriesResponse dati(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.dati(filtri);
    }

    // /api/pesca/porti?Regione=Puglia
    @GetMapping("/porti")
    public SeriesResponse porti(@RequestParam Map<String,String> filtri) throws Exception {
        return svc.porti(filtri);
    }
}
