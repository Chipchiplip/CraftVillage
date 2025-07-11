package controller.cart_order;

import DAO.OrderDAO;
import entity.Orders.Order;
import entity.Account.Account;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.OrderService;

@WebServlet(name = "OrderManagementControl", urlPatterns = {"/order-management"})
public class OrderManagementControl extends HttpServlet {
    private final OrderService orderService;

    public OrderManagementControl() {
        this.orderService = new OrderService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

        try {
            switch (action) {
                case "view":
                    viewOrders(request, response, user);
                    break;
                case "update":
                    updateOrderStatus(request, response);
                    break;
                case "cancel":
                    cancelOrder(request, response);
                    break;
                case "analytics":
                    viewAnalytics(request, response);
                    break;
                default:
                    response.sendRedirect("home");
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void viewOrders(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        List<Order> orders;
        
        if (user.getRole() == 1) { // Admin
            orders = orderService.getAllOrders();
        } else if (user.getRole() == 2) { // Seller
            orders = orderService.getOrdersBySellerId(user.getUserID());
        } else { // Customer
            orders = orderService.getOrdersByUserId(user.getUserID());
        }

        request.setAttribute("orders", orders);
        request.getRequestDispatcher("order-management.jsp").forward(request, response);
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("status");

        boolean success = orderService.updateOrderStatus(orderId, newStatus);
        
        if (success) {
            request.setAttribute("message", "Order status updated successfully");
        } else {
            request.setAttribute("error", "Failed to update order status");
        }

        response.sendRedirect("order-management");
    }

    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        
        boolean success = orderService.cancelOrder(orderId);
        
        if (success) {
            request.setAttribute("message", "Order cancelled successfully");
        } else {
            request.setAttribute("error", "Failed to cancel order");
        }

        response.sendRedirect("order-management");
    }

    private void viewAnalytics(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Map<String, Double> monthlyRevenue = orderService.getRevenueByMonth();
        Map<String, Double> yearlyRevenue = orderService.getRevenueByYear();
        List<Map<String, Object>> topProducts = orderService.getTopSellingProducts();

        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("yearlyRevenue", yearlyRevenue);
        request.setAttribute("topProducts", topProducts);
        request.getRequestDispatcher("order-analytics.jsp").forward(request, response);
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
        return "Order Management Controller";
    }
}