package service;

/**
 *
 * @author Pc
 */

import entity.CartWishList.Cart;
import entity.Product.Product;
import java.util.Date;

public interface ICartService {
    // ===== PRODUCT CART METHODS =====
    void addToCart(Cart cart, Product product, int quantity);
    void updateCartItem(Cart cart, int productId, int quantity);
    void removeCartItem(Cart cart, int productId);
    double getTotalPrice(Cart cart);
    double calculateTotalWithDiscount(Cart cart);
    double calculateTotalDiscount(Cart cart);
    void applyDiscount(Cart cart, String discountCode);
    void clearCart(int userId);

    // ===== NEW METHODS FOR CONTROLLER LAYER =====
    /**
     * Get cart by user ID - replaces direct DAO call in Controller
     */
    Cart getCartByUser(int userId);
    
    /**
     * Get or create cart for user - handles cart creation logic
     */
    Cart getOrCreateCart(int userId);
    
    /**
     * Add product to cart using user ID and product ID
     */
    boolean addProductToCart(int userId, int productId, int quantity);
    
    /**
     * Update cart item quantity using user ID
     */
    boolean updateCartItemQuantity(int userId, int productId, int quantity);
    
    /**
     * Remove cart item using user ID
     */
    boolean removeCartItemByUser(int userId, int productId);

    // ===== TICKET METHODS =====
    void addTicketToCart(Cart cart, int ticketId, Date ticketDate, int quantity);
    void updateTicketInCart(Cart cart, int cartTicketId, int quantity);
    void removeTicketFromCart(Cart cart, int cartTicketId);
    
    /**
     * Add ticket to cart using user ID
     */
    boolean addTicketToCartByUser(int userId, int ticketId, Date ticketDate, int quantity);
    
    /**
     * Update ticket in cart using user ID
     */
    boolean updateTicketQuantityByUser(int userId, int cartTicketId, int quantity);
    
    /**
     * Remove ticket from cart using user ID
     */
    boolean removeTicketByUser(int userId, int cartTicketId);
}