package pl.bykowski.alertsdispatcherms4;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import pl.bykowski.RamAlert;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertConsumer {

    private final List<RamAlert> ramAlertList;

    public AlertConsumer() {
        this.ramAlertList = new ArrayList<>();
    }

    @KafkaListener(
            topics = "alerts",
            groupId = "data-alerts-dispatcher",
            containerFactory = "getKafkaListenerContainerFactory"
    )
    public void analyzeMetrics(RamAlert ramAlert) {
        ramAlertList.add(ramAlert);
    }

    public List<RamAlert> getRamAlertList() {
        return ramAlertList;
    }
}
