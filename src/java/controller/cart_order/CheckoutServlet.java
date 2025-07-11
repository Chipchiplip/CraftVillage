package controller.cart_order;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import entity.CartWishList.Cart;
import entity.CartWishList.CartItem;
import entity.Product.Category;
import entity.Orders.Order;
import entity.Product.Product;
import entity.Account.Account;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import DAO.OrderDAO;
import service.ProductService;
import service.IProductService;
import service.CartService;
import service.EmailService;
import service.OrderService;
import service.IOrderService;
import service.ICartService;
import service.IProductService;
import service.ProductService;

/**
 *
 * @author Pc
 */
@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final IOrderService orderService;
    private final ICartService cartService;
    private final ProductService productService;
    private OrderDAO orderDAO = new OrderDAO();

    public CheckoutServlet() {
        this.orderService = new OrderService();
        this.cartService = new CartService();
        this.productService = new ProductService();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Lấy giỏ hàng từ session
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || cart.getItems().isEmpty()) {
                request.setAttribute("error", "Your cart is empty");
                request.getRequestDispatcher("cart.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin từ form
            String shippingAddress = request.getParameter("shippingAddress");
            String phoneNumber = request.getParameter("phoneNumber");
            String email = request.getParameter("email");
            String paymentMethod = request.getParameter("paymentMethod");

            // Validate dữ liệu
            if (shippingAddress == null || shippingAddress.trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                paymentMethod == null || paymentMethod.trim().isEmpty()) {
                request.setAttribute("error", "Please fill in all required fields");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            // Tạo đơn hàng mới
            Order order = new Order();
            order.setUserID(account.getUserID());
            order.setShippingAddress(shippingAddress);
            order.setPhoneNumber(phoneNumber);
            order.setEmail(email);
            order.setPaymentMethod(paymentMethod);
            order.setStatus("pending");
            order.setPaymentStatus("pending");

            // Lưu đơn hàng
            int orderId = orderService.createOrder(order);
            
            if (orderId > 0) {
                // Thêm chi tiết đơn hàng
                List<CartItem> items = cart.getItems();
                for (CartItem item : items) {
                    orderService.addOrderDetail(
                        orderId,
                        item.getProductID(),
                        item.getQuantity(),
                        item.getPrice()
                    );
                }

                // Xóa giỏ hàng sau khi đặt hàng thành công
                cartService.clearCart(account.getUserID());
                session.removeAttribute("cart");

                // Lưu orderId vào session
                session.setAttribute("currentOrderId", orderId);
                
                // Chuyển hướng sau khi đặt hàng thành công
                // VNPay payment integration removed
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Checkout Controller";
    }// </editor-fold>

}
