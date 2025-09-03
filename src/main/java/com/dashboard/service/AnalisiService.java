package com.dashboard.service;

import com.dashboard.model.MeteoRecord;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

@Service
public class AnalisiService {

    /**
     * Media per mese (JAN..DEC -> media), dato un estrattore (es.: MeteoRecord::getTemperatura).
     */
    public Map<Month, Double> avgByMonth(List<MeteoRecord> daily, ToDoubleFunction<MeteoRecord> extractor) {
        Map<Month, List<MeteoRecord>> byMonth = daily.stream()
                .collect(Collectors.groupingBy(r -> r.getDate().getMonth(),
                        LinkedHashMap::new, Collectors.toList()));

        Map<Month, Double> out = new LinkedHashMap<>();
        for (Month m : Month.values()) {
            double avg = byMonth.getOrDefault(m, List.of())
                    .stream()
                    .mapToDouble(extractor)
                    .average()
                    .orElse(0.0);
            out.put(m, avg);
        }
        return out;
    }
}
