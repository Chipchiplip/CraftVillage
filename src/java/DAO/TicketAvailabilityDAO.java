package DAO;

import entity.Ticket.TicketAvailability;
import context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for TicketAvailability table
 * Manages daily ticket availability with booking and cancellation functionality
 */
public class TicketAvailabilityDAO {

    /**
     * Get available dates for a ticket (next 30 days with available slots)
     */
    public List<TicketAvailability> getAvailableDatesForTicket(int ticketId) {
        List<TicketAvailability> availabilities = new ArrayList<>();
        String sql = "SELECT availabilityID, ticketID, availableDate, totalSlots, bookedSlots, " +
                     "availableSlots, status, createdDate, updatedDate " +
                     "FROM TicketAvailability " +
                     "WHERE ticketID = ? AND status = 1 AND availableDate >= CAST(GETDATE() AS DATE) " +
                     "AND availableDate <= DATEADD(DAY, 30, CAST(GETDATE() AS DATE)) " +
                     "AND availableSlots > 0 " +
                     "ORDER BY availableDate";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                TicketAvailability availability = new TicketAvailability();
                availability.setAvailabilityId(rs.getInt("availabilityID"));
                availability.setTicketId(rs.getInt("ticketID"));
                availability.setAvailableDate(rs.getDate("availableDate"));
                availability.setTotalSlots(rs.getInt("totalSlots"));
                availability.setBookedSlots(rs.getInt("bookedSlots"));
                availability.setAvailableSlots(rs.getInt("availableSlots"));
                availability.setStatus(rs.getInt("status"));
                availability.setCreatedDate(rs.getTimestamp("createdDate"));
                availability.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                availabilities.add(availability);
            }
        } catch (SQLException e) {
            System.err.println("Error getting available dates for ticket: " + e.getMessage());
        }
        return availabilities;
    }

    /**
     * Get availability for specific ticket and date
     */
    public TicketAvailability getAvailabilityByTicketAndDate(int ticketId, Date date) {
        String sql = "SELECT availabilityID, ticketID, availableDate, totalSlots, bookedSlots, " +
                     "availableSlots, status, createdDate, updatedDate " +
                     "FROM TicketAvailability " +
                     "WHERE ticketID = ? AND availableDate = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            st.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                TicketAvailability availability = new TicketAvailability();
                availability.setAvailabilityId(rs.getInt("availabilityID"));
                availability.setTicketId(rs.getInt("ticketID"));
                availability.setAvailableDate(rs.getDate("availableDate"));
                availability.setTotalSlots(rs.getInt("totalSlots"));
                availability.setBookedSlots(rs.getInt("bookedSlots"));
                availability.setAvailableSlots(rs.getInt("availableSlots"));
                availability.setStatus(rs.getInt("status"));
                availability.setCreatedDate(rs.getTimestamp("createdDate"));
                availability.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                return availability;
            }
        } catch (SQLException e) {
            System.err.println("Error getting availability by ticket and date: " + e.getMessage());
        }
        return null;
    }

    /**
     * Check if tickets are available for specific date and quantity
     */
    public boolean checkAvailability(int ticketId, Date date, int quantity) {
        String sql = "SELECT availableSlots FROM TicketAvailability " +
                     "WHERE ticketID = ? AND availableDate = ? AND status = 1";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            st.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int availableSlots = rs.getInt("availableSlots");
                return availableSlots >= quantity;
            } else {
                // If no record exists, create one with default 20 slots
                initializeAvailability(ticketId, date, 20);
                return quantity <= 20;
            }
        } catch (SQLException e) {
            System.err.println("Error checking availability: " + e.getMessage());
            return false;
        }
    }

    /**
     * Book tickets (reduce available slots)
     */
    public boolean bookTickets(int ticketId, Date date, int quantity) {
        String sql = "UPDATE TicketAvailability " +
                     "SET bookedSlots = bookedSlots + ?, updatedDate = GETDATE() " +
                     "WHERE ticketID = ? AND availableDate = ? AND availableSlots >= ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quantity);
            st.setInt(2, ticketId);
            st.setDate(3, new java.sql.Date(date.getTime()));
            st.setInt(4, quantity);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error booking tickets: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancel booking (increase available slots)
     */
    public boolean cancelBooking(int ticketId, Date date, int quantity) {
        String sql = "UPDATE TicketAvailability " +
                     "SET bookedSlots = bookedSlots - ?, updatedDate = GETDATE() " +
                     "WHERE ticketID = ? AND availableDate = ? AND bookedSlots >= ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quantity);
            st.setInt(2, ticketId);
            st.setDate(3, new java.sql.Date(date.getTime()));
            st.setInt(4, quantity);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error canceling booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initialize availability for new date
     */
    public boolean initializeAvailability(int ticketId, Date date, int totalSlots) {
        String sql = "INSERT INTO TicketAvailability (ticketID, availableDate, totalSlots, bookedSlots, status, createdDate) " +
                     "VALUES (?, ?, ?, 0, 1, GETDATE())";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            st.setDate(2, new java.sql.Date(date.getTime()));
            st.setInt(3, totalSlots);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error initializing availability: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update total slots for specific date
     */
    public boolean updateTotalSlots(int ticketId, Date date, int newTotalSlots) {
        String sql = "UPDATE TicketAvailability " +
                     "SET totalSlots = ?, updatedDate = GETDATE() " +
                     "WHERE ticketID = ? AND availableDate = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, newTotalSlots);
            st.setInt(2, ticketId);
            st.setDate(3, new java.sql.Date(date.getTime()));

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating total slots: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close availability for specific date (set status to 0)
     */
    public boolean closeAvailability(int ticketId, Date date) {
        String sql = "UPDATE TicketAvailability " +
                     "SET status = 0, updatedDate = GETDATE() " +
                     "WHERE ticketID = ? AND availableDate = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            st.setDate(2, new java.sql.Date(date.getTime()));

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error closing availability: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get availabilities for date range
     */
    public List<TicketAvailability> getAvailabilitiesByDateRange(Date startDate, Date endDate) {
        List<TicketAvailability> availabilities = new ArrayList<>();
        String sql = "SELECT availabilityID, ticketID, availableDate, totalSlots, bookedSlots, " +
                     "availableSlots, status, createdDate, updatedDate " +
                     "FROM TicketAvailability " +
                     "WHERE availableDate BETWEEN ? AND ? " +
                     "ORDER BY availableDate, ticketID";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setDate(1, new java.sql.Date(startDate.getTime()));
            st.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                TicketAvailability availability = new TicketAvailability();
                availability.setAvailabilityId(rs.getInt("availabilityID"));
                availability.setTicketId(rs.getInt("ticketID"));
                availability.setAvailableDate(rs.getDate("availableDate"));
                availability.setTotalSlots(rs.getInt("totalSlots"));
                availability.setBookedSlots(rs.getInt("bookedSlots"));
                availability.setAvailableSlots(rs.getInt("availableSlots"));
                availability.setStatus(rs.getInt("status"));
                availability.setCreatedDate(rs.getTimestamp("createdDate"));
                availability.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                availabilities.add(availability);
            }
        } catch (SQLException e) {
            System.err.println("Error getting availabilities by date range: " + e.getMessage());
        }
        return availabilities;
    }

    /**
     * Get total booked slots for specific ticket and date
     */
    public int getTotalBookedSlotsForTicket(int ticketId, Date date) {
        String sql = "SELECT bookedSlots FROM TicketAvailability " +
                     "WHERE ticketID = ? AND availableDate = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            st.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("bookedSlots");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total booked slots: " + e.getMessage());
        }
        return 0;
    }
} 