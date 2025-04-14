package pl.bykowski.alertsdispatcherms4;

import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.bykowski.Alert;
import pl.bykowski.RamAlert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlertConsumer.class);

    private final List<Alert> alertList;

    public AlertConsumer() {
        this.alertList = new CopyOnWriteArrayList<>();
    }

    @KafkaListener(
            topics = "alerts",
            groupId = "alerts-dispatcher",
            concurrency = "3" // 3 konsumenci dla topiku alerts
    )
    public void consumeAlert(Alert alert) {
        logger.info("Odebrano alert: {}", alert);
        alertList.add(alert);
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

//    private final List<RamAlert> ramAlertList;
//
//    public AlertConsumer() {
//        this.ramAlertList = new ArrayList<>();
//    }
//
//    @KafkaListener(
//            topics = "alerts",
//            groupId = "data-alerts-dispatcher",
//            containerFactory = "getKafkaListenerContainerFactory"
//    )
//    public void analyzeMetrics(RamAlert ramAlert) {
//        ramAlertList.add(ramAlert);
//    }
//
//    public List<RamAlert> getRamAlertList() {
//        return ramAlertList;
//    }
}
