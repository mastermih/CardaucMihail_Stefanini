package Entity;

import ValueObjects.*;

public class Product {

    private ID product_id;
    private Price price;
    private Width width;
    private Category category;
    private ProductBrand productBrand;
    private ProductName productName;
    private ElectricityConsumption electricityConsumption;
    private Description description;
    private Height height;
    private Depth depth;

    public Product(ID productId, Price price, Width width, Height height, Depth depth,
                   Category category, ProductBrand productBrand, ProductName productName,
                   ElectricityConsumption electricityConsumption, Description description) {
        this.product_id = productId;
        this.price = price;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.category = category;
        this.productBrand = productBrand;
        this.productName = productName;
        this.electricityConsumption = electricityConsumption;
        this.description = description;
    }

    public Depth getDepth() {
        return depth;
    }

    public void setDepth(Depth depth) {
        this.depth = depth;
    }

    public void setWidth(Width width) {
        this.width = width;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    // Getters and setters (optional, but recommended for encapsulation)
    public ID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(ID id) {
        this.product_id = id;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Width getWidth() {
        return  width;
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

    public ElectricityConsumption getElectricityConsumption() {
        return electricityConsumption;
    }

    public void setElectricityConsumption(ElectricityConsumption electricityConsumption) {
        this.electricityConsumption = electricityConsumption;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", price=" + price +
                ", width=" + width +
                ", category=" + category +
                ", productBrand=" + productBrand +
                ", productName=" + productName +
                ", electricityConsumption=" + electricityConsumption +
                ", description=" + description +
                ", height=" + height +
                ", depth=" + depth +
                '}';
    }
}
