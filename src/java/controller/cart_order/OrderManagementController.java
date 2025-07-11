package controller.cart_order;

import entity.Orders.Order;
import entity.Account.Account;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.OrderService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * OrderManagementController - Controller cho quản lý đơn hàng
 * Tuân thủ mô hình MVC: nhận request, gọi Service, forward sang View
 */
//@WebServlet(name = "OrderManagementController", urlPatterns = {"/order-management"})
public class OrderManagementController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderManagementController.class.getName());
    private final OrderService orderService;

    public OrderManagementController() {
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
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listOrders(request, response, user);
                    break;
                case "view":
                    viewOrder(request, response, user);
                    break;
                case "update-status":
                    updateOrderStatus(request, response, user);
                    break;
                case "cancel":
                    cancelOrder(request, response, user);
                    break;
                case "analytics":
                    showAnalytics(request, response, user);
                    break;
                default:
                    listOrders(request, response, user);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in OrderManagementController", e);
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Hiển thị danh sách đơn hàng
     */
    private void listOrders(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        
        List<Order> orders;
        
        // Lấy danh sách đơn hàng dựa trên vai trò người dùng
        if (user.getRole() == 1) { // Admin
            orders = orderService.getAllOrders();
        } else if (user.getRole() == 2) { // Seller
            orders = orderService.getOrdersBySellerId(user.getUserID());
        } else { // Customer
            orders = orderService.getOrdersByUserId(user.getUserID());
        }

        // Set dữ liệu vào request attribute
        request.setAttribute("orders", orders);
        request.setAttribute("user", user);
        
        // Forward sang View
        request.getRequestDispatcher("order-management.jsp").forward(request, response);
    }

    /**
     * Xem chi tiết đơn hàng
     */
    private void viewOrder(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Order ID is required");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderService.getOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Kiểm tra quyền truy cập
            if (user.getRole() != 1 && // Không phải admin
                user.getRole() != 2 && // Không phải seller
                order.getUserID() != user.getUserID()) { // Không phải chủ đơn hàng
                request.setAttribute("error", "Access denied");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute("user", user);
            request.getRequestDispatcher("order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        
        // Chỉ admin và seller mới được cập nhật trạng thái
        if (user.getRole() != 1 && user.getRole() != 2) {
            request.setAttribute("error", "Access denied");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");
        
        if (orderIdStr == null || status == null) {
            request.setAttribute("error", "Order ID and status are required");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            boolean success = orderService.updateOrderStatus(orderId, status);
            
            if (success) {
                request.setAttribute("message", "Order status updated successfully");
            } else {
                request.setAttribute("error", "Failed to update order status");
            }
            
            // Redirect về trang danh sách
            response.sendRedirect("order-management?action=list");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Hủy đơn hàng
     */
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        
        String orderIdStr = request.getParameter("orderId");
        
        if (orderIdStr == null) {
            request.setAttribute("error", "Order ID is required");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderService.getOrderById(orderId);
            
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Kiểm tra quyền hủy đơn hàng
            if (user.getRole() != 1 && // Không phải admin
                order.getUserID() != user.getUserID()) { // Không phải chủ đơn hàng
                request.setAttribute("error", "Access denied");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            boolean success = orderService.cancelOrder(orderId);
            
            if (success) {
                request.setAttribute("message", "Order cancelled successfully");
            } else {
                request.setAttribute("error", "Failed to cancel order");
            }
            
            // Redirect về trang danh sách
            response.sendRedirect("order-management?action=list");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    /**
     * Hiển thị analytics (chỉ admin)
     */
    private void showAnalytics(HttpServletRequest request, HttpServletResponse response, Account user) 
            throws ServletException, IOException {
        
        if (user.getRole() != 1) { // Chỉ admin mới xem được analytics
            request.setAttribute("error", "Access denied");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Lấy dữ liệu analytics từ Service
        var monthlyRevenue = orderService.getRevenueByMonth();
        var yearlyRevenue = orderService.getRevenueByYear();
        var topProducts = orderService.getTopSellingProducts();

        // Set dữ liệu vào request attribute
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("yearlyRevenue", yearlyRevenue);
        request.setAttribute("topProducts", topProducts);
        request.setAttribute("user", user);
        
        // Forward sang View
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