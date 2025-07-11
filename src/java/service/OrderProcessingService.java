package service;

import entity.Orders.Order;
import entity.Account.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Shared service for order processing logic to avoid code duplication
 * between CheckoutServlet and OrderConfirmControl
 */
public class OrderProcessingService {
    
    private final OrderService orderService;
    
    public OrderProcessingService() {
        this.orderService = new OrderService();
    }
    
    /**
     * Validate user authentication
     */
    public Account validateAuthentication(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login");
            return null;
        }
        
        return user;
    }
    
    /**
     * Extract and validate order parameters from request
     */
    public OrderData extractAndValidateOrderData(HttpServletRequest request) {
        String shippingAddress = request.getParameter("shippingAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String paymentMethod = request.getParameter("paymentMethod");
        
        // Validate required fields
        if (shippingAddress == null || shippingAddress.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return new OrderData(null, "Please fill in all required fields");
        }
        
        // Create order object
        Order order = new Order();
        order.setShippingAddress(shippingAddress.trim());
        order.setPhoneNumber(phoneNumber.trim());
        order.setEmail(email.trim());
        order.setPaymentMethod(paymentMethod.trim());
        order.setStatus("pending");
        order.setPaymentStatus("pending");
        
        return new OrderData(order, null);
    }
    
    /**
     * Create order and return order ID
     */
    public int createOrder(Order order, int userId) throws Exception {
        order.setUserID(userId);
        return orderService.createOrder(order);
    }
    
    /**
     * Data class to hold order extraction results
     */
    public static class OrderData {
        private final Order order;
        private final String errorMessage;
        
        public OrderData(Order order, String errorMessage) {
            this.order = order;
            this.errorMessage = errorMessage;
        }
        
        public Order getOrder() {
            return order;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public boolean isValid() {
            return order != null && errorMessage == null;
        }
    }
} 