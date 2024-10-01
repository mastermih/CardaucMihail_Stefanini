package com.ImperioElevator.ordermanagement.valueobjects;

public class Width {

    private double width;

    public Width(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Width{" +
                "width=" + width +
                '}';
    }
}