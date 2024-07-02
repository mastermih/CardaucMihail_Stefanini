package valueobjects;

public class Price
{
    private int price;

    public Price(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int Price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                '}';
    }
}
