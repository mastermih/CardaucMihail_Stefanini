package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.entity.Order;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.function.TriFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.function.BiFunction;
import java.util.function.Function;
@Service
public class FunctionalInvoiceEmailSender {

//    private final String sender;
//
//    public FunctionalInvoiceEmailSender(String sender) {
//        this.sender = sender;
//    }

    // Function to generate email content (Subject and Body) from the Order object
    public final Function<Order, String[]> emailContentFunction = order -> new String[]{
            "Order Invoice for Order #" + order.orderId().id(),
            "Your order with ID " + order.orderId().id() + " is ready for payment. Please find the attached invoice for your reference."
    };

    // TriFunction to send email with the JavaMailSender, Order, and ByteArrayResource
    public final TriFunction<JavaMailSender, Order, ByteArrayResource, ByteArrayResource> sendEmailFunction = (mailSender, order, attachment) -> {
        try {
            // Generate the subject and body using emailContentFunction
            String[] emailContent = emailContentFunction.apply(order);
            String subject = emailContent[0];
            String body = emailContent[1];

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set email details
            helper.setFrom("cardaucmihai@gmail.com");
            helper.setTo("cardaucmihai@gmail.com"); // Replace with dynamic recipient if available
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(attachment.getFilename(), attachment);

            // Send email
            mailSender.send(message);

            // Return the same attachment for further use in the pipeline
            return attachment;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send invoice email", e);
        }
    };

    // Usage example method
    public ByteArrayResource processInvoiceEmail(JavaMailSender javaMailSender, Order order, ByteArrayResource invoiceAttachment) {
        // Use the TriFunction to send email with the given attachment
        return sendEmailFunction.apply(javaMailSender, order, invoiceAttachment);
    }
}
