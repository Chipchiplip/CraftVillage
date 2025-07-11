/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

// import entity.Orders.TicketCode;
// import entity.Orders.TicketOrder;
// import entity.Ticket.Ticket;
import entity.Ticket.VillageTicket;
import entity.Ticket.TicketAvailability;
import entity.CartWishList.CartTicket;
import entity.Ticket.Ticket;
import java.math.BigDecimal;
import java.util.Date;
import entity.Ticket.TicketType;
import java.util.List;

/**
 * Service interface for comprehensive ticket operations
 * Combines village ticket, availability, and cart operations
 */
public interface ITicketService {
    
    // ======================= VILLAGE TICKET OPERATIONS =======================
    List<Ticket> getAllTicketActive();
    /**
     * Get ticket by ID with full information
     */
    VillageTicket getTicketById(int ticketId);
    
    /**
     * Get all tickets for a specific village
     */
    List<VillageTicket> getTicketsByVillageId(int villageId);
    
    /**
     * Get all active tickets across all villages
     */
    List<VillageTicket> getAllActiveTickets();
    
    /**
     * Get all ticket types
     */
    List<TicketType> getAllTicketTypes();
    
    /**
     * Get tickets by price range
     */
    List<VillageTicket> getTicketsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Insert new village ticket
     */
    boolean insertVillageTicket(VillageTicket ticket);
    
    /**
     * Update village ticket
     */
    boolean updateVillageTicket(VillageTicket ticket);
    
    /**
     * Update ticket price
     */
    boolean updateTicketPrice(int ticketId, BigDecimal newPrice);
    
    /**
     * Deactivate ticket (soft delete)
     */
    boolean deactivateTicket(int ticketId);
    
    /**
     * Check if ticket exists for village and type combination
     */
    boolean ticketExistsForVillageAndType(int villageId, int typeId);
    
    // ======================= AVAILABILITY OPERATIONS =======================
    
    /**
     * Get available dates for a ticket (next 30 days)
     */
    List<TicketAvailability> getAvailableDatesForTicket(int ticketId);
    
    /**
     * Get availability for specific ticket and date
     */
    TicketAvailability getAvailabilityByTicketAndDate(int ticketId, Date date);
    
    /**
     * Check if tickets are available for specific date and quantity
     */
    boolean checkTicketAvailability(int ticketId, Date date, int quantity);
    
    /**
     * Book tickets (reduce available slots)
     */
    boolean bookTickets(int ticketId, Date date, int quantity);
    
    /**
     * Cancel booking (increase available slots)
     */
    boolean cancelBooking(int ticketId, Date date, int quantity);
    
    /**
     * Initialize availability for new date
     */
    boolean initializeAvailability(int ticketId, Date date, int totalSlots);
    
    /**
     * Update total slots for specific date
     */
    boolean updateTotalSlots(int ticketId, Date date, int newTotalSlots);
    
    /**
     * Close availability for specific date
     */
    boolean closeAvailability(int ticketId, Date date);
    
    /**
     * Get availabilities by date range
     */
    List<TicketAvailability> getAvailabilitiesByDateRange(Date startDate, Date endDate);
    
    /**
     * Get total booked slots for ticket on specific date
     */
    int getTotalBookedSlotsForTicket(int ticketId, Date date);
    
    // ======================= CART OPERATIONS =======================
    
    /**
     * Add ticket to cart with availability validation
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
    
    // ======================= BUSINESS LOGIC OPERATIONS =======================
    
    /**
     * Add ticket to cart with comprehensive validation
     * Checks date validity, availability, and business rules
     */
    boolean addTicketToCartWithValidation(int cartId, int ticketId, Date ticketDate, int quantity);
    
    /**
     * Get ticket booking summary for date range
     */
    List<TicketAvailability> getTicketBookingSummary(int ticketId, Date startDate, Date endDate);
    
    /**
     * Validate ticket date (must be within 30 days and not in past)
     */
    boolean isValidTicketDate(Date ticketDate);
    
    /**
     * Get recommended dates for ticket (dates with good availability)
     */
    List<Date> getRecommendedDatesForTicket(int ticketId, int daysAhead);
    
    /**
     * Process cart ticket reservation during checkout
     */
    boolean processCartTicketReservation(int cartId);
    boolean createTicketByAdmin(Ticket ticket);
    boolean updateTicketByAdmin(Ticket ticket);
    boolean deleteTicketByAdmin(int ticketID);
    List<Ticket> getTickeReportByAdmin(int status);
    String getTicketNameByID(int typeID);
    List<Ticket> searchTicketByAdmin(int status, int villageID);
    List<TicketType> getAllTicketType();
}
