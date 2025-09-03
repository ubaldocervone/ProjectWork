package com.dashboard.service.impl;

import com.dashboard.model.MonthValue;
import com.dashboard.repository.PrezziRepository;
import com.dashboard.service.PrezziService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrezziServiceImpl implements PrezziService {

    private final PrezziRepository prezziRepository;

    /**
     * Path di default da configurazione.
     * Esempio in application.yml:
     *
     * data:
     *   prezzi:
     *     path: "classpath:data/prezzi.csv"
     */
    @Value("${data.prezzi.path:classpath:data/prezzi.csv}")
    private String defaultCsvPath;

    public PrezziServiceImpl(@Qualifier("prezziRepositoryCsv") PrezziRepository prezziRepository) {
        this.prezziRepository = prezziRepository;
    }

    @Override
    public List<Integer> getAvailableYears() {
        return getAvailableYears(defaultCsvPath);
    }

    @Override
    public List<Integer> getAvailableYears(String csvPath) {
        String path = (csvPath == null || csvPath.isBlank()) ? defaultCsvPath : csvPath;
        return prezziRepository.getAvailableYears(path);
    }

    @Override
    public List<MonthValue> loadMonthSeries() {
        return loadMonthSeries(defaultCsvPath);
    }

    @Override
    public List<MonthValue> loadMonthSeries(String csvPath) {
        String path = (csvPath == null || csvPath.isBlank()) ? defaultCsvPath : csvPath;
        return prezziRepository.loadMonthSeries(path);
    }
}
