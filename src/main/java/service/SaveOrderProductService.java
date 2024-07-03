package service;

import daoimplementation.OrderDaoImpl;
import daoimplementation.OrderProductDaoImpl;

public class SaveOrderProductService {
    //vine un obiect care se va salva in db de la cliend dupa ce face submit
    //statului il definm in service layer
    //se salveaza mai intii in order
    // apoi ducem inservice unde operam si trimitem in order_product
    // Vine spre salvare un obiect mai mare
    // in service call la sava order_product realtionship
    //
    private static OrderDaoImpl orderDaoImpl;
    private static OrderProductDaoImpl orderProductDaoImpl;
}
