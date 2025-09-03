package com.dashboard.repository.impl;

import com.dashboard.model.MeteoRecord;
import com.dashboard.repository.MeteoRepository;
import com.dashboard.util.CsvUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class MeteoCsvRepository implements MeteoRepository {

    private final ResourceLoader loader;

    public MeteoCsvRepository(ResourceLoader loader) {
        this.loader = loader;
    }

    /** Legge tutti i record meteo dal CSV indicato. */
    @Override
    public List<MeteoRecord> readAll(String path) throws IOException {
        try (Reader r = CsvUtils.open(path, loader);
             CSVParser p = CsvUtils.parser(r)) {

            Map<String, Integer> h = CsvUtils.headerIndex(p);
            List<MeteoRecord> out = new ArrayList<>();

            for (CSVRecord rec : p) {
                // Data: prova DATE oppure YM/YEAR+MONTH
                LocalDate date = null;
                String sDate = CsvUtils.get(rec, h, CsvUtils.H.DATE);
                if (date == null && sDate != null) {
                    date = CsvUtils.parseDateFlexible(sDate);
                }
                if (date == null) {
                    String ymStr = CsvUtils.get(rec, h, CsvUtils.H.YM);
                    if (ymStr != null) {
                        var ym = CsvUtils.parseYearMonthFlexible(ymStr);
                        if (ym != null) date = ym.atDay(1);
                    }
                }
                if (date == null) {
                    Integer y = CsvUtils.parseIntSafe(CsvUtils.get(rec, h, CsvUtils.H.YEAR));
                    Integer m = CsvUtils.parseIntSafe(CsvUtils.get(rec, h, CsvUtils.H.MONTH));
                    if (y != null && m != null) date = java.time.YearMonth.of(y, m).atDay(1);
                }

                Double t   = CsvUtils.parseDoubleSafe(CsvUtils.get(rec, h, CsvUtils.H.TEMP));
                Double hum = CsvUtils.parseDoubleSafe(CsvUtils.get(rec, h, CsvUtils.H.HUM));
                Double pr  = CsvUtils.parseDoubleSafe(CsvUtils.get(rec, h, CsvUtils.H.PRECIP));

                MeteoRecord mr = new MeteoRecord();
                mr.setDate(date);
                mr.setTemp(t);
                mr.setHum(hum);
                mr.setRain(pr);
                out.add(mr);
            }

            // Ordina per data crescente
            return out.stream()
                    .sorted(Comparator.comparing(MeteoRecord::getDate,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        }
    }

    /** Estrae l’elenco degli anni disponibili nel CSV. */
    @Override
    public List<Integer> getAvailableYears(String path) throws IOException {
        try (Reader r = CsvUtils.open(path, loader);
             CSVParser p = CsvUtils.parser(r)) {
            Map<String, Integer> h = CsvUtils.headerIndex(p);
            return CsvUtils.collectYears(p, h);
        }
    }
}
