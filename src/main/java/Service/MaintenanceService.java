package Service;
import Entity.Order;
import Entity.Product;

public interface MaintenanceService
    //based on order
{
    double calculateMaintenanceCost(Order order);
}
