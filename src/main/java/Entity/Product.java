package Entity;

import Enum.*;
import ValueObjects.*;

public class Product

    // Prodact, Category, Order in baza la order sa va face Maintenanceul
    // La order se atribuie si alte chitibusuri ca oglind lampi si asa mai departe
    // need to modify the generic item wh can be caegorised by category example (lift, oglinda aircoler, alarm sys)
{
    private ID id;
    private Price price;
    private Size size;
    private Category category;
    private Type type;
    private ProductBrand productBrand;
    private ProductName productName;
    private ElectricityConsumption electricityConsumption;


    //Pentru calculul pretului total
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
