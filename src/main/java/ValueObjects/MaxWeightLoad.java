package ValueObjects;

public class MaxWeightLoad
{
    private int maxWeight;

    public MaxWeightLoad(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString() {
        return "MaxWeightLoad{" +
                "maxWeight=" + maxWeight +
                '}';
    }
}
