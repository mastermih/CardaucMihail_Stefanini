package Service;
import Entity.Order;
import Entity.Product;

public interface MaintenanceService
    //based on order or product ?
{
    double calculateMaintenanceCost(Order order);
}
