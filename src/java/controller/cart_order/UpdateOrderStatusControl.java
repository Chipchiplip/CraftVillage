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

@WebServlet(name = "UpdateOrderStatusControl", urlPatterns = {"/update-order-status"})
public class UpdateOrderStatusControl extends HttpServlet {
    private final OrderService orderService;

    public UpdateOrderStatusControl() {
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

        // Chỉ admin và seller mới được phép cập nhật trạng thái đơn hàng
        if (user.getRole() != 1 && user.getRole() != 2) {
            request.setAttribute("error", "You don't have permission to update order status");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String newStatus = request.getParameter("status");

            // Validate trạng thái mới
            if (newStatus == null || newStatus.trim().isEmpty()) {
                request.setAttribute("error", "Invalid order status");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Kiểm tra đơn hàng có tồn tại không
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Nếu là seller, chỉ được cập nhật đơn hàng của mình
            if (user.getRole() == 2) {
                // TODO: Thêm logic kiểm tra đơn hàng có thuộc về seller không
                // Hiện tại chưa có thông tin seller trong Order entity
            }

            // Cập nhật trạng thái đơn hàng
            boolean success = orderService.updateOrderStatus(orderId, newStatus);
            
            if (success) {
                session.setAttribute("message", "Order status updated successfully");
            } else {
                session.setAttribute("error", "Failed to update order status");
            }

            // Chuyển hướng về trang quản lý đơn hàng
            response.sendRedirect("order-management");
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
        return "Update Order Status Controller";
    }
}