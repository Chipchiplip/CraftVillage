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

@WebServlet(name = "OrderDetailControl", urlPatterns = {"/order-detail"})
public class OrderDetailControl extends HttpServlet {
    private final OrderService orderService;

    public OrderDetailControl() {
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
            // Lấy orderId từ request parameter
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Invalid order ID");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int orderId = Integer.parseInt(orderIdStr);
            
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Order not found");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Kiểm tra quyền truy cập
            if (user.getRole() != 1 && user.getRole() != 2 && order.getUserID() != user.getUserID()) {
                request.setAttribute("error", "You don't have permission to view this order");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Nếu là seller, chỉ được xem đơn hàng của mình
            if (user.getRole() == 2) {
                // TODO: Thêm logic kiểm tra đơn hàng có thuộc về seller không
                // Hiện tại chưa có thông tin seller trong Order entity
            }

            // Lưu thông tin đơn hàng vào request
            request.setAttribute("order", order);
            request.setAttribute("user", user);
            
            // Chuyển hướng đến trang chi tiết đơn hàng
            request.getRequestDispatcher("order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID format");
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
        return "Order Detail Controller";
    }
}