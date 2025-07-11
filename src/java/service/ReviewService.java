/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.ReviewDAO;
import entity.CraftVillage.CraftReview;
import entity.Product.ProductReview;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author ACER
 */
public class ReviewService implements IReviewService {

    private static final Logger LOGGER = Logger.getLogger(ReviewService.class.getName());
    ReviewDAO rDAO = new ReviewDAO();

    @Override
    public boolean addProductReview(ProductReview review) {
        try {
            // Validate input
            if (review == null || review.getUserID() <= 0 || review.getProductID() <= 0) {
                LOGGER.log(Level.WARNING, "Invalid review data provided");
                return false;
            }
            
            if (review.getRating() < 1 || review.getRating() > 5) {
                LOGGER.log(Level.WARNING, "Invalid rating: {0}. Must be between 1-5", review.getRating());
                return false;
            }
            
            if (review.getReviewText() == null || review.getReviewText().trim().isEmpty()) {
                LOGGER.log(Level.WARNING, "Review text cannot be empty");
                return false;
            }
            
            // Use basic add method (without order validation for backward compatibility)
            String query = "INSERT INTO ProductReview (productID, userID, rating, reviewText, reviewDate) VALUES (?, ?, ?, ?, GETDATE())";
            // This would need to be implemented in DAO as a simple add method
            return rDAO.addProductReviewFromOrder(review, 0); // 0 means no order validation
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product review: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Add product review with order validation
     * @param review The product review
     * @param orderID The order ID this review relates to
     * @return true if review was added successfully
     */
    public boolean addProductReviewFromOrder(ProductReview review, int orderID) {
        try {
            // Validate input
            if (review == null || review.getUserID() <= 0 || review.getProductID() <= 0 || orderID <= 0) {
                LOGGER.log(Level.WARNING, "Invalid review or order data provided");
                return false;
            }
            
            if (review.getRating() < 1 || review.getRating() > 5) {
                LOGGER.log(Level.WARNING, "Invalid rating: {0}. Must be between 1-5", review.getRating());
                return false;
            }
            
            if (review.getReviewText() == null || review.getReviewText().trim().isEmpty()) {
                LOGGER.log(Level.WARNING, "Review text cannot be empty");
                return false;
            }
            
            return rDAO.addProductReviewFromOrder(review, orderID);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product review from order: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean addVillageReview(CraftReview review) {
        return rDAO.addVillageReview(review);
    }

    @Override
    public List<ProductReview> getProductReviews(int productId) {
        try {
            if (productId <= 0) {
                LOGGER.log(Level.WARNING, "Invalid product ID: {0}", productId);
                return null;
            }
            return rDAO.getAllProductReviewByAdmin(productId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting product reviews: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<CraftReview> getVillageReviews(int villageId) {
        try {
            if (villageId <= 0) {
                LOGGER.log(Level.WARNING, "Invalid village ID: {0}", villageId);
                return null;
            }
            return rDAO.getAllVillageReviewByAdmin(villageId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting village reviews: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean respondToReview(int reviewId, String response, boolean isProductReview) {
        try {
            if (reviewId <= 0) {
                LOGGER.log(Level.WARNING, "Invalid review ID: {0}", reviewId);
                return false;
            }
            
            if (response == null || response.trim().isEmpty()) {
                LOGGER.log(Level.WARNING, "Response text cannot be empty");
                return false;
            }
            
            if (isProductReview) {
                return rDAO.responseProductReviewByAdmin(reviewId, response);
            } else {
                return rDAO.responseVillageReviewByAdmin(reviewId, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error responding to review: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public double getAverageProductRating(int productId) {
        try {
            if (productId <= 0) {
                LOGGER.log(Level.WARNING, "Invalid product ID: {0}", productId);
                return 0.0;
            }
            
            List<ProductReview> reviews = rDAO.getAllProductReviewByAdmin(productId);
            if (reviews == null || reviews.isEmpty()) {
                return 0.0;
            }
            
            double sum = 0.0;
            for (ProductReview review : reviews) {
                sum += review.getRating();
            }
            
            return sum / reviews.size();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating average product rating: " + e.getMessage(), e);
            return 0.0;
        }
    }

    @Override
    public double getAverageVillageRating(int villageId) {
        try {
            if (villageId <= 0) {
                LOGGER.log(Level.WARNING, "Invalid village ID: {0}", villageId);
                return 0.0;
            }
            
            List<CraftReview> reviews = rDAO.getAllVillageReviewByAdmin(villageId);
            if (reviews == null || reviews.isEmpty()) {
                return 0.0;
            }
            
            double sum = 0.0;
            for (CraftReview review : reviews) {
                sum += review.getRating();
            }
            
            return sum / reviews.size();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error calculating average village rating: " + e.getMessage(), e);
            return 0.0;
        }
    }

    // NEW METHODS: Review eligibility checking
    @Override
    public boolean canUserReviewProduct(int userID, int productID, int orderID) {
        try {
            if (userID <= 0 || productID <= 0 || orderID <= 0) {
                LOGGER.log(Level.WARNING, "Invalid parameters: userID={0}, productID={1}, orderID={2}", 
                          new Object[]{userID, productID, orderID});
                return false;
            }
            return rDAO.canUserReviewProduct(userID, productID, orderID);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if user can review product: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean hasUserReviewedProduct(int userID, int productID, int orderID) {
        try {
            if (userID <= 0 || productID <= 0 || orderID <= 0) {
                LOGGER.log(Level.WARNING, "Invalid parameters: userID={0}, productID={1}, orderID={2}", 
                          new Object[]{userID, productID, orderID});
                return false;
            }
            return rDAO.hasUserReviewedProduct(userID, productID, orderID);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking if user has reviewed product: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Integer> getReviewableProductsFromOrder(int userID, int orderID) {
        try {
            if (userID <= 0 || orderID <= 0) {
                LOGGER.log(Level.WARNING, "Invalid parameters: userID={0}, orderID={1}", 
                          new Object[]{userID, orderID});
                return null;
            }
            return rDAO.getReviewableProductsFromOrder(userID, orderID);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting reviewable products from order: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean isOrderEligibleForReview(int orderID) {
        try {
            if (orderID <= 0) {
                LOGGER.log(Level.WARNING, "Invalid order ID: {0}", orderID);
                return false;
            }
            return rDAO.isOrderEligibleForReview(orderID);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error checking order eligibility: " + e.getMessage(), e);
            return false;
        }
    }

    // Existing admin methods remain unchanged
    @Override
    public List<CraftReview> getAllVillageReviewByAdmin(int villageID) {
        return rDAO.getAllVillageReviewByAdmin(villageID);
    }

    @Override
    public List<ProductReview> getAllProductReviewByAdmin(int productID) {
        return rDAO.getAllProductReviewByAdmin(productID);
    }

    public boolean responseProductReviewByAdmin(int reviewID, String responseText) {
        return rDAO.responseProductReviewByAdmin(reviewID, responseText);
    }

    public boolean responseVillageReviewByAdmin(int reviewID, String responseText) {
        return rDAO.responseVillageReviewByAdmin(reviewID, responseText);
    }

    public boolean deleteProductReviewByAdmin(int reviewID) {
        return rDAO.deleteProductReviewByAdmin(reviewID);
    }

    public boolean deleteVillageReviewByAdmin(int reviewID) {
        return rDAO.deleteVillageReviewByAdmin(reviewID);
    }

    @Override
    public List<ProductReview> searchProductReviewByAdmin(int userID) {
        return rDAO.searchProductReviewByAdmin(userID);
    }

    @Override
    public List<CraftReview> searchVillageReviewByAdmin(int userID) {
        return rDAO.searchVillageReviewByAdmin(userID);
    }

    @Override
    public List<CraftReview> searchVillageReviewToday(int villageID) {
        return rDAO.searchVillageReviewToday(villageID);
    }

    @Override
    public List<ProductReview> searchProductReviewToday(int productID) {
        return rDAO.searchProductReviewToday(productID);
    }
}
