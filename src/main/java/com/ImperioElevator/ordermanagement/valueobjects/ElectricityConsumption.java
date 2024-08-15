package com.ImperioElevator.ordermanagement.valueobjects;

public class ElectricityConsumption
{
    private double kWh;

    public ElectricityConsumption(double kWh) {
        this.kWh = kWh;
    }

    public double getkWh() {
        return kWh;
    }

    public void setkWh(double kWh) {
        this.kWh = kWh;
    }

}
