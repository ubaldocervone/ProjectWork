
package com.dashboard.service;

import com.dashboard.model.TableData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Utility service per caricare un CSV in TableData. */
@Service
public class CsvTableService {

    /**
     * Carica una tabella da CSV. Tenta prima dal filesystem (path relativo o assoluto),
     * in fallback dal classpath (es. src/main/resources/...).
     */
    public TableData loadTable(String path) throws IOException {
        Charset cs = StandardCharsets.UTF_8;

        // 1) Provo come file sul filesystem
        Path p = Paths.get(path);
        if (Files.exists(p)) {
            try (BufferedReader br = Files.newBufferedReader(p, cs)) {
                char delimiter = sniffDelimiter(br);
                return parseAsTable(Files.newBufferedReader(p, cs), delimiter);
            }
        }

        // 2) Fallback: risorsa nel classpath
        ClassPathResource res = new ClassPathResource(path);
        if (res.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), cs))) {
                char delimiter = sniffDelimiter(br);
                return parseAsTable(new BufferedReader(new InputStreamReader(res.getInputStream(), cs)), delimiter);
            }
        }

        throw new IOException("File CSV non trovato: " + path);
    }

    /** Semplice sniff del delimitatore: se nella prima riga ci sono più ';' dei ',' uso ';', altrimenti ','. */
    private char sniffDelimiter(BufferedReader br) throws IOException {
        br.mark(8192);
        String first = br.readLine();
        br.reset();
        if (first == null) return ',';
        long sc = first.chars().filter(c -> c == ';').count();
        long cc = first.chars().filter(c -> c == ',').count();
        return (sc > cc) ? ';' : ',';
    }

    private TableData parseAsTable(Reader reader, char delimiter) throws IOException {
        CSVFormat fmt = CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setTrim(true)
                .setIgnoreSurroundingSpaces(true)
                // leggi la prima riga come header e NON restituirla come record
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try (CSVParser parser = new CSVParser(reader, fmt)) {
            // header da parser
            headers = parser.getHeaderNames();

            for (CSVRecord r : parser) {
                List<String> row = new ArrayList<>(r.size());
                r.forEach(cell -> row.add(cell == null ? "" : cell));
                rows.add(row);
            }
        }

        return new TableData(headers, rows);
    }
}
