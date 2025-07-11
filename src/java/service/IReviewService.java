/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import entity.CraftVillage.CraftReview;
import entity.Product.ProductReview;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface IReviewService {

    boolean addProductReview(ProductReview review);

    boolean addVillageReview(CraftReview review);

    List<ProductReview> getProductReviews(int productId);

    List<CraftReview> getVillageReviews(int villageId);

    boolean respondToReview(int reviewId, String response, boolean isProductReview);

    double getAverageProductRating(int productId);

    double getAverageVillageRating(int villageId);

    List<CraftReview> getAllVillageReviewByAdmin(int villageID);

    List<ProductReview> getAllProductReviewByAdmin(int productID);

    boolean responseProductReviewByAdmin(int reviewID, String responseText);

    boolean responseVillageReviewByAdmin(int reviewID, String responseText);

    boolean deleteProductReviewByAdmin(int reviewID);

    boolean deleteVillageReviewByAdmin(int reviewID);

    List<ProductReview> searchProductReviewByAdmin(int userID);

    List<CraftReview> searchVillageReviewByAdmin(int userID);

    List<CraftReview> searchVillageReviewToday(int villageID);

    List<ProductReview> searchProductReviewToday(int productID);
    
    // NEW METHODS: Check review eligibility and prevent duplicates
    boolean canUserReviewProduct(int userID, int productID, int orderID);
    boolean hasUserReviewedProduct(int userID, int productID, int orderID);
    List<Integer> getReviewableProductsFromOrder(int userID, int orderID);
    boolean isOrderEligibleForReview(int orderID);
}
