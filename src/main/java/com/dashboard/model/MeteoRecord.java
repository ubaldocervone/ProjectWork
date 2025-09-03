package com.dashboard.model;

import java.time.LocalDate;

public class MeteoRecord {
    private LocalDate date;
    private Double temperature;   // °C
    private Double humidity;      // %
    private Double precipitation; // mm

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public Double getPrecipitation() { return precipitation; }
    public void setPrecipitation(Double precipitation) { this.precipitation = precipitation; }
}
