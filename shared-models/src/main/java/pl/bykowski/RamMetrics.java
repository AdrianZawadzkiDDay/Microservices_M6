package pl.bykowski;

import java.time.LocalDateTime;

public class RamMetrics {
    private double totalRam;              // Całkowita ilość RAM (GB)
    private double ramUsagePercentage;    // Aktualne zużycie RAM (%)
    private LocalDateTime time;           // Czas pobrania metryki

    public RamMetrics() {
    }

    public RamMetrics(double totalRam, double ramUsagePercentage) {
        this.totalRam = totalRam;
        this.ramUsagePercentage = ramUsagePercentage;
        this.time = LocalDateTime.now();
    }

    public double getTotalRam() {
        return totalRam;
    }

    public void setTotalRam(double totalRam) {
        this.totalRam = totalRam;
    }

    public double getRamUsagePercentage() {
        return ramUsagePercentage;
    }

    public void setRamUsagePercentage(double ramUsagePercentage) {
        this.ramUsagePercentage = ramUsagePercentage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RamMetrics{" +
                "totalRam=" + totalRam +
                ", ramUsagePercentage=" + ramUsagePercentage +
                ", time=" + time +
                '}';
    }
}
