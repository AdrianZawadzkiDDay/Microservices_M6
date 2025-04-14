package pl.bykowski;

import java.time.LocalDateTime;

public class Alert {
    private String type; // np. "CPU", "RAM"
    private String message; // opis alertu
    private double value; // wartość powodująca alert (np. procent zużycia)
    private LocalDateTime timestamp;

    public Alert() {
    }

    public Alert(String type, String message, double value) {
        this.type = type;
        this.message = message;
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }

    // Getters i Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Alert{type='" + type + "', message='" + message + "', value=" + value + ", timestamp=" + timestamp + "}";
    }
}