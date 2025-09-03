package com.dashboard.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public final class CsvUtils {

    private CsvUtils() {}

    /* ------------------------ IO ------------------------ */

    /** Apertura sicura del file CSV contenuto nello classpath o nel filesystem. */
    public static Reader open(String path, ResourceLoader loader) throws IOException {
        Resource res = loader.getResource(path);
        if (!res.exists()) {
            throw new IOException("CSV not found: " + path);
        }
        return new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));
    }

    /** Parser configurato: prima riga = header, trim, case-insensitive. */
    public static CSVParser parser(Reader r) throws IOException {
        CSVFormat fmt = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim();
        return new CSVParser(r, fmt);
    }

    /* ------------------------ Header / Accesso ------------------------ */

    /** Mappa nomeColonna(normalizzato) -> indice. */
    public static Map<String, Integer> headerIndex(CSVParser p) {
        Map<String, Integer> h = new HashMap<>();
        Map<String, Integer> raw = p.getHeaderMap(); // può essere LinkedHashMap
        for (Map.Entry<String, Integer> e : raw.entrySet()) {
            h.put(normalize(e.getKey()), e.getValue());
        }
        return h;
    }

    /** Normalizza un nome colonna (minuscole, senza spazi). */
    public static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ITALY).replace(" ", "");
    }

    /** Legge una cella come stringa (o null se manca). */
    public static String get(CSVRecord rec, Map<String, Integer> h, String col) {
        if (rec == null || h == null || col == null) return null;
        Integer idx = h.get(normalize(col));
        if (idx == null) return null;
        if (idx < 0 || idx >= rec.size()) return null;
        String v = rec.get(idx);
        return v != null ? v.trim() : null;
    }

    public static String getString(CSVRecord rec, Map<String, Integer> h, String col) {
        return get(rec, h, col);
    }

    public static Integer parseIntSafe(String s) {
        try { return s == null || s.isEmpty() ? null : Integer.valueOf(s.replaceAll("\\D+","")); }
        catch (Exception ignore) { return null; }
    }

    public static Double parseDoubleSafe(String s) {
        try {
            if (s == null || s.isEmpty()) return null;
            // accetta anche "1,23" e "1 234,56"
            String norm = s.replace(" ", "").replace("%","").replace(",", ".");
            return Double.valueOf(norm);
        } catch (Exception ignore) { return null; }
    }

    /* ------------------------ Date parsing ------------------------ */

    /** LocalDate con formati comuni: yyyy-MM-dd, dd/MM/yyyy, yyyy/MM/dd, dd-MM-yyyy. */
    public static LocalDate parseDateFlexible(String s) {
        if (s == null || s.isEmpty()) return null;
        List<DateTimeFormatter> fmts = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("dd/MM/uuuu"),
                DateTimeFormatter.ofPattern("uuuu/MM/dd"),
                DateTimeFormatter.ofPattern("dd-MM-uuuu")
        );
        for (DateTimeFormatter f : fmts) {
            try { return LocalDate.parse(s.trim(), f); }
            catch (DateTimeParseException ignore) {}
        }
        return null;
    }

    /** YearMonth con formati: yyyy-MM, MM/yyyy, yyyy/MM, yyyyMM, M/yyyy, MM-uuuu. */
    public static YearMonth parseYearMonthFlexible(String s) {
        if (s == null || s.isEmpty()) return null;
        List<DateTimeFormatter> fmts = Arrays.asList(
                DateTimeFormatter.ofPattern("uuuu-MM"),
                DateTimeFormatter.ofPattern("MM/uuuu"),
                DateTimeFormatter.ofPattern("uuuu/MM"),
                DateTimeFormatter.ofPattern("uuuuMM"),
                DateTimeFormatter.ofPattern("M/uuuu"),
                DateTimeFormatter.ofPattern("MM-uuuu")
        );
        for (DateTimeFormatter f : fmts) {
            try { return YearMonth.parse(s.trim(), f); }
            catch (DateTimeParseException ignore) {}
        }
        return null;
    }

    /* ------------------------ Utility dominio ------------------------ */

    /**
     * Estrae tutti gli anni disponibili dal CSV. Prova colonne comuni:
     * YEAR/Anno, DATE/Data, YM/YearMonth.
     */
    public static List<Integer> collectYears(CSVParser p, Map<String, Integer> h) {
        Set<Integer> out = new HashSet<>();
        String yearCol = guessFirst(h, "year", "anno");
        String dateCol = guessFirst(h, "date", "data");
        String ymCol   = guessFirst(h, "yearmonth", "ym", "meseanno", "annomese");

        for (CSVRecord rec : p) {
            Integer y = null;
            if (yearCol != null) {
                y = parseIntSafe(get(rec, h, yearCol));
            }
            if (y == null && dateCol != null) {
                LocalDate d = parseDateFlexible(get(rec, h, dateCol));
                if (d != null) y = d.getYear();
            }
            if (y == null && ymCol != null) {
                YearMonth ym = parseYearMonthFlexible(get(rec, h, ymCol));
                if (ym != null) y = ym.getYear();
            }
            if (y != null) out.add(y);
        }
        return out.stream().sorted().collect(Collectors.toList());
    }

    /** Trova la prima chiave in header che “assomiglia” a uno dei candidati. */
    public static String guessFirst(Map<String,Integer> h, String... candidates) {
        if (h == null || h.isEmpty()) return null;
        Set<String> keys = h.keySet();
        for (String c : candidates) {
            String n = normalize(c);
            for (String k : keys) {
                if (k.equals(n) || k.contains(n)) return k;
            }
        }
        return null;
    }

    /* ------------------------ Alias nomi colonne più comuni ------------------------ */
    public static final class H {
        public static final String DATE   = "date";      // o "data"
        public static final String YM     = "yearmonth"; // o "ym", "AnnoMese", "meseanno"
        public static final String YEAR   = "year";      // o "anno"
        public static final String MONTH  = "month";     // o "mese"
        public static final String VALUE  = "value";     // prezzi / indice
        public static final String TEMP   = "temp";      // temperatura
        public static final String HUM    = "hum";       // umidità
        public static final String PRECIP = "precip";    // precipitazioni / rain
        private H() {}
    }
}
