package com.dashboard.model;

import java.time.YearMonth;

public class PrezzoPoint {
    private final YearMonth ym;
    private final double indice;

    public PrezzoPoint(YearMonth ym, double indice) {
        this.ym = ym;
        this.indice = indice;
    }
    public YearMonth getYm() { return ym; }
    public double getIndice() { return indice; }
}
