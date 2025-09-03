package com.dashboard.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class OpenBrowserOnReady {

    private static final Logger log = LoggerFactory.getLogger(OpenBrowserOnReady.class);

    private int port;

    // Questo listener si attiva quando il WebServer è pronto e ci dice la porta effettiva
    @EventListener
    public void onWebServerReady(WebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
        String url = "http://localhost:" + port + "/dashboard";

        log.info("✅ Server avviato su {}", url);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                log.info("🌐 Browser aperto automaticamente su {}", url);
            } else {
                log.warn("⚠️ Browser non supportato in questo ambiente. Apri manualmente: {}", url);
            }
        } catch (Exception e) {
            log.error("❌ Impossibile aprire il browser. URL: {}", url, e);
        }
    }
}
