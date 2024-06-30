package ValueObjects;

public class Price
{
    private int productPrice;

    public Price(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return "Price{" +
                "productPrice=" + productPrice +
                '}';
    }
}
