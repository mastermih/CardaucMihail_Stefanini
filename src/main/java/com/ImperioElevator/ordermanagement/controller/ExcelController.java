package com.ImperioElevator.ordermanagement.controller;

import com.ImperioElevator.ordermanagement.security.JwtService;
import com.ImperioElevator.ordermanagement.service.ExcelService;
import com.ImperioElevator.ordermanagement.service.OrdersService;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import com.ImperioElevator.ordermanagement.service.serviceimpl.CdnService;
import com.ImperioElevator.ordermanagement.service.serviceimpl.ExcelServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@RestController
public class ExcelController {
    private final CdnService cdnService;
    private final JwtService jwtService;
    private final ExcelService excelService;
    private final OrdersService orderService;

    public ExcelController(OrdersService orderService,ExcelService excelService, CdnService cdnService, JwtService jwtService) {
        this.cdnService = cdnService;
        this.jwtService = jwtService;
        this.excelService = excelService;
        this.orderService = orderService;
    }

    @PostMapping("/uploadExcelInvoice")
    public ResponseEntity<String> uploadExcelInvoice ( @RequestParam("orderId") Long orderId,
                                                       @RequestParam("userId") Long userId,
                                                       @RequestParam("Authorization") String authorizationHeader){
        try{
            String jwtToken = authorizationHeader.replace("Bearer ", "");
            String fileName = "OrderInvoice_User_" + userId + "_orderId_" + orderId + ".xlsx";

            excelService.createOrderInvoice(fileName, orderId);

            if(authorizationHeader == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing");
            }
            String cdnUrl = cdnService.sendExcelInvoiceToCDN(fileName, userId, jwtToken);
            if (cdnUrl == null) {
                return new ResponseEntity<>("Failed to upload image to CDN", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Create this method and add the new column in db
            // For example the manager is  going to see the invoice name of the user
            //Idk if it is a good idea to have the  order service in the invoice controller
            orderService.addOrderInvoice(orderId, cdnUrl);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        }catch (Exception  e) {
            return new ResponseEntity<>("Error occurred while uploading file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            File generatedFile = new File("OrderInvoice_User_" + userId + "_orderId_" + orderId + ".xlsx");
            if (generatedFile.exists()){
                generatedFile.delete();
            };
        }
    }
}
