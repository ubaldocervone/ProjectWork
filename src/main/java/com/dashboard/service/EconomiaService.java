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
public class EconomiaService {

    private final ResourceLoader loader;

    @Value("${data.economia.pil:}")
    private String pilPath;

    @Value("${data.economia.produttivita:}")
    private String produttivitaPath;

    @Value("${data.economia.conti:}")
    private String contiPath;

    public EconomiaService(ResourceLoader loader) {
        this.loader = loader;
    }

    // ---------- API pubbliche

    public SeriesResponse pil(Map<String,String> filtri) throws Exception {
        return estraiSerie(pilPath, "PIL", filtri);
    }

    public SeriesResponse produttivita(Map<String,String> filtri) throws Exception {
        return estraiSerie(produttivitaPath, "Produttività", filtri);
    }

    public SeriesResponse conti(Map<String,String> filtri) throws Exception {
        return estraiSerie(contiPath, "Sequenza dei conti", filtri);
    }

    // ---------- Core

    private SeriesResponse estraiSerie(String path, String titolo, Map<String,String> filtri) throws Exception {
        CSVParser p = open(path);

        // mappa header in lower-case per matching robusto
        List<String> headers = p.getHeaderNames();
        Map<String,Integer> idx = new HashMap<>();
        for (int i=0;i<headers.size();i++) idx.put(headers.get(i).toLowerCase(Locale.ITALIAN), i);

        // colonne tempo e valore (autodetect)
        String timeCol = trovaColonna(headers, "time", "time_period", "anno", "year", "periodo");
        String valueCol = trovaColonna(headers, "value", "valore", "obs_value", "dato", "indice", "var", "val");

        if (timeCol == null || valueCol == null) {
            throw new IllegalStateException("Impossibile identificare colonne tempo/valore nel file: " + path);
        }

        // eventuale unità (se presente in metadati)
        String unitCol = trovaColonna(headers, "unit", "misura", "um", "unità di misura", "misura testo");
        String unit = "-";

        // applica filtri dove possibile (solo colonne esistenti)
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

        // se definita colonna unità, prova a leggerla dal primo record
        if (unitCol != null && !records.isEmpty()) {
            Integer pos = idx.get(unitCol.toLowerCase(Locale.ITALIAN));
            if (pos != null) {
                unit = records.get(0).get(pos);
            }
        }

        // aggrego per tempo (alcuni dataset hanno più righe per stesso anno → faccio media o somma)
        boolean somma = titolo.toLowerCase(Locale.ITALIAN).contains("pil") || (unit != null && unit.toLowerCase().contains("mln"));
        Map<String, List<Double>> grouped = new TreeMap<>();
        for (CSVRecord r : records) {
            String t = r.get(timeCol).trim();
            double v = parseDoubleSafe(r.get(valueCol));
            grouped.computeIfAbsent(t, k -> new ArrayList<>()).add(v);
        }

        List<String> labels = new ArrayList<>(grouped.keySet());
        List<Double> values = labels.stream()
                .map(k -> {
                    List<Double> l = grouped.get(k);
                    if (l.isEmpty()) return 0d;
                    return somma ? l.stream().mapToDouble(Double::doubleValue).sum()
                            : l.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                }).collect(Collectors.toList());

        return new SeriesResponse(titolo, unit, labels, values);
    }

    // ---------- Utils CSV/numero

    private CSVParser open(String path) throws Exception {
        if (path == null || path.isBlank())
            throw new IllegalStateException("Path CSV vuoto.");
        Resource res = loader.getResource(path);
        if (!res.exists())
            throw new IllegalStateException("File non trovato: " + path);

        // autodetect separatore ; oppure ,
        BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));
        br.mark(8192);
        String firstLine = br.readLine();
        if (firstLine == null) throw new IllegalStateException("CSV vuoto: " + path);
        char sep = firstLine.contains(";") && !firstLine.contains(",") ? ';' : ',';
        br.reset();

        return CSVFormat.DEFAULT
                .builder()
                .setDelimiter(sep)
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(br);
    }

    private String trovaColonna(List<String> headers, String... candidati) {
        List<String> lc = headers.stream().map(h -> h.toLowerCase(Locale.ITALIAN)).collect(Collectors.toList());
        for (String c : candidati) {
            int i = lc.indexOf(c.toLowerCase(Locale.ITALIAN));
            if (i >= 0) return headers.get(i);
        }
        // fallback: se c'è una colonna che "contiene" il candidato
        for (String h : headers) {
            for (String c : candidati) {
                if (h.toLowerCase(Locale.ITALIAN).contains(c.toLowerCase(Locale.ITALIAN)))
                    return h;
            }
        }
        return null;
    }

    private double parseDoubleSafe(String s) {
        if (s == null) return 0d;
        s = s.trim();
        if (s.isEmpty()) return 0d;
        // normalizza separatore decimale
        String norm = s.replace(".", "").replace(",", ".");
        // prova veloce
        try { return Double.parseDouble(norm); } catch (Exception ignored) {}
        // fallback NumberFormat (locale IT)
        NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
        Number n = nf.parse(s, new ParsePosition(0));
        return n == null ? 0d : n.doubleValue();
    }
}
