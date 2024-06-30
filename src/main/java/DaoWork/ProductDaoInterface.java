package DaoWork;

import Entity.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDaoInterface extends DaoMain<Product>
{
    List<Product> getProductPagination(int numberOfProducts) throws SQLException;
}
