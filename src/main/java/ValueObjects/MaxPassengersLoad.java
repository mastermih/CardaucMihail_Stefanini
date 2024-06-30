package ValueObjects;

public class MaxPassengersLoad
{
    private int maxPassengers;

    public MaxPassengersLoad(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(int maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    @Override
    public String toString() {
        return "MaxPassengersLoad{" +
                "maxPassengers=" + maxPassengers +
                '}';
    }
}
