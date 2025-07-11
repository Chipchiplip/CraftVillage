package service;

import entity.CartWishList.Wishlist;
import DAO.WishlistDAO;
import java.util.List;

public interface IWishlistService {
    // ===== BASIC WISHLIST OPERATIONS =====
    List<Wishlist> getAllWishlists();
    Wishlist getWishlistById(int id);
    boolean addWishlist(Wishlist wishlist);
    boolean updateWishlist(Wishlist wishlist);
    boolean deleteWishlist(int id);
    List<Wishlist> getWishlistsByUserId(int userId);
    int getWishListCount(int userID);
    
    // ===== HELPER METHODS =====
    boolean addToWishlist(int userId, int productId);
    boolean removeFromWishlist(int userId, int productId);
    boolean isInWishlist(int userId, int productId);
    
    // ===== NEW METHODS FOR CONTROLLER LAYER =====
    /**
     * Get wishlist with product details - replaces direct DAO call in Controller
     */
    List<WishlistDAO.WishlistWithProduct> getWishlistWithProductDetails(int userId);
    
    /**
     * Debug wishlist data - replaces direct DAO call in Controller
     */
    void debugWishlistData(int userId);
    
    /**
     * Add product to wishlist and remove from cart if specified
     */
    boolean addProductToWishlist(int userId, int productId);
    
    /**
     * Move item from wishlist to cart
     */
    boolean moveItemToCart(int userId, int productId, int wishlistId);
    
    /**
     * Move all wishlist items to cart
     */
    boolean moveAllItemsToCart(int userId);
} 