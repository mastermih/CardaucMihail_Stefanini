package ValueObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBrand
    // to change db based

{
    private String productBrand;

    public ProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    @Override
    public String toString() {
        return "ProductBrand{" +
                "productBrand='" + productBrand + '\'' +
                '}';
    }
}
