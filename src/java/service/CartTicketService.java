package service;

import DAO.CartTicketDAO;
import DAO.TicketAvailabilityDAO;
import entity.CartWishList.CartTicket;
import java.util.Date;
import java.util.List;

/**
 * Service implementation for CartTicket operations
 */
public class CartTicketService implements ICartTicketService {
    
    private CartTicketDAO cartTicketDAO;
    private TicketAvailabilityDAO availabilityDAO;
    
    public CartTicketService() {
        this.cartTicketDAO = new CartTicketDAO();
        this.availabilityDAO = new TicketAvailabilityDAO();
    }
    
    @Override
    public List<CartTicket> getCartTicketsByCartId(int cartId) {
        return cartTicketDAO.getCartTicketsByCartId(cartId);
    }
    
    // Alias method for compatibility
    public List<CartTicket> getCartTicketsByCartID(int cartId) {
        return getCartTicketsByCartId(cartId);
    }
    
    @Override
    public boolean addTicketToCart(int cartId, int ticketId, Date ticketDate, int quantity) {
        // Validate input parameters
        if (cartId <= 0 || ticketId <= 0 || quantity <= 0 || ticketDate == null) {
            return false;
        }
        
        // Check if date is in the future
        Date today = new Date();
        if (ticketDate.before(today)) {
            return false;
        }
        
        // Check availability before adding
        if (!availabilityDAO.checkAvailability(ticketId, ticketDate, quantity)) {
            return false;
        }
        
        // Add to cart using DAO
        return cartTicketDAO.addTicketToCart(cartId, ticketId, ticketDate, quantity);
    }
    
    @Override
    public boolean updateTicketQuantity(int cartTicketId, int newQuantity) {
        if (cartTicketId <= 0 || newQuantity <= 0) {
            return false;
        }
        
        // TODO: Add availability check for the new quantity
        // This would require getting the ticket info first, then checking availability
        
        return cartTicketDAO.updateTicketQuantity(cartTicketId, newQuantity);
    }
    
    // Alias method for compatibility
    public boolean updateCartTicketQuantity(int cartTicketId, int newQuantity) {
        return updateTicketQuantity(cartTicketId, newQuantity);
    }
    
    @Override
    public boolean removeTicketFromCart(int cartTicketId) {
        if (cartTicketId <= 0) {
            return false;
        }
        return cartTicketDAO.removeTicketFromCart(cartTicketId);
    }
    
    // Alias method for compatibility
    public boolean removeCartTicket(int cartTicketId) {
        return removeTicketFromCart(cartTicketId);
    }
    
    @Override
    public boolean clearCartTickets(int cartId) {
        if (cartId <= 0) {
            return false;
        }
        return cartTicketDAO.clearCartTickets(cartId);
    }
    
    @Override
    public CartTicket getCartTicketByDetails(int cartId, int ticketId, Date ticketDate) {
        if (cartId <= 0 || ticketId <= 0 || ticketDate == null) {
            return null;
        }
        return cartTicketDAO.getCartTicketByDetails(cartId, ticketId, ticketDate);
    }
    
    @Override
    public int getCartTicketCount(int cartId) {
        if (cartId <= 0) {
            return 0;
        }
        return cartTicketDAO.getCartTicketCount(cartId);
    }
    
    @Override
    public double getCartTicketTotalValue(int cartId) {
        if (cartId <= 0) {
            return 0.0;
        }
        return cartTicketDAO.getCartTicketTotalValue(cartId);
    }
    
    @Override
    public List<CartTicket> getCartTicketsByDateRange(int cartId, Date startDate, Date endDate) {
        if (cartId <= 0 || startDate == null || endDate == null) {
            return List.of();
        }
        return cartTicketDAO.getCartTicketsByDateRange(cartId, startDate, endDate);
    }
    
    /**
     * Validate ticket booking constraints
     */
    public boolean validateTicketBooking(int ticketId, Date ticketDate, int quantity) {
        // Check date is not in the past
        Date today = new Date();
        if (ticketDate.before(today)) {
            return false;
        }
        
        // Check date is within 30 days
        long thirtyDaysInMillis = 30L * 24 * 60 * 60 * 1000;
        Date maxBookingDate = new Date(today.getTime() + thirtyDaysInMillis);
        if (ticketDate.after(maxBookingDate)) {
            return false;
        }
        
        // Check availability
        return availabilityDAO.checkAvailability(ticketId, ticketDate, quantity);
    }
    
    @Override
    public boolean addTicketToCartWithValidation(int cartId, int ticketId, Date ticketDate, int quantity) {
        if (!validateTicketBooking(ticketId, ticketDate, quantity)) {
            return false;
        }
        return addTicketToCart(cartId, ticketId, ticketDate, quantity);
    }

    @Override
    public boolean hasTicketsInCart(int cartId) {
        return getCartTicketCount(cartId) > 0;
    }

    @Override
    public Date getEarliestTicketDateInCart(int cartId) {
        List<CartTicket> tickets = getCartTicketsByCartId(cartId);
        return tickets.stream()
                .map(CartTicket::getTicketDate)
                .min(java.util.Date::compareTo)
                .orElse(null);
    }

    @Override
    public Date getLatestTicketDateInCart(int cartId) {
        List<CartTicket> tickets = getCartTicketsByCartId(cartId);
        return tickets.stream()
                .map(CartTicket::getTicketDate)
                .max(java.util.Date::compareTo)
                .orElse(null);
    }

    @Override
    public String getCartTicketSummary(int cartId) {
        List<CartTicket> tickets = getCartTicketsByCartId(cartId);
        if (tickets.isEmpty()) return "Cart has no tickets";
        StringBuilder sb = new StringBuilder();
        sb.append("Tickets in cart: ").append(tickets.size()).append("\n");
        for (CartTicket t : tickets) {
            sb.append("- Ticket ").append(t.getTicketId())
              .append(" on ").append(new java.text.SimpleDateFormat("yyyy-MM-dd").format(t.getTicketDate()))
              .append(" x ").append(t.getQuantity()).append("\n");
        }
        sb.append("Total quantity: ").append(getCartTicketCount(cartId)).append("\n");
        sb.append("Total value: ").append(getCartTicketTotalValue(cartId));
        return sb.toString();
    }
} 