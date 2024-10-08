package com.ImperioElevator.ordermanagement.service.serviceimpl;

import com.ImperioElevator.ordermanagement.dao.daoimpl.ProductDaoImpl;
import com.ImperioElevator.ordermanagement.dao.daoimpl.UserDaoImpl;
import com.ImperioElevator.ordermanagement.entity.EmailDetails;
import com.ImperioElevator.ordermanagement.entity.LoginRequest;
import com.ImperioElevator.ordermanagement.entity.User;
import com.ImperioElevator.ordermanagement.exception.AccountLockedException;
import com.ImperioElevator.ordermanagement.exception.DoublePasswordVerificationException;
import com.ImperioElevator.ordermanagement.exception.LoginUserNotFoundException;
import com.ImperioElevator.ordermanagement.exception.ThisUserAlreadyExistException;
import com.ImperioElevator.ordermanagement.security.JwtService;
import com.ImperioElevator.ordermanagement.service.UserSevice;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserSevice {
    public final UserDaoImpl userDao;
    public final EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    private  final JwtService jwtService;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public UserServiceImpl(UserDaoImpl userDao, EmailServiceImpl emailService, JwtService jwtService,BCryptPasswordEncoder encoder, AuthenticationManager authManager) {
        this.userDao = userDao;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.encoder = encoder;
        this.authManager = authManager;
    }

//Encoding the user password useing BCryptPasswordEncoder
    @Override
    public Long addNewUser(User user) throws SQLException {
        String encryptedPassword = encoder.encode(user.password());
        User encriptedUser = new User(
                user.userId(),
                user.name(),
                user.email(),
                encryptedPassword,  // Set the encrypted password
                user.phoneNumber(),
                user.image(),
                user.roles(),
                user.accountNonLocked()
        );
        String name = user.name().name();
        String email = user.email().email();
        boolean exists = userDao.registrationThatUserCredentialsAlreadyExists(name, email);
        if(exists){
            throw new ThisUserAlreadyExistException("User with this email or user name already exists " + email + " " + name);
        }
        Long userId = userDao.insert(encriptedUser);
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
        emailService.sendConfirmationMail(emailDetails, userId);
        return userId;
    }
    //ToDo ths method have wrong name and have to be fixed (not only the name)
    @Override
    public Long createUserUnauthorized(User user, String verifyPassword) throws SQLException {
        String encryptedPassword = encoder.encode(user.password());
        User encriptedUser = new User(
                user.userId(),
                user.name(),
                user.email(),
                encryptedPassword,  // Set the encrypted password
                user.phoneNumber(),
                user.image(),
                user.roles(),
                user.accountNonLocked()
        );
        String name = user.name().name();
        String email = user.email().email();

         boolean exists = userDao.registrationThatUserCredentialsAlreadyExists(name, email);
        System.out.println("Checking if user exists: " + exists);
        logger.info("User exists check for {} with email {}: {}", name, email, exists);
        if(exists){
            throw new ThisUserAlreadyExistException("User with this email or user name already exists " + email + " " + name);
        }
        Long userId = userDao.createUserUnauthorized(encriptedUser);
        // Fetch all role ids for users roles
        List<Long> roleIds = user.roles().stream()
                .map(role -> {
                    try {
                        return userDao.getRoleIdFromRoleName("USER");
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
        emailService.sendConfirmationMail(emailDetails, userId);
        return userId;
    }

//    @Override
//    public List<User> getManagementUsers() throws SQLException {
//        return userDao.getManagementUsers();
//    }

    @Override
    public Long addImageForUSer(Long userId, String  image) throws SQLException {
        return userDao.addImageForUSer(userId, image);
    }

    @Override
    public String getUserImage(Long userId) throws SQLException {
        return userDao.getUserImage(userId);
    }


    // This is for user Profile selection by token
    @Override
    public User fiendUserByToken(String token) throws SQLException {
        return userDao.fiendUserByToken(token);
    }

    @Override
    public User getUserProfile(Long userId) throws SQLException {
        return userDao.findById(userId);
    }

    @Override
    public Long updateUser(User user) throws SQLException {
        return userDao.update(user);
    }

    @Override
    public String verifyUser(LoginRequest loginRequest) throws SQLException {
        Long userId = null;
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
            if (authentication.isAuthenticated()) {
                // Extract UserDetails from the Authentication object
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                List<String> roles = userDetails.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .toList();

                userId = userDao.findUserIdByEmail(userDetails.getUsername());

                return jwtService.generateToken(userDetails.getUsername(), roles, userDetails.isAccountNonLocked(), userId);
            }
        } catch (AccountLockedException aEx) {
            throw new AccountLockedException(userId);
        } catch (LoginUserNotFoundException e) {
            // Here I can throw only the email but just wanted to test also with the message
            throw new LoginUserNotFoundException(loginRequest.email() + " Invalid login credentials");
        }
        return "";
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