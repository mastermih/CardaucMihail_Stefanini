package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.Order;

public interface MaintenanceService{
    double calculateMaintenanceCost(Order order);
}
