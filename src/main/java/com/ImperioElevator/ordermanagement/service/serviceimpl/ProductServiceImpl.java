package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Paginable;
import com.ImperioElevator.ordermanagement.entity.Product;
import com.ImperioElevator.ordermanagement.service.ProductService;
import com.ImperioElevator.ordermanagement.valueobjects.FilterComponents;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
// Info
@Service

public class ProductServiceImpl implements ProductService {

    private final ProductDaoImpl productDao;

    public ProductServiceImpl(ProductDaoImpl productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> fiendProductForMainPage(Long limit, String categoryType) throws SQLException {
        return productDao.fiendProductForMainPage(limit, categoryType);
    }

    @Override
    public Product findProductById(Long id) throws SQLException {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findProductByName(String  name) throws SQLException {
        return productDao.fiendProductByName(name);
    }

    @Override
    public Paginable<Product> filterProducts(FilterComponents filterComponents, Long page, Long pageSize) throws SQLException {
        return productDao.filterProducts(filterComponents, page, pageSize);
    }

}
