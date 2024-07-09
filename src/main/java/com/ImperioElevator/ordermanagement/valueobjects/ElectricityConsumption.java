package com.ImperioElevator.ordermanagement.valueobjects;

public class ElectricityConsumption
{
    private int kWh;

    public ElectricityConsumption(int kWh) {
        this.kWh = kWh;
    }

    public int getkWh() {
        return kWh;
    }

    public void setkWh(int kWh) {
        this.kWh = kWh;
    }

    @Override
    public String toString() {
        return "ElectricityConsumption{" +
                "kWh=" + kWh +
                '}';
    }
}
