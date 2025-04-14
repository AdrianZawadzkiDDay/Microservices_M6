package pl.bykowski;

import java.time.LocalDateTime;

public class SystemMetrics {
    private double cpuPower;              // Moc procesora (GHz)
    private double cpuUsagePercentage;    // Aktualne zużycie CPU (%)
    private double totalRam;              // Całkowita ilość RAM (GB)
    private double ramUsagePercentage;    // Aktualne zużycie RAM (%)
    private LocalDateTime time;           // Czas pobrania metryki

    public SystemMetrics() {
    }

    public SystemMetrics(double cpuPower, double cpuUsagePercentage, double totalRam, double ramUsagePercentage) {
        this.cpuPower = cpuPower;
        this.cpuUsagePercentage = cpuUsagePercentage;
        this.totalRam = totalRam;
        this.ramUsagePercentage = ramUsagePercentage;
        this.time = LocalDateTime.now();
    }

    public double getCpuPower() {
        return cpuPower;
    }

    public void setCpuPower(double cpuPower) {
        this.cpuPower = cpuPower;
    }

    public double getCpuUsagePercentage() {
        return cpuUsagePercentage;
    }

    public void setCpuUsagePercentage(double cpuUsagePercentage) {
        this.cpuUsagePercentage = cpuUsagePercentage;
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
        return "SystemMetrics{" +
                "cpuPower=" + cpuPower +
                ", cpuUsagePercentage=" + cpuUsagePercentage +
                ", totalRam=" + totalRam +
                ", ramUsagePercentage=" + ramUsagePercentage +
                ", time=" + time +
                '}';
    }
}
