package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.service.EmailService;
import com.ImperioElevator.ordermanagement.valueobjects.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender; // Studiu ++

    private final OrderDaoImpl orderDao;

    @Value("${spring.mail.username}")
    private String sender;  // Studiu ++

    public EmailServiceImpl(OrderDaoImpl orderDao) {
        this.orderDao = orderDao;
    }

    public String sendSimpleMail(EmailDetails details) {
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());

            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail " + e;
        }
    }

    @Override
    public String sendConfirmationMail(EmailDetails details, Long orderId) {
        String confirmationLink = "http://localhost:3000/sendMail/confirm/" + orderId;
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

      //      Constructing the email body
          String emailBody = details.getMsgBody();
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(emailBody);
            mailMessage.setSubject(details.getSubject());
                // Sending the mail
                javaMailSender.send(mailMessage);
                return "Mail Sent Successfully...";
            }

            // Catch block to handle the exceptions
            catch (Exception e) {
                return "Error while Sending Mail " + e;
            }
    }

    @Override
    public Long updateOrderEmailConfirmStatus(Long id) throws SQLException {
        return orderDao.updateOrderEmailConfirmStatus(id);
    }


}