package Entity;

import Enum.*;
import ValueObjects.*;

import java.lang.reflect.Parameter;

public class Product

    // Prodact, Category, Order in baza la order sa va face Maintenanceul
    // need to modify the generic item wh can be caegorised by category example (lift, oglinda aircoler, alarm sys)
{
    private ID id;
    private Price price;
    private Parameter parameter;
    private Category category;
    private Type type;
    private ProductBrand productBrand;
    private ProductName productName;
    private ElectricityConsumption electricityConsumption;
    private Description description;


    //Pentru calculul pretului total
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
