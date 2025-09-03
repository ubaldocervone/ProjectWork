package com.dashboard.service;

import com.dashboard.model.SeriesResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PescaService {

    private final ResourceLoader loader;

    @Value("${data.pesca.servizi:}")
    private String serviziPath;

    @Value("${data.pesca.dati:}")
    private String datiPath;

    @Value("${data.pesca.porti:}")
    private String portiPath;

    public PescaService(ResourceLoader loader) {
        this.loader = loader;
    }

    public SeriesResponse servizi(Map<String,String> filtri) throws Exception {
        return estraiSerie(serviziPath, "Servizi settore pesca", filtri);
    }

    public SeriesResponse dati(Map<String,String> filtri) throws Exception {
        return estraiSerie(datiPath, "Dati pesca", filtri);
    }

    public SeriesResponse porti(Map<String,String> filtri) throws Exception {
        return estraiSerie(portiPath, "Porti e approdi pesca", filtri);
    }

    // ---- core molto simile a EconomiaService (riuso logica)

    private SeriesResponse estraiSerie(String path, String titolo, Map<String,String> filtri) throws Exception {
        CSVParser p = open(path);

        List<String> headers = p.getHeaderNames();
        Map<String,Integer> idx = new HashMap<>();
        for (int i=0;i<headers.size();i++) idx.put(headers.get(i).toLowerCase(Locale.ITALIAN), i);

        String timeCol = trovaColonna(headers, "time", "time_period", "anno", "year", "periodo");
        String valueCol = trovaColonna(headers, "value", "valore", "obs_value", "quantita", "numero", "totale");

        if (timeCol == null || valueCol == null) {
            throw new IllegalStateException("Impossibile identificare colonne tempo/valore nel file: " + path);
        }

        String unitCol = trovaColonna(headers, "unit", "misura", "um", "unità di misura");
        String unit = "-";

        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord r : p) {
            boolean ok = true;
            for (var e : filtri.entrySet()) {
                String col = e.getKey();
                String val = e.getValue();
                Integer pos = idx.get(col.toLowerCase(Locale.ITALIAN));
                if (pos != null) {
                    String cell = r.get(pos).trim();
                    if (!cell.equalsIgnoreCase(val)) { ok = false; break; }
                }
            }
            if (ok) records.add(r);
        }

        if (unitCol != null && !records.isEmpty()) {
            Integer pos = idx.get(unitCol.toLowerCase(Locale.ITALIAN));
            if (pos != null) unit = records.get(0).get(pos);
        }

        Map<String, List<Double>> grouped = new TreeMap<>();
        for (CSVRecord r : records) {
            String t = r.get(timeCol).trim();
            double v = parseDoubleSafe(r.get(valueCol));
            grouped.computeIfAbsent(t, k -> new ArrayList<>()).add(v);
        }

        List<String> labels = new ArrayList<>(grouped.keySet());
        List<Double> values = labels.stream()
                .map(k -> grouped.get(k).stream().mapToDouble(Double::doubleValue).sum())
                .collect(Collectors.toList());

        return new SeriesResponse(titulo(titolo, filtri), unit, labels, values);
    }

    private String titulo(String titolo, Map<String,String> filtri){
        if (filtri == null || filtri.isEmpty()) return titolo;
        String det = filtri.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
        return titolo + " — " + det;
    }

    private CSVParser open(String path) throws Exception {
        if (path == null || path.isBlank())
            throw new IllegalStateException("Path CSV vuoto.");
        Resource res = loader.getResource(path);
        if (!res.exists())
            throw new IllegalStateException("File non trovato: " + path);
        BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));
        br.mark(8192);
        String first = br.readLine();
        if (first == null) throw new IllegalStateException("CSV vuoto: " + path);
        char sep = first.contains(";") && !first.contains(",") ? ';' : ',';
        br.reset();
        return CSVFormat.DEFAULT.builder().setDelimiter(sep).setHeader().setSkipHeaderRecord(true).build().parse(br);
    }

    private String trovaColonna(List<String> headers, String... candidati) {
        List<String> lc = headers.stream().map(h -> h.toLowerCase(Locale.ITALIAN)).collect(Collectors.toList());
        for (String c : candidati) {
            int i = lc.indexOf(c.toLowerCase(Locale.ITALIAN));
            if (i >= 0) return headers.get(i);
        }
        for (String h : headers)
            for (String c : candidati)
                if (h.toLowerCase(Locale.ITALIAN).contains(c.toLowerCase(Locale.ITALIAN))) return h;
        return null;
    }

    private double parseDoubleSafe(String s) {
        if (s == null) return 0d;
        s = s.trim();
        if (s.isEmpty()) return 0d;
        String norm = s.replace(".", "").replace(",", ".");
        try { return Double.parseDouble(norm); } catch (Exception ignored) {}
        NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
        Number n = nf.parse(s, new ParsePosition(0));
        return n == null ? 0d : n.doubleValue();
    }
}
