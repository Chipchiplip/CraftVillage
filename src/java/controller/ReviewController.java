package controller;

import entity.Account.Account;
import entity.Product.ProductReview;
import service.ReviewService;
import service.OrderService;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ReviewController handles all review-related operations
 * Supports product reviews with order validation
 */
@WebServlet(name = "ReviewController", urlPatterns = {"/review"})
public class ReviewController extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ReviewController.class.getName());
    private ReviewService reviewService;
    private OrderService orderService;
    
    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
        orderService = new OrderService();
    }
    
    /**
     * Handles GET requests - Display review forms and review lists
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        try {
            switch (action) {
                case "form":
                    showReviewForm(request, response);
                    break;
                case "check":
                    checkReviewEligibility(request, response);
                    break;
                case "list":
                    listReviews(request, response);
                    break;
                case "reviewable-products":
                    getReviewableProducts(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doGet: " + e.getMessage(), e);
            request.setAttribute("error", "An error occurred while processing your request");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Handles POST requests - Submit reviews
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "submit";
        }
        
        try {
            switch (action) {
                case "submit":
                    submitReview(request, response);
                    break;
                case "submit-from-order":
                    submitReviewFromOrder(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in doPost: " + e.getMessage(), e);
            request.setAttribute("error", "An error occurred while submitting your review");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Show review form for a specific product
     */
    private void showReviewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            
            // Check if user can review this product
            boolean canReview = reviewService.canUserReviewProduct(user.getUserID(), productID, orderID);
            
            request.setAttribute("productID", productID);
            request.setAttribute("orderID", orderID);
            request.setAttribute("canReview", canReview);
            
            if (!canReview) {
                // Get reason why user cannot review
                boolean orderEligible = reviewService.isOrderEligibleForReview(orderID);
                boolean alreadyReviewed = reviewService.hasUserReviewedProduct(user.getUserID(), productID, orderID);
                
                if (!orderEligible) {
                    request.setAttribute("reason", "Order must be delivered and paid before you can leave a review");
                } else if (alreadyReviewed) {
                    request.setAttribute("reason", "You have already reviewed this product");
                } else {
                    request.setAttribute("reason", "This product is not in your order or you don't have permission to review it");
                }
            }
            
            request.getRequestDispatcher("review-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product or order ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
    
    /**
     * Check if user can review products from an order (AJAX endpoint)
     */
    private void checkReviewEligibility(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        response.setContentType("application/json;charset=UTF-8");
        
        if (user == null) {
            response.getWriter().write("{\"error\": \"User not logged in\"}");
            return;
        }
        
        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            
            boolean orderEligible = reviewService.isOrderEligibleForReview(orderID);
            List<Integer> reviewableProducts = reviewService.getReviewableProductsFromOrder(user.getUserID(), orderID);
            
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"orderEligible\": ").append(orderEligible).append(",");
            json.append("\"reviewableProducts\": [");
            
            if (reviewableProducts != null && !reviewableProducts.isEmpty()) {
                for (int i = 0; i < reviewableProducts.size(); i++) {
                    if (i > 0) json.append(",");
                    json.append(reviewableProducts.get(i));
                }
            }
            
            json.append("]}");
            
            response.getWriter().write(json.toString());
            
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"error\": \"Invalid order ID\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking review eligibility: " + e.getMessage(), e);
            response.getWriter().write("{\"error\": \"Server error\"}");
        }
    }
    
    /**
     * Get all reviewable products from an order (AJAX endpoint)
     */
    private void getReviewableProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        response.setContentType("application/json;charset=UTF-8");
        
        if (user == null) {
            response.getWriter().write("{\"error\": \"User not logged in\"}");
            return;
        }
        
        try {
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            
            List<Integer> reviewableProducts = reviewService.getReviewableProductsFromOrder(user.getUserID(), orderID);
            
            StringBuilder json = new StringBuilder();
            json.append("{\"reviewableProducts\": [");
            
            if (reviewableProducts != null && !reviewableProducts.isEmpty()) {
                for (int i = 0; i < reviewableProducts.size(); i++) {
                    if (i > 0) json.append(",");
                    json.append(reviewableProducts.get(i));
                }
            }
            
            json.append("]}");
            
            response.getWriter().write(json.toString());
            
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"error\": \"Invalid order ID\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting reviewable products: " + e.getMessage(), e);
            response.getWriter().write("{\"error\": \"Server error\"}");
        }
    }
    
    /**
     * Submit a basic product review (without order validation)
     */
    private void submitReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");
            
            // Validate input
            if (rating < 1 || rating > 5) {
                request.setAttribute("error", "Rating must be between 1 and 5 stars");
                showReviewForm(request, response);
                return;
            }
            
            if (reviewText == null || reviewText.trim().isEmpty()) {
                request.setAttribute("error", "Review text cannot be empty");
                showReviewForm(request, response);
                return;
            }
            
            // Create review object
            ProductReview review = new ProductReview();
            review.setProductID(productID);
            review.setUserID(user.getUserID());
            review.setRating(rating);
            review.setReviewText(reviewText.trim());
            
            // Submit review
            boolean success = reviewService.addProductReview(review);
            
            if (success) {
                request.setAttribute("message", "Your review has been submitted successfully!");
                response.sendRedirect("product?pid=" + productID + "&reviewSuccess=true");
            } else {
                request.setAttribute("error", "Failed to submit review. Please try again.");
                showReviewForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input data");
            showReviewForm(request, response);
        }
    }
    
    /**
     * Submit product review with order validation
     */
    private void submitReviewFromOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account user = (Account) session.getAttribute("acc");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            int orderID = Integer.parseInt(request.getParameter("orderID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String reviewText = request.getParameter("reviewText");
            
            // Validate input
            if (rating < 1 || rating > 5) {
                request.setAttribute("error", "Rating must be between 1 and 5 stars");
                showReviewForm(request, response);
                return;
            }
            
            if (reviewText == null || reviewText.trim().isEmpty()) {
                request.setAttribute("error", "Review text cannot be empty");
                showReviewForm(request, response);
                return;
            }
            
            // Create review object
            ProductReview review = new ProductReview();
            review.setProductID(productID);
            review.setUserID(user.getUserID());
            review.setRating(rating);
            review.setReviewText(reviewText.trim());
            
            // Submit review with order validation
            boolean success = reviewService.addProductReviewFromOrder(review, orderID);
            
            if (success) {
                request.setAttribute("message", "Your review has been submitted successfully!");
                response.sendRedirect("order-history?orderID=" + orderID + "&reviewSuccess=true");
            } else {
                request.setAttribute("error", "Failed to submit review. You may not have permission to review this product or have already reviewed it.");
                showReviewForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input data");
            showReviewForm(request, response);
        }
    }
    
    /**
     * List reviews for a product
     */
    private void listReviews(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            
            List<ProductReview> reviews = reviewService.getProductReviews(productID);
            double averageRating = reviewService.getAverageProductRating(productID);
            
            request.setAttribute("reviews", reviews);
            request.setAttribute("averageRating", averageRating);
            request.setAttribute("productID", productID);
            
            request.getRequestDispatcher("product-reviews.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
} 