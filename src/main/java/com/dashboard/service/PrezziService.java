package com.dashboard.service;

import com.dashboard.model.MonthValue;

import java.util.List;

/**
 * Servizio per serie prezzi.
 */
public interface PrezziService {

    /**
     * Anni disponibili (usa il path di default da configurazione).
     */
    List<Integer> getAvailableYears();

    /**
     * Anni disponibili dal CSV indicato.
     */
    List<Integer> getAvailableYears(String csvPath);

    /**
     * Serie mensile (usa il path di default da configurazione).
     */
    List<MonthValue> loadMonthSeries();

    /**
     * Serie mensile dal CSV indicato.
     */
    List<MonthValue> loadMonthSeries(String csvPath);
}
