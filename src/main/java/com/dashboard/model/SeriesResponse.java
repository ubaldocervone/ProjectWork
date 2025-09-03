package com.dashboard.model;

import java.util.List;

public class SeriesResponse {
    private String title;
    private String unit;
    private List<String> labels;
    private List<Double> values;

    public SeriesResponse() {}

    public SeriesResponse(String title, String unit, List<String> labels, List<Double> values) {
        this.title = title;
        this.unit = unit;
        this.labels = labels;
        this.values = values;
    }

    public String getTitle() { return title; }
    public String getUnit() { return unit; }
    public List<String> getLabels() { return labels; }
    public List<Double> getValues() { return values; }
    public void setTitle(String title) { this.title = title; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public void setValues(List<Double> values) { this.values = values; }
}
