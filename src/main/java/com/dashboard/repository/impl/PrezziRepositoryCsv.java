package com.dashboard.repository.impl;

import com.dashboard.model.MonthValue;
import com.dashboard.repository.PrezziRepository;
import com.dashboard.util.CsvUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Primary
public class PrezziRepositoryCsv implements PrezziRepository {

    private final ResourceLoader loader;

    public PrezziRepositoryCsv(ResourceLoader loader) {
        this.loader = loader;
    }

    /** Serie mensile (YearMonth -> valore) letta dal CSV. */
    @Override
    public List<MonthValue> loadMonthSeries(String path) throws IOException {
        try (Reader r = CsvUtils.open(path, loader);
             CSVParser p = CsvUtils.parser(r)) {

            Map<String, Integer> h = CsvUtils.headerIndex(p);
            List<MonthValue> out = new ArrayList<>();

            for (CSVRecord rec : p) {
                // YM può essere in una colonna oppure derivato da YEAR+MONTH
                YearMonth ym = null;

                String ymStr = CsvUtils.get(rec, h, CsvUtils.H.YM);
                if (ymStr != null) {
                    ym = CsvUtils.parseYearMonthFlexible(ymStr);
                }
                if (ym == null) {
                    Integer y = CsvUtils.parseIntSafe(CsvUtils.get(rec, h, CsvUtils.H.YEAR));
                    Integer m = CsvUtils.parseIntSafe(CsvUtils.get(rec, h, CsvUtils.H.MONTH));
                    if (y != null && m != null) ym = YearMonth.of(y, m);
                }

                Double val = CsvUtils.parseDoubleSafe(CsvUtils.get(rec, h, CsvUtils.H.VALUE));
                if (ym != null && val != null) {
                    out.add(new MonthValue(ym, val));
                }
            }

            // ordina per YearMonth crescente
            return out.stream()
                    .sorted(Comparator.comparing(MonthValue::getYearMonth))
                    .collect(Collectors.toList());
        }
    }

    /** Anni presenti nella serie prezzi. */
    @Override
    public List<Integer> getAvailableYears(String path) throws IOException {
        try (Reader r = CsvUtils.open(path, loader);
             CSVParser p = CsvUtils.parser(r)) {
            Map<String, Integer> h = CsvUtils.headerIndex(p);
            return CsvUtils.collectYears(p, h);
        }
    }
}
