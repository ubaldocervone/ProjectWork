/*package com.dashboard.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;

@Service
public class ExcelService {

    private final ResourceLoader loader;

    @Value("${data.excel.andamento_agricoltura_2025:}")
    private String andamentoPath;

    @Value("${data.excel.unita_agricole_2020:}")
    private String unitaPath;

    public ExcelService(ResourceLoader loader) { this.loader = loader; }

    public List<Map<String, String>> readFirstSheet(String which) throws Exception {
        String path = switch (which) {
            case "andamento" -> andamentoPath;
            case "unita" -> unitaPath;
            default -> throw new IllegalArgumentException("Sorgente non valida: " + which);
        };
        Resource res = loader.getResource(path);
        if (!res.exists()) throw new IllegalStateException("File Excel non trovato: " + path);

        try (InputStream in = res.getInputStream()) {
            Workbook wb = path.toLowerCase().endsWith(".xlsx") ? new XSSFWorkbook(in) : new HSSFWorkbook(in);
            Sheet sheet = wb.getSheetAt(0);

            // intestazioni dalla prima riga
            Iterator<Row> it = sheet.iterator();
            if (!it.hasNext()) return List.of();
            Row headRow = it.next();
            List<String> headers = new ArrayList<>();
            headRow.forEach(c -> headers.add(getCellString(c)));

            List<Map<String, String>> out = new ArrayList<>();
            while (it.hasNext()) {
                Row r = it.next();
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.size(); i++) {
                    Cell c = r.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    row.put(headers.get(i), c == null ? "" : getCellString(c));
                }
                out.add(row);
            }
            return out;
        }
    }

    private static String getCellString(Cell c) {
        if (c == null) return "";
        return switch (c.getCellType()) {
            case STRING -> c.getStringCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(c)
                    ? c.getLocalDateTimeCellValue().toLocalDate().toString()
                    : String.valueOf(c.getNumericCellValue());
            case BOOLEAN -> String.valueOf(c.getBooleanCellValue());
            case FORMULA -> c.getCellFormula();
            default -> "";
        };
    }
}
*/