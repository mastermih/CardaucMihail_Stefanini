package ValueObjects;
import Enum.*;


public class FilterComponents
{
    private Price price;
    private Type type;
    private ProductBrand productBrand;
    private ProductName productName;

    public FilterComponents(Price price,  Type type, ProductBrand productBrand, ProductName productName) {
        this.price = price;
        this.type = type;
        this.productBrand = productBrand;
        this.productName = productName;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }



    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public ProductBrand getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(ProductBrand productBrand) {
        this.productBrand = productBrand;
    }

    public ProductName getProductName() {
        return productName;
    }

    public void setProductName(ProductName productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "FilterComponents{" +
                "price=" + price +
                ", type=" + type +
                ", productBrand=" + productBrand +
                ", productName=" + productName +
                '}';
    }
}
