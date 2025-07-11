package service;

import entity.CartWishList.CartTicket;
import java.util.Date;
import java.util.List;

/**
 * Service interface for cart ticket operations
 * Handles adding, removing, and managing tickets in shopping cart
 */
public interface ICartTicketService {
    
    /**
     * Add ticket to cart with date validation
     */
    boolean addTicketToCart(int cartId, int ticketId, Date ticketDate, int quantity);
    
    /**
     * Remove ticket from cart
     */
    boolean removeTicketFromCart(int cartTicketId);
    
    /**
     * Update ticket quantity in cart
     */
    boolean updateTicketQuantity(int cartTicketId, int newQuantity);
    
    /**
     * Get all cart tickets by cart ID
     */
    List<CartTicket> getCartTicketsByCartId(int cartId);
    
    /**
     * Get specific cart ticket by details
     */
    CartTicket getCartTicketByDetails(int cartId, int ticketId, Date ticketDate);
    
    /**
     * Clear all tickets from cart
     */
    boolean clearCartTickets(int cartId);
    
    /**
     * Get total ticket count in cart
     */
    int getCartTicketCount(int cartId);
    
    /**
     * Get total value of tickets in cart
     */
    double getCartTicketTotalValue(int cartId);
    
    /**
     * Get tickets for specific date range in cart
     */
    List<CartTicket> getCartTicketsByDateRange(int cartId, Date startDate, Date endDate);
    
    /**
     * Validate and add ticket to cart with business rules
     */
    boolean addTicketToCartWithValidation(int cartId, int ticketId, Date ticketDate, int quantity);
    
    /**
     * Get cart ticket summary information
     */
    String getCartTicketSummary(int cartId);
    
    /**
     * Check if cart has any tickets
     */
    boolean hasTicketsInCart(int cartId);
    
    /**
     * Get earliest ticket date in cart
     */
    Date getEarliestTicketDateInCart(int cartId);
    
    /**
     * Get latest ticket date in cart  
     */
    Date getLatestTicketDateInCart(int cartId);

    // Compatibility methods with different naming conventions
    List<CartTicket> getCartTicketsByCartID(int cartId);
    boolean removeCartTicket(int cartTicketId);
    boolean updateCartTicketQuantity(int cartTicketId, int newQuantity);
} 