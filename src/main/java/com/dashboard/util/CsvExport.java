package com.dashboard.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Utility minimale per esportare CSV se serve nel simulatore. */
public final class CsvExport {

    private CsvExport() {}

    public static void writeCsv(Path file, List<String> headers, List<List<String>> rows) {
        try {
            Files.createDirectories(file.getParent());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.toFile()))) {
                if (headers != null && !headers.isEmpty()) {
                    bw.write(String.join(",", headers));
                    bw.newLine();
                }
                for (List<String> r : rows) {
                    bw.write(String.join(",", r));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore scrittura CSV: " + e.getMessage(), e);
        }
    }
}
