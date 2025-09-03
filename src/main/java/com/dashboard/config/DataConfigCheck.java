
package com.dashboard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import jakarta.annotation.PostConstruct;

@Configuration
public class DataConfigCheck {

    private final ResourceLoader loader;

    @Value("${data.meteo:}")
    private String meteoPath;

    @Value("${data.prezzi.tab01:}")
    private String prezziPath;

    public DataConfigCheck(ResourceLoader loader) { this.loader = loader; }

    @PostConstruct
    public void check() {
        checkResource("data.meteo", meteoPath);
        checkResource("data.prezzi.tab01", prezziPath);
    }

    private void checkResource(String key, String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalStateException("Property '" + key + "' non impostata.");
        }
        Resource r = loader.getResource(path);
        if (!r.exists()) {
            throw new IllegalStateException("File non trovato per '" + key + "': " + path);
        }
        System.out.println("[DATA] " + key + " -> OK (" + path + ")");
    }
}
