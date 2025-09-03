package com.dashboard.model;

import java.time.LocalDate;

public class PrezzoRecord {
    private LocalDate data;
    private Double valore;

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Double getValore() { return valore; }
    public void setValore(Double valore) { this.valore = valore; }
}
