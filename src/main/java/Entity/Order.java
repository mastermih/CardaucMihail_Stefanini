package Entity;

import java.util.List;
import java.util.Map;

public class Order
{
    //Map ?
    private String orderId;
    private User user;
    private List<Product> products;
    private int totalPrice;



    public int totalPrice()
    {
        totalPrice=0;
        for(Product product: products)
        {
            totalPrice += product.getPrice().getProductPrice();
        }
        return totalPrice;
    }

}
