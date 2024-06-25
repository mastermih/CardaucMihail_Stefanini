package Service;

import Entity.Product;

import java.util.List;

public interface ProductService
 //product service
{
 Product getProductById(int id);
 List<Product>getAllProducts();
 Product deleteProduct(int id);
 Product addProduct(Product product);
 Product updateProduct(Product product);
}
