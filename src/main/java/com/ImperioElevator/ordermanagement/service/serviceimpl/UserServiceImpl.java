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

        Long userId = userDao.insert(user); // Insert the user and get the generated user ID
        Long roleId = userDao.getRoleIdFromRoleName(user.role().name());

        EmailDetails emailDetails = constructEmailDetails(user);
        String emailResult = emailService.sendConfirmationMail(emailDetails, user.userId().id());
        System.out.println("Email Result: " + emailResult);
        userDao.giveToUserARole(userId, roleId);
        return userId;
    }

    public class RoleMapper {

        public static Long getRoleId(Role role) {
            switch (role) {
                case USER:
                    return 1L;
                case ADMIN:
                    return 2L;
                case MANAGER:
                    return 3L;
                case SALESMAN:
                    return 4L;
                default:
                    throw new IllegalArgumentException("Unknown role: " + role);
            }
        }
    }


    // Userul confirma entitatea din email
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
                + "Please confirm your user creation  by clicking the link below:\n"
                + confirmationLink;
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject(subject);
        details.setMsgBody(messageBody);
        details.setOrderId(user.userId().id()); // Ensure orderId is included
        return details;
    }
}
