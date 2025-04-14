package pl.bykowski.cpurammetricscollectorms1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CpuRamMetricsCollectorMs1Application {

    public static void main(String[] args) {
        SpringApplication.run(CpuRamMetricsCollectorMs1Application.class, args);
    }

}
