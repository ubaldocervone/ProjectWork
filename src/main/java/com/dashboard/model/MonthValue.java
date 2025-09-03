package com.dashboard.model;

import java.time.YearMonth;

public class MonthValue {
    private YearMonth yearMonth;
    private Double value;

    public MonthValue(YearMonth yearMonth, Double value) {
        this.yearMonth = yearMonth;
        this.value = value;
    }

    public YearMonth getYearMonth() { return yearMonth; }
    public void setYearMonth(YearMonth yearMonth) { this.yearMonth = yearMonth; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
