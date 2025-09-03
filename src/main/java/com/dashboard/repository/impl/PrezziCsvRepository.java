package com.dashboard.repository.impl;

import com.dashboard.model.MonthValue;
import com.dashboard.repository.PrezziRepository;
import com.dashboard.util.CsvUtils;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.dashboard.util.CsvUtils.*;

@Repository
@Primary
public class PrezziCsvRepository implements PrezziRepository {

    private final ResourceLoader loader;

    public PrezziCsvRepository(ResourceLoader loader) {
        this.loader = loader;
    }

    /** Colonne tollerate per serie prezzi: YearMonth + valore */
    private static final List<String> COL_YM   = List.of("YM", "YEARMONTH", "DATA", "MESE", "MONTH");
    private static final List<String> COL_VAL  = List.of("VALUE", "VALORE", "PREZZO", "PRICE", "INDICE");

    @Override
    public List<MonthValue> loadMonthSeries(String path) {
        CSVParser p = CsvUtils.open(loader, path);
        Map<String,Integer> H = headerIndex(p);

        String cYm  = firstExisting(H, COL_YM);
        String cVal = firstExisting(H, COL_VAL);

        List<MonthValue> list = new ArrayList<>();
        for (CSVRecord r : p) {
            YearMonth ym = parseYearMonthFlexible(getString(r, H, cYm));
            if (ym == null) continue;
            Double v = parseDoubleSafe(getString(r, H, cVal));
            if (v == null) continue;
            list.add(new MonthValue(ym, v));
        }
        // ordina per YearMonth
        list.sort(Comparator.comparing(MonthValue::getYearMonth));
        return list;
    }

    @Override
    public List<Integer> getAvailableYears(String path) {
        CSVParser p = CsvUtils.open(loader, path);
        Map<String,Integer> H = headerIndex(p);
        String cYm = firstExisting(H, COL_YM);

        // raccogli anni dal YearMonth
        Set<Integer> yrs = new HashSet<>();
        for (CSVRecord r : p) {
            YearMonth ym = parseYearMonthFlexible(getString(r, H, cYm));
            if (ym != null) yrs.add(ym.getYear());
        }
        return yrs.stream().sorted().collect(Collectors.toList());
    }

    private static String firstExisting(Map<String,Integer> H, List<String> candidates) {
        for (String c : candidates) {
            if (H.containsKey(normalizedHeader(c))) return c;
        }
        return H.keySet().stream().findFirst().orElseThrow(
                () -> new IllegalStateException("Header mancante nel CSV prezzi"));
    }
}
