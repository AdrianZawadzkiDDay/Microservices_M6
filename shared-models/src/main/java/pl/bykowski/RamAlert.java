package pl.bykowski;

import java.time.LocalDateTime;

public class RamAlert {

    private String message;
    private double ramUsage;
    private LocalDateTime time;           // Czas pobrania metryki

    public RamAlert() {
    }

    public RamAlert(String message, double ramUsage, LocalDateTime time) {
        this.message = message;
        this.ramUsage = ramUsage;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(double ramUsage) {
        this.ramUsage = ramUsage;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RamAlert{" +
                "message='" + message + '\'' +
                ", ramUsage=" + ramUsage +
                ", time=" + time +
                '}';
    }
}
