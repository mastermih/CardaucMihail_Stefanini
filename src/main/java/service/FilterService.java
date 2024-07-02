package service;

import entity.Product;


import java.util.List;
import valueobjects.FilterComponents;

public interface FilterService
    // filtrul va fi facut in baza la obiect
{
    List<Product> filter(List<Product> products, FilterComponents filterComponents);
}
