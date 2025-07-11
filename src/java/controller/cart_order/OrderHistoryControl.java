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

@WebServlet(name = "OrderHistoryControl", urlPatterns = {"/order-history"})
public class OrderHistoryControl extends HttpServlet {
    private final OrderService orderService;

    public OrderHistoryControl() {
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

        try {
            List<Order> orders;
            
            // Lấy danh sách đơn hàng dựa trên vai trò người dùng
            if (user.getRole() == 1) { // Admin
                orders = orderService.getAllOrders();
            } else if (user.getRole() == 2) { // Seller
                orders = orderService.getOrdersBySellerId(user.getUserID());
            } else { // Customer
                orders = orderService.getOrdersByUserId(user.getUserID());
            }

            // Lưu danh sách đơn hàng vào request
            request.setAttribute("orders", orders);
            request.setAttribute("user", user);
            
            // Chuyển hướng đến trang lịch sử đơn hàng
            request.getRequestDispatcher("order-history.jsp").forward(request, response);
            
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
        return "Order History Controller";
    }
} 