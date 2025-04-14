package pl.bykowski.cpurammetricscollectorms1;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import pl.bykowski.CpuMetrics;
import pl.bykowski.RamMetrics;
import pl.bykowski.SystemMetrics;
import pl.bykowski.RamAlert;

@RestController
public class SystemMetricsController {

    private final SystemInfo systemInfo = new SystemInfo();
    private final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private final CentralProcessor processor = hardware.getProcessor();
    private final GlobalMemory memory = hardware.getMemory();

    private long[] prevTicks = processor.getSystemCpuLoadTicks();

    private final KafkaTemplate<String, SystemMetrics> kafkaTemplate;
    private final KafkaTemplate<String, CpuMetrics> cpuKafkaTemplate;
    private final KafkaTemplate<String, RamMetrics> ramKafkaTemplate;

    public SystemMetricsController(KafkaTemplate<String, SystemMetrics> kafkaTemplate, KafkaTemplate<String, CpuMetrics> cpuKafkaTemplate, KafkaTemplate<String, RamMetrics> ramKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.cpuKafkaTemplate = cpuKafkaTemplate;
        this.ramKafkaTemplate = ramKafkaTemplate;
    }

//    @Scheduled(fixedRate = 10000) // co 10 sekund
    public String getSystemMetrics() {
        long[] newTicks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = newTicks;

        long[] frequencies = processor.getCurrentFreq();
        double avgFreq = frequencies.length > 0 ?
                java.util.Arrays.stream(frequencies).sum() / frequencies.length / 1_000_000_000.0 : 0.0;

        long totalMemory = memory.getTotal();
        long usedMemory = totalMemory - memory.getAvailable();
        double ramUsagePercentage = ((double) usedMemory / totalMemory) * 100;

        SystemMetrics metrics = new SystemMetrics(
                avgFreq,                                    // Moc procesora (GHz)
                Math.round(cpuLoad * 100.0) / 100.0,       // Zużycie CPU (%)
                Math.round((totalMemory / (1024.0 * 1024 * 1024)) * 100.0) / 100.0, // Całkowity RAM (GB)
                Math.round(ramUsagePercentage * 100.0) / 100.0  // Zużycie RAM (%)
        );

        kafkaTemplate.send("system-metrics", metrics);

        return "Metrics: " + metrics ;
    }

    @Scheduled(fixedRate = 10000) // co 10 sekund
    public String getSeparateSystemMetrics() {
        // CPU Metrics
        long[] newTicks = processor.getSystemCpuLoadTicks();
        double cpuLoad = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = newTicks;

        long[] frequencies = processor.getCurrentFreq();
        double avgFreq = frequencies.length > 0 ?
                java.util.Arrays.stream(frequencies).sum() / frequencies.length / 1_000_000_000.0 : 0.0;

        CpuMetrics cpuMetrics = new CpuMetrics(
                Math.round(avgFreq * 100.0) / 100.0,       // Moc procesora (GHz)
                Math.round(cpuLoad * 100.0) / 100.0        // Zużycie CPU (%)
        );

        cpuKafkaTemplate.send("cpu-metrics", cpuMetrics);

        // RAM Metrics
        long totalMemory = memory.getTotal();
        long usedMemory = totalMemory - memory.getAvailable();
        double ramUsagePercentage = ((double) usedMemory / totalMemory) * 100;

        RamMetrics ramMetrics = new RamMetrics(
                Math.round((totalMemory / (1024.0 * 1024 * 1024)) * 100.0) / 100.0, // Całkowity RAM (GB)
                Math.round(ramUsagePercentage * 100.0) / 100.0  // Zużycie RAM (%)
        );

        ramKafkaTemplate.send("ram-metrics", ramMetrics);

        return "CPU Metrics: " + cpuMetrics + ", RAM Metrics: " + ramMetrics;
    }

}
