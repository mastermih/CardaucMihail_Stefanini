package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.service.InvoiceService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final OrderProductDaoImpl orderProductDaoImpl;
    private final CdnService cdnService;
    private final EmailService emailService;
    private final OrderDaoImpl orderDao;
    private final NotificationServiceImpl notificationService;

    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    public InvoiceServiceImpl(NotificationServiceImpl notificationService,OrderDaoImpl orderDao, EmailService emailService, CdnService cdnService, OrderProductDaoImpl orderProductDaoImpl) {
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.cdnService = cdnService;
        this.emailService = emailService;
        this.orderDao = orderDao;
        this.notificationService = notificationService;
    }

    @Value("${invoice.template}")
    private  String TEMPLATE_PATH;

    @Override
    public void prepareInvoiceForOrder(Order order, String jwtToken) throws SQLException {
        ByteArrayResource byteArrayResource = createOrderInvoice(order);

        String invoiceFullPath = cdnService.sendExcelInvoiceToCDN(byteArrayResource, jwtToken);

        orderDao.addOrderInvoice(order.orderId().id(), invoiceFullPath);

        emailService.sendInvoiceEmail(order, byteArrayResource);

    }


    @Override
    public ByteArrayResource createOrderInvoice(Order order) {

        // add this in the conf file
        String templatePath = System.getenv("TEMPLATE_PATH");
        try (FileInputStream templateStream = new FileInputStream(templatePath)){

            //Do not forget to remove this
            File file = new File("/app/templates/OrderInvoiceTemplate.xlsx");
            if (!file.exists()) {
                logger.error("Template file not found at: " + file.getAbsolutePath());
            }

            if(templateStream == null){
                logger.error("No template file was found " + templateStream);
                throw new FileNotFoundException("File template was not found" + templateStream);
            }

             Workbook workbook = new XSSFWorkbook(templateStream);


            Sheet sheet = workbook.getSheetAt(0);

            List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId(order.orderId().id());
            int rowIndex = 1; //because 0 are the headers

            for (OrderProduct orderProduct : orderProducts) {
                Row row = sheet.createRow(rowIndex++);

                String nameOfTheProduct = orderProduct.product().productName().productName();
                BigDecimal price = BigDecimal.valueOf(orderProduct.priceOrder().price().longValue());
                Long discountPercentages = orderProduct.discount_percentages().discount_percentages();
                BigDecimal priceWithDiscount = BigDecimal.valueOf(orderProduct.price_discount().price().longValue());
                Long VAT = orderProduct.VAT().VAT();
                BigDecimal priceWithVAT = BigDecimal.valueOf(orderProduct.price_with_VAT().priceWithVAT().doubleValue());



                row.createCell(0).setCellValue(nameOfTheProduct);
                row.createCell(1).setCellValue(price.doubleValue());
                row.createCell(2).setCellValue(discountPercentages);
                row.createCell(3).setCellValue(priceWithDiscount.doubleValue());
                row.createCell(4).setCellValue(VAT);
                row.createCell(5).setCellValue(priceWithVAT.doubleValue());

            }
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
            logger.error("Failed to create order invoice " + e);
        }
        return null;
    }
}
