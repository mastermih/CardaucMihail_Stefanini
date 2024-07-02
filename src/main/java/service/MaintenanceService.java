package service;
import entity.Order;

public interface MaintenanceService
    //based on order or product ?
{
    double calculateMaintenanceCost(Order order);
}
