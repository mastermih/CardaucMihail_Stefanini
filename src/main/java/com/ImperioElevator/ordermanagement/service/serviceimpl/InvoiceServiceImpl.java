package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.service.ExcelService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

@Service
public class InvoiceServiceImpl implements ExcelService {
    private final OrderProductDaoImpl orderProductDaoImpl;

    public InvoiceServiceImpl(OrderProductDaoImpl orderProductDaoImpl) {
        this.orderProductDaoImpl = orderProductDaoImpl;
    }

    @Override
    public ByteArrayResource createOrderInvoice(String fileName, Long orderId) {
        try (FileInputStream templateStream = new FileInputStream("src/main/resources/templates/OrderInvoiceTemplate.xlsx");
             Workbook workbook = new XSSFWorkbook(templateStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(orderId);
            int rowIndex = 1; //because 0 are the headers

            for (OrderProduct orderProduct : orderProducts) {
                Row row = sheet.createRow(rowIndex++);
                //ToDo this field have to b in the order not in the ExcelService/ Dicount per oprderProduct not Order TVA same there

                String nameOfTheProduct = orderProduct.product().productName().productName();
                Long price = (long) orderProduct.priceOrder().price();
                Long discountProcentanges = orderProduct.discount_percentages().discount_percentages();
                Long priceWithDiscount = (long) orderProduct.price_discount().price();
                Long VAT = orderProduct.VAT().VAT();
                Double priceWithVAT = orderProduct.price_with_VAT().price();


//            Long discountProcentanges = 10L;
//            Long priceWithDiscount = price - (price * discountProcentanges / 100);
//            Long VAT = 20L;
//            Long priceWithVAT = priceWithDiscount + (priceWithDiscount * VAT / 200);

                row.createCell(0).setCellValue(nameOfTheProduct);
                row.createCell(1).setCellValue(price);
                row.createCell(2).setCellValue(discountProcentanges);
                row.createCell(3).setCellValue(priceWithDiscount);
                row.createCell(4).setCellValue(VAT);
                row.createCell(5).setCellValue(priceWithVAT);
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return new ByteArrayResource(outputStream.toByteArray()) {
                    @Override
                    public String getFilename() {
                        return fileName;
                    }
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
