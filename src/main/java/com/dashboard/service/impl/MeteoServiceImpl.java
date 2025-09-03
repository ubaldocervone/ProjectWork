package com.dashboard.service.impl;

import com.dashboard.repository.MeteoRepository;
import com.dashboard.service.MeteoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MeteoServiceImpl implements MeteoService {

    private final MeteoRepository meteoRepository;

    /**
     * Path di default da configurazione.
     * Esempio in application.yml:
     *
     * data:
     *   meteo:
     *     path: "classpath:data/meteo.csv"
     */
    @Value("${data.meteo.path:classpath:data/meteo.csv}")
    private String defaultCsvPath;

    public MeteoServiceImpl(@Qualifier("meteoRepositoryCsv") MeteoRepository meteoRepository) {
        this.meteoRepository = meteoRepository;
    }

    @Override
    public List<Integer> getAvailableYears() {
        return getAvailableYears(defaultCsvPath);
    }

    @Override
    public List<Integer> getAvailableYears(String csvPath) {
        String path = (csvPath == null || csvPath.isBlank()) ? defaultCsvPath : csvPath;
        return meteoRepository.getAvailableYears(path);
    }
}
