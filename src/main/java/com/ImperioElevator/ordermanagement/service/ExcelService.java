package com.ImperioElevator.ordermanagement.service;

import org.springframework.core.io.ByteArrayResource;

public interface ExcelService {

        ByteArrayResource createOrderInvoice(String fileName, Long orderId);
}