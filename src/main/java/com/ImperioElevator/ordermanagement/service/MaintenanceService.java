package com.ImperioElevator.ordermanagement.service;


import com.ImperioElevator.ordermanagement.entity.Order;

public interface MaintenanceService{
    //based on order or product ?
    double calculateMaintenanceCost(Order order);
}
