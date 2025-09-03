package com.dashboard.repository;

import com.dashboard.model.MeteoRecord;
import java.util.List;

public interface MeteoRepository {
    List<MeteoRecord> readAll(String path);
    List<Integer> getAvailableYears(String path);
}
