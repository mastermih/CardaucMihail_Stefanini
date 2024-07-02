package valueobjects;

public class ProductName
{
    private String productName;

    public ProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductName{" +
                "productName='" + productName + '\'' +
                '}';
    }
}
