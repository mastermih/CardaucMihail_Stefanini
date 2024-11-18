package com.ImperioElevator.ordermanagement.functional;

import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.Product;

import java.sql.SQLException;

public interface PriceChecker {
    boolean checkAndUpdate(Order order, Product product)throws SQLException;
}
