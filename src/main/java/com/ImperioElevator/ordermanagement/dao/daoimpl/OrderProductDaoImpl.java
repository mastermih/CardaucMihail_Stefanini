//package com.ImperioElevator.ordermanagement.dao.daoimpl;
//
//import com.ImperioElevator.ordermanagement.dao.OrderProductDao;
//import com.ImperioElevator.ordermanagement.entity.Order;
//import com.ImperioElevator.ordermanagement.entity.OrderProduct;
//import com.ImperioElevator.ordermanagement.entity.Product;
//import com.ImperioElevator.ordermanagement.valueobjects.Id;
//import com.ImperioElevator.ordermanagement.valueobjects.Price;
//import com.ImperioElevator.ordermanagement.valueobjects.Quantity;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//@Component
//public class OrderProductDaoImpl extends AbstractDao<OrderProduct> implements OrderProductDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public OrderProductDaoImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public Long insert(OrderProduct orderProduct) throws SQLException {
//        String sql = "INSERT INTO \"order_product\" (\"order_id\", \"product_id\", \"quantity\", \"price_order\") VALUES (?, ?, ?, ?)";
//        jdbcTemplate.update(sql,
//                orderProduct.getOrderId().orderId().getId(),
//                orderProduct.getProductId().productId().getId(),
//                orderProduct.getQuantity().getQuantity(),
//                orderProduct.getPriceOrder().getPrice());
//        return orderProduct.getOrderId().orderId().getId();
//    }
//
//    @Override
//    public Long update(OrderProduct orderProduct) throws SQLException {
//        String sql = "UPDATE \"order_product\" SET \"quantity\" = ?, \"price_order\" = ? WHERE \"order_id\" = ? AND \"product_id\" = ?";
//        jdbcTemplate.update(sql,
//                orderProduct.getQuantity().getQuantity(),
//                orderProduct.getPriceOrder().getPrice(),
//                orderProduct.getOrderId().orderId().getId(),
//                orderProduct.getProductId().productId().getId());
//        return orderProduct.getOrderId().orderId().getId();
//    }
//
//    @Override
//    public Long deleteById(Long orderId, Long productId) throws SQLException {
//        String sql = "DELETE FROM \"order_product\" WHERE \"order_id\" = ? AND \"product_id\" = ?";
//        jdbcTemplate.update(sql, orderId, productId);
//        return orderId;
//    }
//
//    @Override
//    public OrderProduct findById(Long orderId, Long productId) throws SQLException {
//        String sql = "SELECT * FROM \"order_product\" WHERE \"order_id\" = ? AND \"product_id\" = ?";
//        try {
//            return jdbcTemplate.queryForObject(sql, new Object[]{orderId, productId}, (resultSet, i) -> mapResultSetToEntity(resultSet));
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
//    @Override
//    public OrderProduct mapResultSetToEntity(ResultSet resultSet) throws SQLException {
//        Id orderId = new Id(resultSet.getLong("order_id"));
//        Id productId = new Id(resultSet.getLong("product_id"));
//        Quantity quantity = new Quantity(resultSet.getInt("quantity"));
//        Price priceOrder = new Price(resultSet.getInt("price_order"));
//
//        Order order = new Order(orderId, null, null, null, null); // Adjust constructor params as needed
//        Product product = new Product(productId, null, null, null, null, null, null, null, null, null, null); // Adjust constructor params as needed
//
//        return new OrderProduct(order, product, quantity, priceOrder);
//    }
//}
