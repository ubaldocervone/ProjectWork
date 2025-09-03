package com.dashboard.repository.impl;

import com.dashboard.model.MeteoRecord;
import com.dashboard.repository.MeteoRepository;
import com.dashboard.util.CsvUtils;
import com.dashboard.util.CsvUtils.H;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.Reader;
import java.util.*;

@Repository
@Primary
public class MeteoRepositoryCsv implements MeteoRepository {

    private final ResourceLoader loader;

    public MeteoRepositoryCsv(ResourceLoader loader) {
        this.loader = loader;
    }

    @Override
    public List<Integer> getAvailableYears(String path) {
        Set<Integer> out = new TreeSet<>();
        try (Reader r = CsvUtils.open(path, loader);
             CSVParser p = CsvUtils.parser(r)) {

            Map<String, Integer> h = CsvUtils.headerIndex(p);
            for (CSVRecord rec : p) {
                Integer y = CsvUtils.parseIntSafe(CsvUtils.get(rec, h, H.ANNO, H.YEAR));
                if (y != null) out.add(y);
            }
        } catch (Exception ignored) {}
        return new ArrayList<>(out);
    }

    @Override
    public List<MeteoRecord> readAll(String path) {
        // Se ti serve il mapping completo su MeteoRecord, dimmelo e lo aggiungo
        // in base ai tuoi setter/constructor esatti.
        return Collections.emptyList();
    }
}
