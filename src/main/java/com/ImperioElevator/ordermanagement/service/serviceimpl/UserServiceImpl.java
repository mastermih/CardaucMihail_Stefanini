package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.Order;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.enumobects.Role;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
//ToDo add token confirmation in email
public class UserServiceImpl implements UserSevice {
    public final UserDaoImpl userDao;
    public final EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public UserServiceImpl(UserDaoImpl userDao, EmailServiceImpl emailService) {
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Override
    public Long addNewUser(User user) throws SQLException {
        Long userId = userDao.insert(user);

        // Fetch all role ids for users roles
        List<Long> roleIds = user.roles().stream()
                .map(role -> {
                    try {
                        return userDao.getRoleIdFromRoleName(role.name());
                    } catch (SQLException e) {
                        logger.error("Error fetching role ID for role: {}", role.name(), e);
                        throw new RuntimeException(e);
                    }
                })
                .toList();  // Collect role ids

        // Assign multiple roles to the user
        userDao.giveToUserRoles(userId, roleIds);

        // Generate confirmation token and send the email
        String token = userDao.getTheConfirmationToken(userId);
        EmailDetails emailDetails = constructEmailDetails(user, token);
        String emailResult = emailService.sendConfirmationMail(emailDetails, userId);
        System.out.println("Email Result: " + emailResult);

        return userId;
    }



    private EmailDetails constructEmailDetails(User user, String token) {
        // Construct email details based on the order information
        String recipient = "cardaucmihai@gmail.com";
        String subject = "Registration Confirmation";
        String confirmationLink = "http://localhost:3000/sendMail/confirm/user/" + token;
        String messageBody = "Dear " + user.name().name() + ",\n\n"
                + "Please confirm your user creation  by clicking the link below:\n"
                + confirmationLink;
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject(subject);
        details.setMsgBody(messageBody);
        details.setOrderId(user.userId().id());
        return details;
    }
}
