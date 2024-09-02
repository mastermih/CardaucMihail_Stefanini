package com.ImperioElevator.ordermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

@EnableScheduling
@ControllerAdvice// This mf allows  to handle exceptions across the whole application in one global place

@SpringBootApplication
public class OrderManagementApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderManagementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementApplication.class, args);
        log.info("Application started successfully");
    }
}
