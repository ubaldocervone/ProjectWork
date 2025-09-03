package com.dashboard.service;

import java.util.List;

/**
 * Servizio per dati meteo.
 */
public interface MeteoService {

    /**
     * Ritorna tutti gli anni disponibili leggendo dal path configurato
     * (application.yml) o dal fallback se non presente.
     */
    List<Integer> getAvailableYears();

    /**
     * Ritorna tutti gli anni disponibili leggendo dal CSV indicato.
     */
    List<Integer> getAvailableYears(String csvPath);
}
