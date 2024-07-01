package DaoImplementation;
import Entity.Category;

import DaoWork.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDaoImpl extends AbstractDao<Category>
{
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public CategoryDaoImpl(Connection connection) {
       super(connection);
    }

    @Override
    public void insert(Category category) throws SQLException
    {
       String sql = "INSERT INTO category (id, name, parent_id) VALUES (?,?,?);";
       try (PreparedStatement statement = connection.prepareStatement(sql))
       {
           statement.setLong(1, category.getId());
           statement.setString(2, category.getName());
           if(category.getParent_id() != null)
           {
               statement.setLong(3, category.getParent_id().getId());
           }
           else
           {
              statement.setNull(3, java.sql.Types.BIGINT);
           }
           statement.executeUpdate();
           logger.info("Inserted category with ID: {}", category.getId());

       }


    }

    @Override
    public void update(Category category) throws SQLException
    {
        String sql = "UPDATE category SET name = ?, parent_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName());
            if (category.getParent_id() != null) {
                statement.setLong(2, category.getParent_id().getId());
            } else {
                statement.setNull(2, java.sql.Types.BIGINT);
            }
            statement.setLong(3, category.getId());
            statement.executeUpdate();
            logger.info("Updated category with ID: {}", category.getId());
        }
    }

    @Override
    public void deleteById(long id) throws SQLException
    {
       String sql = "DELETE FROM category WHERE id = ?;";
       try(PreparedStatement statement = connection.prepareStatement(sql))
       {
           statement.setLong(1, id);
           statement.executeUpdate();
           logger.info("Deleted category with ID: {}", id);

       }
    }

    @Override
    public Category findById(long id) throws SQLException {
         String sql = "SELECT * FROM category WHERE id = ?;";
         try(PreparedStatement statement = connection.prepareStatement(sql))
         {
             statement.setLong(1, id);
             try (ResultSet resultSet = statement.executeQuery())
             {
                 if (resultSet.next())
                 {
                    return mapResultSetToEntity(resultSet);
                 }
             }
         }
         return null;
    }

    @Override
    public Category mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        long parentIdValue = resultSet.getLong("parent_id");
        Category parent = null;

        if (!resultSet.wasNull()) {
            parent = new Category(parentIdValue, null, null);
        }

        return new Category(id, name, parent);
    }
}
