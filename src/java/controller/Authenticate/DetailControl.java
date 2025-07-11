/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Authenticate;

import entity.Product.Product;
import entity.Product.ProductCategory;
import entity.Account.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import service.ProductService;
import service.ReviewService;
import service.OrderService;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author ACER
 */
@WebServlet(name = "DetailControl", urlPatterns = {"/detail"})
public class DetailControl extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(DetailControl.class.getName());

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
        
        ProductService productService = new ProductService();
        ReviewService reviewService = new ReviewService();
        OrderService orderService = new OrderService();
        
        String id = request.getParameter("pid");
        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        // Lấy thông tin sản phẩm
        Product product = productService.getProductByID(id);
        if (product == null) {
            response.sendRedirect("home");
            return;
        }

        // Set thông tin sản phẩm
        request.setAttribute("detail", product);
        request.setAttribute("cid", product.getCategoryID());
        request.setAttribute("productName", product.getName());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("description", product.getDescription());
        request.setAttribute("img", product.getMainImageUrl());

        // CHECK REVIEW ELIGIBILITY
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user != null) {
            try {
                int productId = Integer.parseInt(id);
                int userId = user.getUserID();
                
                // Get all user's orders to check if any contain this product and are eligible for review
                List<entity.Orders.Order> userOrders = orderService.getOrdersByUserId(userId);
                
                boolean canReview = false;
                int eligibleOrderId = -1;
                String reviewMessage = "";
                
                for (entity.Orders.Order order : userOrders) {
                    // Check if order is eligible (delivered AND paid)
                    if (order.isEligibleForReview()) {
                        // Check if this product is in this order
                        List<entity.Orders.OrderDetail> orderDetails = order.getOrderDetails();
                        if (orderDetails == null || orderDetails.isEmpty()) {
                            // Load order details if not already loaded
                            orderDetails = orderService.getOrderDetailsByOrderId(order.getOrderID());
                        }
                        
                        boolean productInOrder = false;
                        for (entity.Orders.OrderDetail detail : orderDetails) {
                            if (detail.getProductID() == productId) {
                                productInOrder = true;
                                break;
                            }
                        }
                        
                        if (productInOrder) {
                            // Check if user has already reviewed this product from this order
                            boolean alreadyReviewed = reviewService.hasUserReviewedProduct(userId, productId, order.getOrderID());
                            
                            if (!alreadyReviewed) {
                                canReview = true;
                                eligibleOrderId = order.getOrderID();
                                break; // Found an eligible order
                            }
                        }
                    }
                }
                
                // Set review-related attributes
                request.setAttribute("canUserReviewProduct", canReview);
                if (canReview) {
                    request.setAttribute("orderIDForReview", eligibleOrderId);
                } else {
                    // Determine why user cannot review
                    boolean hasAnyOrderWithProduct = false;
                    boolean hasDeliveredPaidOrder = false;
                    boolean alreadyReviewedAll = false;
                    
                    for (entity.Orders.Order order : userOrders) {
                        List<entity.Orders.OrderDetail> orderDetails = order.getOrderDetails();
                        if (orderDetails == null || orderDetails.isEmpty()) {
                            orderDetails = orderService.getOrderDetailsByOrderId(order.getOrderID());
                        }
                        
                        for (entity.Orders.OrderDetail detail : orderDetails) {
                            if (detail.getProductID() == productId) {
                                hasAnyOrderWithProduct = true;
                                
                                if (order.isEligibleForReview()) {
                                    hasDeliveredPaidOrder = true;
                                    // Check if already reviewed
                                    if (reviewService.hasUserReviewedProduct(userId, productId, order.getOrderID())) {
                                        alreadyReviewedAll = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    
                    if (!hasAnyOrderWithProduct) {
                        reviewMessage = "You need to purchase this product before you can review it.";
                    } else if (!hasDeliveredPaidOrder) {
                        reviewMessage = "You can only review products from orders that have been delivered and paid.";
                    } else if (alreadyReviewedAll) {
                        reviewMessage = "You have already reviewed this product.";
                    } else {
                        reviewMessage = "You cannot review this product at this time.";
                    }
                    
                    request.setAttribute("reviewMessage", reviewMessage);
                }
                
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid product ID format: " + id, e);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error checking review eligibility for product " + id, e);
                // Set default values to prevent errors
                request.setAttribute("canUserReviewProduct", false);
                request.setAttribute("reviewMessage", "Unable to check review eligibility at this time.");
            }
        } else {
            // User not logged in
            request.setAttribute("canUserReviewProduct", false);
            request.setAttribute("reviewMessage", "Please log in to leave a review.");
        }

        // Lấy danh sách tất cả sản phẩm
        List<Product> listP = productService.getAllProducts();
        request.setAttribute("listP", listP);

        // Lấy sản phẩm cùng danh mục
        List<Product> listPP = productService.getProductByCategoryID(String.valueOf(product.getCategoryID()));
        request.setAttribute("listPP", listPP);

        // Lấy tất cả categories
        List<ProductCategory> listC = productService.getAllCategory();
        request.setAttribute("listCC", listC);

        // Tìm tên category của sản phẩm hiện tại
        String categoryName = "";
        for (ProductCategory c : listC) {
            if (c.getCategoryID() == product.getCategoryID()) {
                categoryName = c.getCategoryName();
                break;
            }
        }
        request.setAttribute("categoryName", categoryName);

        // Lấy top 5 sản phẩm mới nhất
        List<Product> list5 = productService.getTop5NewestProducts();
        request.setAttribute("list5", list5);

        request.getRequestDispatcher("Detail.jsp").forward(request, response);
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
        return "Short description";
    }// </editor-fold>

} 