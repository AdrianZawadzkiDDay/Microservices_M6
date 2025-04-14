package pl.bykowski.alertsdispatcherms4;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.bykowski.Alert;
import pl.bykowski.RamAlert;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AlertController {
    private final AlertConsumer alertConsumer;

    public AlertController(AlertConsumer alertConsumer) {
        this.alertConsumer = alertConsumer;
    }

    @GetMapping("/get-alerts")
    public ResponseEntity<List<Alert>> getAlerts(@RequestParam(required = false) String type) {
        List<Alert> alertList = alertConsumer.getAlertList();

        // Filtrowanie po typie (CPU, RAM), jeśli podano parametr
        if (type != null && !type.isEmpty()) {
            alertList = alertList.stream()
                    .filter(alert -> alert.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(alertList);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("HEALTH");
    }
}
