package pl.bykowski.dataaggregatoranalyzerms3;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.bykowski.RamAlert;
import pl.bykowski.SystemMetrics;

@Service
public class MetricsAnalyzerService {

    private final KafkaTemplate<String, RamAlert> kafkaTemplate;

    public MetricsAnalyzerService(KafkaTemplate<String, RamAlert> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(
            topics = "system-metrics",
            groupId = "data-aggregator-analyzer"
    )
    public void analyzeMetrics(SystemMetrics metrics) {

        double ramUsagePercentage = metrics.getRamUsagePercentage();

        if (ramUsagePercentage >= 80) {
            RamAlert ramAlert = new RamAlert("CRITICAL", ramUsagePercentage, metrics.getTime());
            kafkaTemplate.send("alerts", ramAlert);
            System.out.println("⛔" + ramAlert);
        } else if (ramUsagePercentage >= 50) {
            RamAlert ramAlert = new RamAlert("WARNING", ramUsagePercentage, metrics.getTime());
            kafkaTemplate.send("alerts", ramAlert);
            System.out.println("⚠️" + ramAlert);
        }

    }


}

