package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.entity.Order;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class PipeLineInvoiceService {

    private final FunctionalInvoiceCreatorService invoiceCreatorService;
    private final InvoiceUploaderServiceImpl uploaderService;
    private final FunctionalInvoiceEmailSender emailSender;
    private final addOrderInvoicePath orderUpdater;
    private final NotificationHelperServiceImpl notificationHelperService;
    private final JavaMailSender javaMailSender;

    public PipeLineInvoiceService(
            JavaMailSender javaMailSender,
            FunctionalInvoiceCreatorService invoiceCreatorService,
            InvoiceUploaderServiceImpl uploaderService,
            FunctionalInvoiceEmailSender emailSender,
            addOrderInvoicePath orderUpdater,
            NotificationHelperServiceImpl notificationHelperService) {
        this.invoiceCreatorService = invoiceCreatorService;
        this.uploaderService = uploaderService;
        this.emailSender = emailSender;
        this.orderUpdater = orderUpdater;
        this.notificationHelperService = notificationHelperService;
        this.javaMailSender = javaMailSender;
    }

    //This is not correct the Operators are taken from the out side of the function
    public Long buildPipeline(Order order, String jwtToken, List<String> operators) throws SQLException {
        //  Create the invoice
        Function<Order, ByteArrayResource> createInvoice = invoiceCreatorService::createInvoice;

        //  Upload the invoice to CDN
        BiFunction<ByteArrayResource, String, String> uploadInvoice = uploaderService::uploadInvoice;

        // Update the order with the invoice path
        BiFunction<Order, String, Order> updateOrderWithInvoice = (o, invoicePath) -> {
            orderUpdater.processInvoice(o, invoicePath);
            return o; // This is used to return the order for chaining
        };

        // Send the invoice email
        TriFunction<JavaMailSender, Order, ByteArrayResource, ByteArrayResource> sendEmailFunction = emailSender.sendEmailFunction;


        // Notify operators - :(
//        Function<Order, Long> notifyOperators = o -> {
//            ByteArrayResource invoiceResource = createInvoice.apply(o); // Reuse the invoice for notification
//            notificationHelperService.insertNotificationWithInvoice(o, invoiceResource, operators);
//            return o.orderId().id();
//        };

        // Combine the pipeline using andThen


        return createInvoice
                .andThen(invoice -> {
                    String invoicePath = uploadInvoice.apply(invoice, jwtToken);
                    updateOrderWithInvoice.apply(order, invoicePath);
                    return invoice; // Pass the ByteArrayResource to the next step
                })
                .andThen(invoice -> {
                    sendEmailFunction.apply(javaMailSender, order, invoice);
                    return order; // Pass the updated order to the next step
                })
                //.andThen(notifyOperators) // Notify operators
                .apply(order)
                .orderId().id();
    }
}


//        return createInvoice
//                .andThen(invoice -> {
//                    String invoicePath = uploadInvoice.apply(invoice, jwtToken); // Send invoice and get path
//                    updateOrderWithInvoice.apply(order, invoicePath); // Update the order with the path
//                    sendEmailFunction.apply(javaMailSender, order, invoice); // Send the email
//                    return order; // Return the order for notification step
//                })
//                //.andThen(notifyOperators)
//                .apply(order)
//                .orderId().id();
//    }
//}