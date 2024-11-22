package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.OrderProduct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ExcelServiceImpl {
    private final OrderDaoImpl orderDao;
    private final OrderProductDaoImpl orderProductDaoImpl;
    private final UserDaoImpl userDao;

    private List<Object[]> orderData = new ArrayList<>();
    private Workbook workbook;

    public ExcelServiceImpl(OrderDaoImpl orderDao, OrderProductDaoImpl orderProductDaoImpl, UserDaoImpl userDao){
        this.orderDao = orderDao;
        this.orderProductDaoImpl = orderProductDaoImpl;
        this.userDao = userDao;
    }

    public void ExcelOrderInvoice() {
        // Initialize the workbook and create the sheet
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("excel-sheet");

        Row rowHeader = sheet.createRow(0);

        rowHeader.createCell(0).setCellValue("Name of the product");
        rowHeader.createCell(1).setCellValue("price");
        rowHeader.createCell(2).setCellValue("discount in %");
        rowHeader.createCell(3).setCellValue("VAT");
        rowHeader.createCell(4).setCellValue("price with vat");

    }

    List<OrderProduct> orderProducts = orderProductDaoImpl.findByOrderId();
    int rowIndex = 1; //because 0 are the headers

    for(OrderProduct orderProduct : orderProducts) {

    }
    public Workbook getWorkbook() {
        return workbook;
    }

    public List<Object[]> getEmployeeData() {
        return orderData;
    }
}
