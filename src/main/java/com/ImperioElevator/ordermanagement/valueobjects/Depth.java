package com.ImperioElevator.ordermanagement.valueobjects;

public class Depth
{
    private double depth;

    public Depth(double depth) {
        this.depth = depth;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "Depth{" +
                "depth=" + depth +
                '}';
    }
}
