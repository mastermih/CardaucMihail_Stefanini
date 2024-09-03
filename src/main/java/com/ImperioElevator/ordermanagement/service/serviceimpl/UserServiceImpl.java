package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.service.UserSevice;

import java.sql.SQLException;

//TODO BLEAAAA sa nu uiti sa adaugi user_roles dupa ce se creaiaza userul
public class UserServiceImpl implements UserSevice {
    public final UserDaoImpl userDao;
    public final EmailServiceImpl emailService;
    public UserServiceImpl(UserDaoImpl userDao, EmailServiceImpl emailService) {
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Override
    public Long addNewUser(User user) throws SQLException {
        Long userId = userDao.insert(user); // Insert the user and get the generated user ID
        EmailDetails emailDetails = constructEmailDetails(user);
        String emailResult = emailService.sendConfirmationMail(emailDetails, user.userId().id());
        System.out.println("Email Result: " + emailResult);
        return userId; // Return the generated user ID
    }

    @Override
    public Long confirmUserByEmail(Long id) throws SQLException {
        return userDao.confirmUserByEmailConfirmationLocked(id);
    }

    private EmailDetails constructEmailDetails(User user) {
        // Construct email details based on the order information
        String recipient = "cardaucmihai@gmail.com";
        String subject = "Registration Confirmation";
        String confirmationLink = "http://localhost:3000/sendMail/confirm/" + user.userId().id();
        String messageBody = "Your order with ID " + user.userId().id() + " has been successfully created.\n\n"
                + "Please confirm your order by clicking the link below:\n"
                + confirmationLink;
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject(subject);
        details.setMsgBody(messageBody);
        details.setOrderId(user.userId().id()); // Ensure orderId is included
        return details;
    }
}
