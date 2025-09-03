package com.dashboard.repository;

import com.dashboard.model.MonthValue;
import java.util.List;

public interface PrezziRepository {
    List<MonthValue> loadMonthSeries(String path);
    List<Integer> getAvailableYears(String path);
}
