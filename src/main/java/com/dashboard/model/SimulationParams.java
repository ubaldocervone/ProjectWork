package com.dashboard.model;

public class SimulationParams {

    private double temperaturaMedia;
    private double umiditaMedia;
    private double precipitazioni;   // mm
    private double radiazioneSolare; // W/m2 o kWh/m2
    private double pressioneMedia;   // hPa

    public SimulationParams() { }

    public SimulationParams(double temperaturaMedia, double umiditaMedia,
                            double precipitazioni, double radiazioneSolare,
                            double pressioneMedia) {
        this.temperaturaMedia = temperaturaMedia;
        this.umiditaMedia = umiditaMedia;
        this.precipitazioni = precipitazioni;
        this.radiazioneSolare = radiazioneSolare;
        this.pressionemedia = pressioneMedia; // compatibilità ma non usata
    }

    public double getTemperaturaMedia() { return temperaturaMedia; }
    public void setTemperaturaMedia(double temperaturaMedia) { this.temperaturaMedia = temperaturaMedia; }

    public double getUmiditaMedia() { return umiditaMedia; }
    public void setUmiditaMedia(double umiditaMedia) { this.umiditaMedia = umiditaMedia; }

    public double getPrecipitazioni() { return precipitazioni; }
    public void setPrecipitazioni(double precipitazioni) { this.precipitazioni = precipitazioni; }

    public double getRadiazioneSolare() { return radiazioneSolare; }
    public void setRadiazioneSolare(double radiazioneSolare) { this.radiazioneSolare = radiazioneSolare; }

    public double getPressioneMedia() { return pressioneMedia; }
    public void setPressioneMedia(double pressioneMedia) { this.pressioneMedia = pressioneMedia; }

    // Campo privato per compatibilità (non esposto)
    private double pressionemedia; // evita errori se qualche SpEL lo cerca col nome minuscolo
}
