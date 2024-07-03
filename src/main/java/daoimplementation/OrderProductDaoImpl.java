package daoimplementation;

import daointerface.OrderProductDao;
import daowork.AbstractDao;
import entity.Order;
import entity.OrderProduct;
import entity.Product;
import valueobjects.Id;
import valueobjects.Price;
import valueobjects.Quantity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderProductDaoImpl extends AbstractDao<OrderProduct> implements OrderProductDao{

    public OrderProductDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public long insert(OrderProduct orderProduct) throws SQLException {
        String sql = "INSERT INTO order_product (order_id, product_id, quantity, price_order) VALUES (?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setLong(1 , orderProduct.getOrderId().getOrderId().getId());
            statement.setLong(2 , orderProduct.getProductId().getProductId().getId());
            statement.setInt(3 , orderProduct.getQuantity().getQuantity());
            statement.setInt(4 , orderProduct.getPriceOrder().getPrice());
            statement.executeUpdate();
        }
        return orderProduct.getOrderId().getOrderId().getId();
    }

    @Override
    public long update(OrderProduct orderProduct) throws SQLException {
        String sql =  "UPDATE order_product SET quantity = ?, price_order = ? WHERE order_id = ? AND product_id = ?";
        try(PreparedStatement statement =  connection.prepareStatement(sql)){
            statement.setInt(1, orderProduct.getQuantity().getQuantity());
            statement.setInt(2, orderProduct.getPriceOrder().getPrice());
            statement.setLong(3, orderProduct.getOrderId().getOrderId().getId());
            statement.setLong(4, orderProduct.getProductId().getProductId().getId());
            statement.executeUpdate();
        }
        return orderProduct.getOrderId().getOrderId().getId();
    }
    //Trebu de clarificat cu DAo si te ster metodele acestea doua
    @Override
    public long deleteById(long id) throws SQLException {
        return 0;
    }

    @Override
    public OrderProduct findById(long id) throws SQLException {
        return null;
    }

    @Override
    public long deleteById(long id, long product_id) throws SQLException {
       String sql = "DELETE FROM order_product WHERE order_id = ? AND product_id = ?";
       try(PreparedStatement statement  = connection.prepareStatement(sql)){
           statement.setLong(1, id);
           statement.setLong(2, product_id);
           statement.executeUpdate();
       }
       return id;
    }

    @Override
    public OrderProduct findById(long id , long productId) throws SQLException {
        String sql = "SELECT * FROM order_product WHERE order_id = ? AND product_id = ?";
        try(PreparedStatement statement  = connection.prepareStatement(sql)){
            statement.setLong(1, id);
            statement.setLong(2, productId);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return mapResultSetToEntity(resultSet);
                }
            }
        }
        return null;

    }

    @Override
    public OrderProduct mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Id orderId = new Id(resultSet.getLong("order_id"));
        Id productId = new Id(resultSet.getLong("product_id"));
        Quantity quantity = new Quantity(resultSet.getInt("quantity"));
        Price priceOrder = new Price(resultSet.getInt("price_order"));

        Order order = new Order(orderId, null, null, null, null); // Adjust constructor params as needed
        Product product = new Product(productId, null, null, null, null, null, null, null, null, null); // Adjust constructor params as needed

        return new OrderProduct(order, product, quantity, priceOrder);
    }
}
