package service;
import java.sql.*;
import java.util.List;

import daoimplementation.OrderDaoImpl;
import daoimplementation.OrderProductDaoImpl;
import entity.Order;
import entity.OrderProduct;
import valueobjects.Id;

public class SaveOrderProductService {
    //vine un obiect care se va salva in db de la cliend dupa ce face submit
    //statului il definm in service layer
    //se salveaza mai intii in order
    // apoi ducem inservice unde operam si trimitem in order_product
    // Vine spre salvare un obiect mai mare
    // in service call la sava order_product realtionship
    // idul si statusul il prdefinim in service laier
    // dupa ce din service trimitem in order ne intoarcem inapoi in service si trimitem in order product
    // We will get fro the user UserOrder(user_id, <pentru order>) (product_id, quantity, price_product <pentru category order>)
    private final  OrderDaoImpl orderDaoImpl;
    private final  OrderProductDaoImpl orderProductDaoImpl;

    public SaveOrderProductService(Connection connection) {
        this.orderDaoImpl = new OrderDaoImpl(connection);
        this.orderProductDaoImpl = new OrderProductDaoImpl(connection);
    }


}
