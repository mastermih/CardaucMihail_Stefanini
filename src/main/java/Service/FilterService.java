package Service;

import Entity.Product;
import ValueObjects.*;
import Enum.*;

import java.lang.reflect.Type;
import java.util.List;
import ValueObjects.FilterComponents;

public interface FilterService
    // filtrul va fi facut in baza la obiect
{
    List<Product> filter(List<Product> products, FilterComponents filterComponents);
}
