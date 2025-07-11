package controller.cart_order;

import entity.Orders.Order;
import entity.Account.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.OrderService;

@WebServlet(name = "OrderConfirmControl", urlPatterns = {"/order-confirm"})
public class OrderConfirmControl extends HttpServlet {
    private final OrderService orderService;

    public OrderConfirmControl() {
        this.orderService = new OrderService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        // ✅ REFACTORED: Use shared OrderProcessingService to avoid code duplication
        service.OrderProcessingService orderProcessingService = new service.OrderProcessingService();
        
        // Validate authentication
        Account user = orderProcessingService.validateAuthentication(request, response);
        if (user == null) return; // Response already handled

        try {
            // Extract and validate order data
            service.OrderProcessingService.OrderData orderData = 
                orderProcessingService.extractAndValidateOrderData(request);
            
            if (!orderData.isValid()) {
                request.setAttribute("error", orderData.getErrorMessage());
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            // Create order
            int orderId = orderProcessingService.createOrder(orderData.getOrder(), user.getUserID());
            
            if (orderId > 0) {
                // Store orderId in session for subsequent steps
                HttpSession session = request.getSession();
                session.setAttribute("currentOrderId", orderId);
                
                // Redirect after successful order creation
                response.sendRedirect("order-success?orderId=" + orderId);
            } else {
                request.setAttribute("error", "Failed to create order");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Order Confirmation Controller";
    }
}