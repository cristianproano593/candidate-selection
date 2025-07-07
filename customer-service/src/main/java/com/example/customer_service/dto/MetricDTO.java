package com.example.customer_service.dto;

public class MetricDTO {
    private double averageAge;
    private double standardDeviation;
    private int totalCustomers;

    public MetricDTO(double averageAge, double standardDeviation, int totalCustomers) {
        this.averageAge = averageAge;
        this.standardDeviation = standardDeviation;
        this.totalCustomers = totalCustomers;
    }

    // getters y setters
    public double getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(double averageAge) {
        this.averageAge = averageAge;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
