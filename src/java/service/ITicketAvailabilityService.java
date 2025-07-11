package service;

import entity.Ticket.TicketAvailability;
import java.util.Date;
import java.util.List;

/**
 * Service interface for ticket availability operations
 * Focused on availability management and booking operations
 */
public interface ITicketAvailabilityService {
    
    /**
     * Get available dates for a ticket (next 30 days with available slots)
     */
    List<TicketAvailability> getAvailableDatesForTicket(int ticketId);
    
    /**
     * Get availability for specific ticket and date
     */
    TicketAvailability getAvailabilityByTicketAndDate(int ticketId, Date date);
    
    /**
     * Check if tickets are available for specific date and quantity
     */
    boolean checkAvailability(int ticketId, Date date, int quantity);
    
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
    
    /**
     * Batch initialize availability for multiple dates
     */
    boolean batchInitializeAvailability(int ticketId, List<Date> dates, int totalSlots);
    
    /**
     * Get availability statistics for a ticket
     */
    double getAverageOccupancyRate(int ticketId, Date startDate, Date endDate);
    
    /**
     * Get peak demand dates for a ticket
     */
    List<Date> getPeakDemandDates(int ticketId, int daysAhead);
} 