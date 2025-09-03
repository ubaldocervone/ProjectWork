package com.dashboard.service;

import com.dashboard.model.TableData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class UnitaAgricoleService {

    private final CsvTableService csv;

    public UnitaAgricoleService(CsvTableService csv) {
        this.csv = csv;
    }

    public TableData load(String path) {
        try {
            return csv.loadTable(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Errore nel leggere il CSV: " + path, e);
        }
    }
}
