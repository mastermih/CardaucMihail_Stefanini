package Runner;

import DaoImplementation.OrderDaoImpl;
import DaoImplementation.ProductDaoImpl;

import java.sql.SQLException;

public class DaoRunner {
    public static void main(String[] args) throws SQLException {
        ProductDaoImpl productDao = ProductDaoImpl.getInstance();
        OrderDaoImpl orderDao = OrderDaoImpl.getInstance();

        String dai = orderDao.getOrderWithProduct(3).toString();
        System.out.println(dai);
        // String nishu = productDao.getProductPagination(5).toString();
        //System.out.println(nishu);

       // orderDao.findById(3);

        //orderDao.deleteById(7);

//        Order order = new Order(7, 2, 1, 8, 0);
//        orderDao.insert(order);
//        System.out.println("A mers");

     //   Order update_order = new Order(1, 9, 1, 9, 0);
      //  orderDao.update(update_order);

//        // Create a new product
//        Product product = new Product(
//                new ID(13),
//                new Price(100),
//                new Width(5.6),
//                new Height(4.3),
//                new Depth(5.7),
//                Type.ElevatorType.FREIGHT,
//                new ProductBrand("example brand"),
//                new ProductName("example name"),
//                new ElectricityConsumption(50),
//               new Description("example description")
//                );
//
//        // Add the product to the database
//        productDao.insert(product);
//        System.out.println("New product added successfully!");
//
//         //Update the product in the database
//        Product productToUpdate = new Product(
//                new ID(6),  // This is the product_id you want to update
//                new Price(120),
//                new Width(6.0),
//                new Height(4.5),
//                new Depth(6.0),
//                Type.ElevatorType.RESIDENTIAL_ELEVATORS,
//                new ProductBrand("updated brand"),
//                new ProductName("updated name"),
//                new ElectricityConsumption(60),
//                new Description("updated description")
//        );
//       // productToUpdate.setType(Type.ElevatorType.RESIDENTIAL_ELEVATORS);  // Modify as needed
//        productDao.update(productToUpdate);  // This uses the product with product_id 3
//        System.out.println("Product updated successfully!");
//        // Find a product by ID
//        Product foundProduct = productDao.findById(9);
//        if (foundProduct != null) {
//            System.out.println("Found product: " + foundProduct);
//       } else {
//            System.out.println("Product with ID 5 not found!");
//        }
//
//         //Delete a product by ID
//        productDao.deleteById(5);
//        System.out.println("Product with ID 5 deleted successfully!");

    }
}
