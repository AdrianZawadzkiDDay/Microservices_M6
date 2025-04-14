package pl.bykowski.alertsdispatcherms4;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.bykowski.RamAlert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@Controller
public class AlertControllerMock {

    private static final double MIN_USAGE = 50.0;
    private static final double MAX_USAGE = 97.0;
    private static final double WARNING_THRESHOLD = 50.0;
    private static final double CRITICAL_THRESHOLD = 80.0;
    private static final double MAX_VARIATION = 5.0;

//    @GetMapping("/get-alerts-mock")
    public ResponseEntity<List<RamAlert>> getAlertsMock(@RequestParam(required = false) String level) {
        List<RamAlert> ramAlertList = generateAlerts();

        if (level != null && !level.isEmpty()) {
            ramAlertList = ramAlertList
                    .stream()
                    .filter(alert -> alert.getMessage().equalsIgnoreCase(level))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(ramAlertList);
    }

    private List<RamAlert> generateAlerts() {
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 17, 1, 2);
        Random random = new Random();
        AtomicReference<Double> lastUsage = new AtomicReference<>(WARNING_THRESHOLD);

        return IntStream.range(0, 100)
                .mapToObj(i -> {
                    boolean isCritical = (i % 2 != 0);
                    double minBound = isCritical ? CRITICAL_THRESHOLD : WARNING_THRESHOLD;
                    double newUsage = Math.min(
                            MAX_USAGE,
                            Math.max(minBound, lastUsage.get() + (random.nextDouble() * 2 * MAX_VARIATION - MAX_VARIATION))
                    );
                    lastUsage.set(newUsage);
                    return new RamAlert(
                            isCritical ? "CRITICAL" : "WARNING",
                            newUsage,
                            startTime.plusSeconds(i * 10)
                    );
                })
                .collect(Collectors.toList());
    }

}
