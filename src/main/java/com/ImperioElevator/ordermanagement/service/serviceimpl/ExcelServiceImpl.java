package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.service.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelServiceImpl implements ExcelService {
    private final OrderProductDaoImpl orderProductDaoImpl;

    public ExcelServiceImpl(OrderProductDaoImpl orderProductDaoImpl) {
        this.orderProductDaoImpl = orderProductDaoImpl;
    }

    @Override
    public void createOrderInvoice(String fileName, Long orderId) {
        try(FileInputStream templateStream = new FileInputStream("src/main/resources/templates/OrderInvoiceTemplate.xlsx");
         Workbook workbook = new XSSFWorkbook(templateStream)){

        Sheet sheet = workbook.getSheetAt(0);

        List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(orderId);
        int rowIndex = 1; //because 0 are the headers

        for (OrderProduct orderProduct : orderProducts) {
            Row row = sheet.createRow(rowIndex++);

            String nameOfTheProduct = orderProduct.product().productName().productName();

            Long price = (long) orderProduct.priceOrder().price();
            Long discountProcentanges = 10L;
            Long priceWithDiscount = price - (price * discountProcentanges / 100);
            Long VAT = 20L;
            Long priceWithVAT = priceWithDiscount + (priceWithDiscount * VAT / 200);

            row.createCell(0).setCellValue(nameOfTheProduct);
            row.createCell(1).setCellValue(price);
            row.createCell(2).setCellValue(discountProcentanges);
            row.createCell(3).setCellValue(priceWithDiscount);
            row.createCell(4).setCellValue(VAT);
            row.createCell(5).setCellValue(priceWithVAT);
        }
        try(FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            System.out.println("Excel file created " + fileName);
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
