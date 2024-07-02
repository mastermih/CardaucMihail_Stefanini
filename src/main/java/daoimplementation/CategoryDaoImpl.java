package daoimplementation;
import entity.Category;

import daowork.AbstractDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valueobjects.Id;
import valueobjects.Name;

import java.sql.*;

public class CategoryDaoImpl extends AbstractDao<Category>
{

    public CategoryDaoImpl(Connection connection) {
       super(connection);
    }

    @Override
    public long  insert(Category category) throws SQLException {
       String sql = "INSERT INTO category (name, parent_id) VALUES (?,?);";
       try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
           statement.setString(1, category.getName().getName());
           if(category.getParentId() != null)
           {
               statement.setLong(2, category.getParentId().getId().getId());
           }
           else
           {
              statement.setNull(2, java.sql.Types.BIGINT);
           }
           statement.executeUpdate();
        try (ResultSet generatedKays = statement.getGeneratedKeys()){
                if(generatedKays.next()) {
                    return generatedKays.getLong(1);
                }else {
                    throw new SQLException("Creating category field, Id problem");
                }
            }
       }
    }

    @Override
    public long update(Category category) throws SQLException
    {
        String sql = "UPDATE category SET name = ?, parent_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getName().getName());
            if (category.getParentId() != null) {
                statement.setLong(2, category.getParentId().getId().getId());
            } else {
                statement.setNull(2, java.sql.Types.BIGINT);
            }
            statement.setLong(3, category.getId().getId());
            statement.executeUpdate();
        }
        return category.getId().getId();

    }

    @Override
    public long deleteById(long id) throws SQLException
    {
       String sql = "DELETE FROM category WHERE id = ?;";
       try(PreparedStatement statement = connection.prepareStatement(sql))
       {
           statement.setLong(1, id);
           statement.executeUpdate();

       }
       return id;
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
        Id id = new Id(resultSet.getLong("id"));
        Name name = new Name(resultSet.getString("name"));
        long parentIdValue = resultSet.getLong("parent_id");
        Category parent = null;

        if (!resultSet.wasNull()) {
            parent = new Category(new Id(parentIdValue), null, null);
        }

        return new Category(id, name, parent);
    }
}
