package com.dashboard.service;

import com.dashboard.model.TableData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;

@Service
public class TavoleService {

    private final CsvTableService csv;

    public TavoleService(CsvTableService csv) {
        this.csv = csv;
    }

    /** Carica una tavola CSV generica. */
    public TableData load(String path) {
        try {
            return csv.loadTable(path);
        } catch (IOException e) {
            // unchecked = non obbliga i caller (controller) a dichiarare throws
            throw new UncheckedIOException("Errore nel leggere il CSV: " + path, e);
        }
    }
}
