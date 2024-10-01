package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.OrderDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final OrderDaoImpl orderDao;
    private final UserDaoImpl userDao;


    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(OrderDaoImpl orderDao, UserDaoImpl userDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
    }
//TODO delete this method because this was a test
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
    public String sendConfirmationMail(EmailDetails details, Long token) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

      //    Constructing the email body
          String emailBody = details.getMsgBody() ;
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(emailBody);
            mailMessage.setSubject(details.getSubject());
                // Sending the mail
                javaMailSender.send(mailMessage);
                return "Mail Sent Successfully...";
            }

            catch (Exception e) {
                return "Error while Sending Mail " + e;
            }
    }

    @Override
    public String updateOrderEmailConfirmStatus(String token) throws SQLException {
        // Update the order status
        orderDao.updateOrderEmailConfirmStatus(token);

        // Disable the token after confirmation
        orderDao.disableTokenAfterConfirmation(token);

        return "Order confirmed and token disabled.";
    }


    @Override
    public String updateUserEmailConfirmStatus(String token) throws SQLException {
        // First, confirm the user account by unlocking it
        userDao.confirmUserByEmailConfirmationLocked(token);

        // Then, disable the token after confirmation
       userDao.disableTokenAfterUserConfirmation(token);

        return "User confirmed and token disabled.";
    }

}