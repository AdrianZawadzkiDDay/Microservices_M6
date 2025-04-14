package com.example.alert_microservice_application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.bykowski.Alert;
import pl.bykowski.CpuMetrics;
import pl.bykowski.RamMetrics;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MetricsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MetricsConsumer.class);

    private final KafkaTemplate<String, Alert> alertKafkaTemplate;

    // Progi dla alertów
    private static final double CPU_USAGE_THRESHOLD = 50.0;
    private static final double RAM_USAGE_THRESHOLD = 60.0;
    private static final long WINDOW_SECONDS = 300; // 5 minut
    private final ConcurrentHashMap<String, List<CpuMetrics>> cpuMetricsStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<RamMetrics>> ramMetricsStore = new ConcurrentHashMap<>();


    public MetricsConsumer(KafkaTemplate<String, Alert> alertKafkaTemplate) {
        this.alertKafkaTemplate = alertKafkaTemplate;
        cpuMetricsStore.put("metrics", new ArrayList<>());
        ramMetricsStore.put("metrics", new ArrayList<>());
    }

    @KafkaListener(
            topics = "cpu-metrics",
            groupId = "metrics-group",
            concurrency = "3" // 3 konsumenci dla topiku CPU
    )
    public void consumeCpuMetrics(CpuMetrics cpuMetrics) {
        logger.info("Odebrano CPU Metrics: {}", cpuMetrics);

        // Sprawdzanie progu zużycia CPU
        if (cpuMetrics.getCpuUsagePercentage() > CPU_USAGE_THRESHOLD) {
            Alert alert = new Alert(
                    "CPU",
                    String.format("Wysokie zużycie CPU: %.2f%%", cpuMetrics.getCpuUsagePercentage()),
                    cpuMetrics.getCpuUsagePercentage()
            );
            alertKafkaTemplate.send("alerts", alert);
            logger.warn("Wysłano alert: {}", alert);
        }
    }

    @KafkaListener(
            topics = "ram-metrics",
            groupId = "metrics-group",
            concurrency = "3" // 3 konsumenci dla topiku RAM
    )
    public void consumeRamMetrics(RamMetrics ramMetrics) {
        logger.info("Odebrano RAM Metrics: {}", ramMetrics);

        // Sprawdzanie progu zużycia RAM
        if (ramMetrics.getRamUsagePercentage() > RAM_USAGE_THRESHOLD) {
            Alert alert = new Alert(
                    "RAM",
                    String.format("Wysokie zużycie RAM: %.2f%%", ramMetrics.getRamUsagePercentage()),
                    ramMetrics.getRamUsagePercentage()
            );
            alertKafkaTemplate.send("alerts", alert);
            logger.warn("Wysłano alert: {}", alert);
        }
    }

    @Scheduled(fixedRate = 60000) // Co minutę
    public void aggregateMetrics() {
        LocalDateTime now = LocalDateTime.now();
        synchronized (cpuMetricsStore) {
            List<CpuMetrics> cpuMetrics = cpuMetricsStore.get("metrics");
            cpuMetrics.removeIf(metric -> metric.getTime().isBefore(now.minus(WINDOW_SECONDS, ChronoUnit.SECONDS)));

            if (!cpuMetrics.isEmpty()) {
                double avgCpuUsage = cpuMetrics.stream()
                        .mapToDouble(CpuMetrics::getCpuUsagePercentage)
                        .average()
                        .orElse(0.0);

                if (avgCpuUsage > CPU_USAGE_THRESHOLD) {
                    Alert alert = new Alert(
                            "CPU",
                            String.format("Średnie zużycie CPU w oknie 5 minut: %.2f%%", avgCpuUsage),
                            avgCpuUsage
                    );
                    alertKafkaTemplate.send("alerts", alert);
                    logger.warn("Wysłano alert agregacji CPU: {}", alert);
                }
            }
        }

        synchronized (ramMetricsStore) {
            List<RamMetrics> ramMetrics = ramMetricsStore.get("metrics");
            ramMetrics.removeIf(metric -> metric.getTime().isBefore(now.minus(WINDOW_SECONDS, ChronoUnit.SECONDS)));

            if (!ramMetrics.isEmpty()) {
                double avgRamUsage = ramMetrics.stream()
                        .mapToDouble(RamMetrics::getRamUsagePercentage)
                        .average()
                        .orElse(0.0);

                if (avgRamUsage > RAM_USAGE_THRESHOLD) {
                    Alert alert = new Alert(
                            "RAM",
                            String.format("Średnie zużycie RAM w oknie 5 minut: %.2f%%", avgRamUsage),
                            avgRamUsage
                    );
                    alertKafkaTemplate.send("alerts", alert);
                    logger.warn("Wysłano alert agregacji RAM: {}", alert);
                }
            }
        }
    }
}
