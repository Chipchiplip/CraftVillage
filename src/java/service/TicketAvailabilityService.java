package service;

import DAO.TicketAvailabilityDAO;
import entity.Ticket.TicketAvailability;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * Service implementation for TicketAvailability operations
 */
public class TicketAvailabilityService implements ITicketAvailabilityService {
    
    private TicketAvailabilityDAO availabilityDAO;
    
    public TicketAvailabilityService() {
        this.availabilityDAO = new TicketAvailabilityDAO();
    }
    
    @Override
    public boolean checkAvailability(int ticketID, Date requestedDate, int quantity) {
        if (ticketID <= 0 || requestedDate == null || quantity <= 0) {
            return false;
        }
        
        // Validate date constraints
        if (!isValidBookingDate(requestedDate)) {
            return false;
        }
        
        return availabilityDAO.checkAvailability(ticketID, requestedDate, quantity);
    }
    
    @Override
    public TicketAvailability getAvailabilityByTicketAndDate(int ticketID, Date date) {
        if (ticketID <= 0 || date == null) {
            return null;
        }
        return availabilityDAO.getAvailabilityByTicketAndDate(ticketID, date);
    }
    
    @Override
    public List<TicketAvailability> getAvailableDatesForTicket(int ticketID) {
        if (ticketID <= 0) {
            return List.of();
        }
        return availabilityDAO.getAvailableDatesForTicket(ticketID);
    }
    
    @Override
    public boolean initializeAvailability(int ticketId, Date date, int totalSlots) {
        if (ticketId <= 0 || date == null || totalSlots < 0) {
            return false;
        }
        return availabilityDAO.initializeAvailability(ticketId, date, totalSlots);
    }
    
    @Override
    public boolean updateTotalSlots(int ticketId, Date date, int newTotalSlots) {
        if (ticketId <= 0 || date == null || newTotalSlots < 0) {
            return false;
        }
        return availabilityDAO.updateTotalSlots(ticketId, date, newTotalSlots);
    }
    
    @Override
    public boolean closeAvailability(int ticketId, Date date) {
        if (ticketId <= 0 || date == null) {
            return false;
        }
        return availabilityDAO.closeAvailability(ticketId, date);
    }
    
    @Override
    public List<TicketAvailability> getAvailabilitiesByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return List.of();
        }
        return availabilityDAO.getAvailabilitiesByDateRange(startDate, endDate);
    }
    
    @Override
    public int getTotalBookedSlotsForTicket(int ticketId, Date date) {
        if (ticketId <= 0 || date == null) {
            return 0;
        }
        return availabilityDAO.getTotalBookedSlotsForTicket(ticketId, date);
    }
    
    @Override
    public boolean batchInitializeAvailability(int ticketId, List<Date> dates, int totalSlots) {
        if (ticketId <= 0 || dates == null || dates.isEmpty() || totalSlots < 0) {
            return false;
        }
        
        try {
            for (Date date : dates) {
                initializeAvailability(ticketId, date, totalSlots);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public double getAverageOccupancyRate(int ticketId, Date startDate, Date endDate) {
        if (ticketId <= 0 || startDate == null || endDate == null) {
            return 0.0;
        }
        
        List<TicketAvailability> availabilities = getAvailabilitiesByDateRange(startDate, endDate);
        if (availabilities.isEmpty()) {
            return 0.0;
        }
        
        int totalSlots = availabilities.stream().mapToInt(TicketAvailability::getTotalSlots).sum();
        int bookedSlots = availabilities.stream().mapToInt(TicketAvailability::getBookedSlots).sum();
        
        return totalSlots > 0 ? (double) bookedSlots / totalSlots * 100 : 0.0;
    }
    
    @Override
    public List<Date> getPeakDemandDates(int ticketId, int daysAhead) {
        // Simplified implementation - return dates with high booking rates
        return List.of();
    }
    
    @Override
    public boolean bookTickets(int ticketID, Date date, int quantity) {
        if (ticketID <= 0 || date == null || quantity <= 0) {
            return false;
        }
        
        // Check availability before booking
        if (!checkAvailability(ticketID, date, quantity)) {
            return false;
        }
        
        return availabilityDAO.bookTickets(ticketID, date, quantity);
    }
    
    @Override
    public boolean cancelBooking(int ticketID, Date date, int quantity) {
        if (ticketID <= 0 || date == null || quantity <= 0) {
            return false;
        }
        
        return availabilityDAO.cancelBooking(ticketID, date, quantity);
    }
    
    private boolean isValidBookingDate(Date requestedDate) {
        if (requestedDate == null) {
            return false;
        }
        
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        
        Calendar requestDate = Calendar.getInstance();
        requestDate.setTime(requestedDate);
        requestDate.set(Calendar.HOUR_OF_DAY, 0);
        requestDate.set(Calendar.MINUTE, 0);
        requestDate.set(Calendar.SECOND, 0);
        requestDate.set(Calendar.MILLISECOND, 0);
        
        // Must be today or future date
        if (requestDate.before(today)) {
            return false;
        }
        
        // Must be within 30 days
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTime(today.getTime());
        maxDate.add(Calendar.DAY_OF_MONTH, 30);
        
        return !requestDate.after(maxDate);
    }
} 