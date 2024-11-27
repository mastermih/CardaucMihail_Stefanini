package com.ImperioElevator.ordermanagement.factory.factoryimpl;

import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.factory.EmailServiceFactory;
import com.ImperioElevator.ordermanagement.service.EmailService;
import org.springframework.stereotype.Component;

@Component
public class EmailFactoryImpl implements EmailServiceFactory {

    @Override
    public EmailDetails createEmailServiceUpdateOrderStatus(String message) {
        EmailDetails details = new EmailDetails();
        details.setRecipient("cardaucmihai@gmail.com");
        details.setSubject("Order Confirmation");
        details.setMsgBody(message);
        return details;
    }

    @Override
    public EmailDetails createEmailServiceUserCreation(String message) {
        EmailDetails details = new EmailDetails();
        details.setRecipient("cardaucmihai@gmail.com");
        details.setSubject("User Confirmation");
        details.setMsgBody(message);
        return details;
    }

    @Override
    public EmailDetails sendInvoiceEmail(String message) {
        EmailDetails details = new EmailDetails();
        details.setRecipient("cardaucmihai@gmail.com");
        details.setSubject("Order Invoice");
        details.setMsgBody(message);
        // maybe here have to be also the attachments
        return details;
    }
}
