package ValueObjects;

public class Height
{
    private double height;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Height(double height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Height{" +
                "height=" + height +
                '}';
    }
}
