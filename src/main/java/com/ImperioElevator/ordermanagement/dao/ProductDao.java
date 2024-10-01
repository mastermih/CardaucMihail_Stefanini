package com.ImperioElevator.ordermanagement.dao;

import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.enumobects.CategoryType;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import com.ImperioElevator.ordermanagement.valueobjects.Name;
import com.ImperioElevator.ordermanagement.valueobjects.Number;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao extends Dao<Product> {
   List<Product> fiendProductForMainPage(Long limit, String categoryType);
   List<Product> fiendProductByName(String name);
   Paginable<Product> filterProducts(FilterComponents filterComponents, Long page, Long pageSize);
   Product findProductId(Long id) throws SQLException;

}
