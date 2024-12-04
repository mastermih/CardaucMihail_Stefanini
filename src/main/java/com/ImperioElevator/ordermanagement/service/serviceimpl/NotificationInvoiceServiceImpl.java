package com.ImperioElevator.ordermanagement.service.serviceimpl;

//@Service
//public class FunctionalNotificationHelperService {
//
//    private final NotificationFactoryImpl notificationFactoryImpl;
//    private final UserServiceImpl userService;
//    private final UserNotificationDaoImpl userNotificationDao;
//    private final NotificationService notificationService;
//
//    public FunctionalNotificationHelperService(NotificationService notificationService,
//                                               UserNotificationDaoImpl userNotificationDao,
//                                               NotificationFactoryImpl notificationFactoryImpl,
//                                               UserServiceImpl userService) {
//        this.notificationFactoryImpl = notificationFactoryImpl;
//        this.userService = userService;
//        this.userNotificationDao = userNotificationDao;
//        this.notificationService = notificationService;
//    }
//
//    // Function to generate the notification message
//    public final Function<Order, String> notificationMessageFunction = order ->
//            "Invoice has been created for the order with ID " + order.orderId();
//
//    // BiFunction to create and insert the notification
//    public final BiFunction<String, ByteArrayResource, Long> createAndInsertNotificationFunction = (message, attachment) -> {
//        Notification notification = notificationFactoryImpl.createInvoiceNotification(message, attachment);
//        try {
//            return notificationService.insert(notification);
//        } catch (SQLException e) {
//            throw new RuntimeException("Failed to insert notification", e);
//        }
//    };
//
//    // Consumer to assign the notification to operators
//    public final BiFunction<Long, List<String>, Void> assignNotificationToOperatorsConsumer = (notificationId, operators) -> {
//        operators.forEach(operatorName -> {
//            try {
//                Long operatorId = userService.userDao.findUserIdByName(operatorName);
//                if (operatorId != null) {
//                    UserNotification userNotification = notificationFactoryImpl.createUserNotificationAssigneeOperatorToOrder(notificationId, operatorId);
//                    userNotificationDao.insertUserNotification(userNotification);
//                } else {
//                    System.err.println("No user ID found for operator: " + operatorName);
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("Failed to assign notification to operator: " + operatorName, e);
//            }
//        });
//        return null; // Void return
//    };
//
//    // Pipeline method to handle the entire notification flow
//    public Long processNotificationWithInvoice(Order order, ByteArrayResource attachment, List<String> operators) {
//        // Generate the notification message
//        String message = notificationMessageFunction.apply(order);
//
//        // Create and insert the notification
//        Long notificationId = createAndInsertNotificationFunction.apply(message, attachment);
//
//        // Assign the notification to operators
//        assignNotificationToOperatorsConsumer.apply(notificationId, operators);
//
//        // Return the order ID
//        return order.orderId().id();
//    }
//}
