package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FunctionalInvoiceCreatorService {

    private final OrderProductDaoImpl orderProductDaoImpl;

    @Value("${invoice.template}")
    private String templatePath;

    // Constructor injection
    public FunctionalInvoiceCreatorService(OrderProductDaoImpl orderProductDaoImpl) {
        this.orderProductDaoImpl = orderProductDaoImpl;
    }

    // TriFunction to create an invoice
    public final TriFunction<Order, OrderProductDaoImpl, String, ByteArrayResource> createInvoiceFunction = (order, dao, templatePath) -> {
        try (FileInputStream templateStream = new FileInputStream(templatePath)) {
            Workbook workbook = new XSSFWorkbook(templateStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Fetch order products using the passed DAO
            List<OrderProduct> orderProducts = dao.findByOrderId(order.orderId().id());
            AtomicInteger rowIndex = new AtomicInteger(1); // Start from row 1 (headers are at row 0)

            // Populate the sheet with order product data
            orderProducts.forEach(orderProduct -> {
                Row row = sheet.createRow(rowIndex.getAndIncrement());
                row.createCell(0).setCellValue(orderProduct.product().productName().productName());
                row.createCell(1).setCellValue(orderProduct.priceOrder().price().doubleValue());
                row.createCell(2).setCellValue(orderProduct.discount_percentages().discount_percentages());
                row.createCell(3).setCellValue(orderProduct.price_discount().price().doubleValue());
                row.createCell(4).setCellValue(orderProduct.VAT().VAT());
                row.createCell(5).setCellValue(orderProduct.price_with_VAT().priceWithVAT().doubleValue());
            });

            // Generate the ByteArrayResource
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return new ByteArrayResource(outputStream.toByteArray()) {
                    @Override
                    public String getFilename() {
                        return "OrderInvoice_Order_" + order.orderId().id() + ".xlsx";
                    }
                };
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order invoice", e);
        }
    };

    public ByteArrayResource createInvoice(Order order) {
        // Call the TriFunction with the DAO and template path
        return createInvoiceFunction.apply(order, orderProductDaoImpl, templatePath);
    }
}

