package service;


import DAO.VillageTicketDAO;
import DAO.TicketAvailabilityDAO;
import DAO.CartTicketDAO;
import DAO.ProductDAO;
import entity.Ticket.VillageTicket;
import entity.Ticket.TicketAvailability;
import entity.CartWishList.CartTicket;
import DAO.TicketDAO;
import entity.Orders.TicketCode;
import entity.Orders.TicketOrder;
import entity.Ticket.Ticket;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import entity.Ticket.TicketType;
import java.util.List;
import java.util.ArrayList;

/**
 * Comprehensive ticket service implementation
 * Combines village ticket, availability, and cart operations with business logic
 */

public class TicketService implements ITicketService {
    private ProductDAO pDAO = new ProductDAO();
    private VillageTicketDAO villageTicketDAO;
    private TicketAvailabilityDAO availabilityDAO;
    private CartTicketDAO cartTicketDAO;
    TicketDAO tDAO = new TicketDAO();
    
    public TicketService() {
        this.villageTicketDAO = new VillageTicketDAO();
        this.availabilityDAO = new TicketAvailabilityDAO();
        this.cartTicketDAO = new CartTicketDAO();
    }
    
    public List<Ticket> getTicketsByVillage(int villageId) {
        return tDAO.getTicketsByVillage(villageId);
    }

    public boolean addTicket(Ticket ticket) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public TicketOrder createTicketOrder(TicketOrder order) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean cancelTicketOrder(int orderId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<TicketCode> getTicketCodesByOrderDetail(int detailId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

    @Override
    public List<Ticket> getAllTicketActive() {
        return pDAO.getAllTicketActive();
    }

    public boolean createTicketByAdmin(Ticket ticket) {
        return tDAO.createTicketByAdmin(ticket);
    }

    public boolean updateTicketByAdmin(Ticket ticket) {
        return tDAO.updateTicketByAdmin(ticket);
    }

    public boolean deleteTicketByAdmin(int ticketID) {
        return tDAO.deleteTicketByAdmin(ticketID);
    }

    public List<Ticket> getTickeReportByAdmin(int status) {
        return tDAO.getTickeReportByAdmin(status);
    }

    public String getTicketNameByID(int typeID){
        return tDAO.getTicketNameByID(typeID);
    }

    @Override
    public List<TicketType> getAllTicketType() {
        return tDAO.getAllTicketType();
    }

    
    @Override
    public VillageTicket getTicketById(int ticketId) {
        return villageTicketDAO.getTicketById(ticketId);
    }
    
    @Override
    public List<VillageTicket> getTicketsByVillageId(int villageId) {
        return villageTicketDAO.getTicketsByVillageId(villageId);
    }
    
    @Override
    public List<VillageTicket> getAllActiveTickets() {
        return villageTicketDAO.getAllActiveTickets();
    }
    
    @Override
    public List<TicketType> getAllTicketTypes() {
        return villageTicketDAO.getAllTicketTypes();
    }
    
    @Override
    public List<VillageTicket> getTicketsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return villageTicketDAO.getTicketsByPriceRange(minPrice, maxPrice);
    }
    
    @Override
    public boolean insertVillageTicket(VillageTicket ticket) {
        return villageTicketDAO.insertVillageTicket(ticket);
    }
    
    @Override
    public boolean updateVillageTicket(VillageTicket ticket) {
        return villageTicketDAO.updateVillageTicket(ticket);
    }
    
    @Override
    public boolean updateTicketPrice(int ticketId, BigDecimal newPrice) {
        return villageTicketDAO.updateTicketPrice(ticketId, newPrice);
    }
    
    @Override
    public boolean deactivateTicket(int ticketId) {
        return villageTicketDAO.deactivateTicket(ticketId);
    }
    
    @Override
    public boolean ticketExistsForVillageAndType(int villageId, int typeId) {
        return villageTicketDAO.ticketExistsForVillageAndType(villageId, typeId);
    }
    
    // ======================= AVAILABILITY OPERATIONS =======================
    
    @Override
    public List<TicketAvailability> getAvailableDatesForTicket(int ticketId) {
        return availabilityDAO.getAvailableDatesForTicket(ticketId);
    }
    
    @Override
    public TicketAvailability getAvailabilityByTicketAndDate(int ticketId, Date date) {
        return availabilityDAO.getAvailabilityByTicketAndDate(ticketId, date);
    }
    
    @Override
    public boolean checkTicketAvailability(int ticketId, Date date, int quantity) {
        return availabilityDAO.checkAvailability(ticketId, date, quantity);
    }
    
    @Override
    public boolean bookTickets(int ticketId, Date date, int quantity) {
        return availabilityDAO.bookTickets(ticketId, date, quantity);
    }
    
    @Override
    public boolean cancelBooking(int ticketId, Date date, int quantity) {
        return availabilityDAO.cancelBooking(ticketId, date, quantity);
    }
    
    @Override
    public boolean initializeAvailability(int ticketId, Date date, int totalSlots) {
        return availabilityDAO.initializeAvailability(ticketId, date, totalSlots);
    }
    
    @Override
    public boolean updateTotalSlots(int ticketId, Date date, int newTotalSlots) {
        return availabilityDAO.updateTotalSlots(ticketId, date, newTotalSlots);
    }
    
    @Override
    public boolean closeAvailability(int ticketId, Date date) {
        return availabilityDAO.closeAvailability(ticketId, date);
    }
    
    @Override
    public List<TicketAvailability> getAvailabilitiesByDateRange(Date startDate, Date endDate) {
        return availabilityDAO.getAvailabilitiesByDateRange(startDate, endDate);
    }
    
    @Override
    public int getTotalBookedSlotsForTicket(int ticketId, Date date) {
        return availabilityDAO.getTotalBookedSlotsForTicket(ticketId, date);
    }
    
    // ======================= CART OPERATIONS =======================
    
    @Override
    public boolean addTicketToCart(int cartId, int ticketId, Date ticketDate, int quantity) {
        return cartTicketDAO.addTicketToCart(cartId, ticketId, ticketDate, quantity);
    }
    
    @Override
    public boolean removeTicketFromCart(int cartTicketId) {
        return cartTicketDAO.removeTicketFromCart(cartTicketId);
    }
    
    @Override
    public boolean updateTicketQuantity(int cartTicketId, int newQuantity) {
        return cartTicketDAO.updateTicketQuantity(cartTicketId, newQuantity);
    }
    
    @Override
    public List<CartTicket> getCartTicketsByCartId(int cartId) {
        return cartTicketDAO.getCartTicketsByCartId(cartId);
    }
    
    @Override
    public CartTicket getCartTicketByDetails(int cartId, int ticketId, Date ticketDate) {
        return cartTicketDAO.getCartTicketByDetails(cartId, ticketId, ticketDate);
    }
    
    @Override
    public boolean clearCartTickets(int cartId) {
        return cartTicketDAO.clearCartTickets(cartId);
    }
    
    @Override
    public int getCartTicketCount(int cartId) {
        return cartTicketDAO.getCartTicketCount(cartId);
    }
    
    @Override
    public double getCartTicketTotalValue(int cartId) {
        return cartTicketDAO.getCartTicketTotalValue(cartId);
    }
    
    @Override
    public List<CartTicket> getCartTicketsByDateRange(int cartId, Date startDate, Date endDate) {
        return cartTicketDAO.getCartTicketsByDateRange(cartId, startDate, endDate);
    }
    
    // ======================= BUSINESS LOGIC OPERATIONS =======================
    
    @Override
    public boolean addTicketToCartWithValidation(int cartId, int ticketId, Date ticketDate, int quantity) {
        // Validate date
        if (!isValidTicketDate(ticketDate)) {
            System.err.println("Invalid ticket date: " + ticketDate);
            return false;
        }
        
        // Check availability
        if (!checkTicketAvailability(ticketId, ticketDate, quantity)) {
            System.err.println("Not enough tickets available for date: " + ticketDate);
            return false;
        }
        
        // Add to cart
        return addTicketToCart(cartId, ticketId, ticketDate, quantity);
    }
    
    @Override
    public List<TicketAvailability> getTicketBookingSummary(int ticketId, Date startDate, Date endDate) {
        return getAvailabilitiesByDateRange(startDate, endDate);
    }
    
    @Override
    public boolean isValidTicketDate(Date ticketDate) {
        if (ticketDate == null) return false;
        
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date maxDate = cal.getTime();
        
        // Check if date is not in the past and within 30 days
        return ticketDate.after(today) && ticketDate.before(maxDate);
    }
    
    @Override
    public List<Date> getRecommendedDatesForTicket(int ticketId, int daysAhead) {
        List<Date> recommendedDates = new ArrayList<>();
        List<TicketAvailability> availabilities = getAvailableDatesForTicket(ticketId);
        
        // Filter dates with good availability (more than 10 slots available)
        for (TicketAvailability availability : availabilities) {
            if (availability.getAvailableSlots() > 10) {
                recommendedDates.add(availability.getAvailableDate());
            }
        }
        
        // Limit to requested number of days
        if (recommendedDates.size() > daysAhead) {
            return recommendedDates.subList(0, daysAhead);
        }
        
        return recommendedDates;
    }
    
    @Override
    public boolean processCartTicketReservation(int cartId) {
        List<CartTicket> cartTickets = getCartTicketsByCartId(cartId);
        
        // Reserve all tickets in cart
        for (CartTicket cartTicket : cartTickets) {
            boolean reserved = bookTickets(cartTicket.getTicketId(), 
                                         cartTicket.getTicketDate(), 
                                         cartTicket.getQuantity());
            if (!reserved) {
                // If any reservation fails, rollback previous reservations
                rollbackReservations(cartTickets, cartTicket);
                return false;
            }
        }
        
        // Clear cart after successful reservation
        return clearCartTickets(cartId);
    }
    
    /**
     * Private helper method to rollback ticket reservations
     */
    private void rollbackReservations(List<CartTicket> cartTickets, CartTicket failedTicket) {
        for (CartTicket cartTicket : cartTickets) {
            if (cartTicket.equals(failedTicket)) break;
            
            cancelBooking(cartTicket.getTicketId(), 
                         cartTicket.getTicketDate(), 
                         cartTicket.getQuantity());
        }
    }
    
    /**
     * Get ticket availability with default initialization
     */
    public TicketAvailability getOrCreateAvailability(int ticketId, Date date) {
        TicketAvailability availability = getAvailabilityByTicketAndDate(ticketId, date);
        
        if (availability == null && isValidTicketDate(date)) {
            // Create new availability with default 20 slots
            if (initializeAvailability(ticketId, date, 20)) {
                availability = getAvailabilityByTicketAndDate(ticketId, date);
            }
        }
        
        return availability;
    }
    
    /**
     * Get ticket with pricing information
     */
    public VillageTicket getTicketWithPricing(int ticketId) {
        VillageTicket ticket = getTicketById(ticketId);
        if (ticket != null) {
            // Additional business logic for pricing can be added here
            // For example: apply discounts, seasonal pricing, etc.
        }
        return ticket;
    }

    public List<Ticket> searchTicketByAdmin(int status, int villageID) {
        return tDAO.searchTicketByAdmin(status,villageID);
    }
}
