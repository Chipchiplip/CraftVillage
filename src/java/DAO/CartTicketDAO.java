package DAO;

import entity.CartWishList.CartTicket;
import entity.Ticket.VillageTicket;
import context.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Access Object for CartTicket operations
 * Manages ticket items in shopping cart with availability validation
 */
public class CartTicketDAO {

    /**
     * Add ticket to cart with date validation
     */
    public boolean addTicketToCart(int cartId, int ticketId, Date ticketDate, int quantity) {
        // First check if item already exists
        CartTicket existingItem = getCartTicketByDetails(cartId, ticketId, ticketDate);
        
        if (existingItem != null) {
            // Update existing item
            return updateTicketQuantity(existingItem.getCartTicketId(), existingItem.getQuantity() + quantity);
        } else {
            // Insert new item
            String sql = "INSERT INTO CartTicket (cartID, ticketID, quantity, ticketDate, createdDate) " +
                         "VALUES (?, ?, ?, ?, GETDATE())";

            try (Connection connection = DBContext.getConnection();
                 PreparedStatement st = connection.prepareStatement(sql)) {
                st.setInt(1, cartId);
                st.setInt(2, ticketId);
                st.setInt(3, quantity);
                st.setDate(4, new java.sql.Date(ticketDate.getTime()));

                int rowsAffected = st.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Error adding ticket to cart: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Remove ticket from cart
     */
    public boolean removeTicketFromCart(int cartTicketId) {
        String sql = "DELETE FROM CartTicket WHERE itemID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartTicketId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error removing ticket from cart: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update ticket quantity in cart
     */
    public boolean updateTicketQuantity(int cartTicketId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeTicketFromCart(cartTicketId);
        }

        String sql = "UPDATE CartTicket SET quantity = ?, updatedDate = GETDATE() WHERE itemID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, newQuantity);
            st.setInt(2, cartTicketId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating ticket quantity: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all cart tickets by cart ID with full information
     */
    public List<CartTicket> getCartTicketsByCartId(int cartId) {
        List<CartTicket> cartTickets = new ArrayList<>();
        String sql = "SELECT ct.itemID, ct.cartID, ct.ticketID, ct.quantity, " +
                     "ct.ticketDate, ct.createdDate, ct.updatedDate, " +
                     "vt.price, cv.villageName, tt.typeName, tt.ageRange " +
                     "FROM CartTicket ct " +
                     "JOIN VillageTicket vt ON ct.ticketID = vt.ticketID " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE ct.cartID = ? " +
                     "ORDER BY ct.createdDate DESC";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                CartTicket cartTicket = new CartTicket();
                cartTicket.setCartTicketId(rs.getInt("itemID"));
                cartTicket.setCartId(rs.getInt("cartID"));
                cartTicket.setTicketId(rs.getInt("ticketID"));
                cartTicket.setQuantity(rs.getInt("quantity"));
                cartTicket.setTicketDate(rs.getDate("ticketDate"));
                cartTicket.setCreatedDate(rs.getTimestamp("createdDate"));
                cartTicket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Set price and additional info
                cartTicket.setPrice(rs.getDouble("price"));
                cartTicket.setVillageName(rs.getString("villageName"));
                cartTicket.setTicketTypeName(rs.getString("typeName"));
                cartTicket.setAgeRange(rs.getString("ageRange"));
                
                cartTickets.add(cartTicket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cart tickets: " + e.getMessage());
        }
        return cartTickets;
    }

    /**
     * Get specific cart ticket by details
     */
    public CartTicket getCartTicketByDetails(int cartId, int ticketId, Date ticketDate) {
        String sql = "SELECT ct.itemID, ct.cartID, ct.ticketID, ct.quantity, " +
                     "ct.ticketDate, ct.createdDate, ct.updatedDate, " +
                     "vt.price, cv.villageName, tt.typeName, tt.ageRange " +
                     "FROM CartTicket ct " +
                     "JOIN VillageTicket vt ON ct.ticketID = vt.ticketID " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE ct.cartID = ? AND ct.ticketID = ? AND ct.ticketDate = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);
            st.setInt(2, ticketId);
            st.setDate(3, new java.sql.Date(ticketDate.getTime()));
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                CartTicket cartTicket = new CartTicket();
                cartTicket.setCartTicketId(rs.getInt("itemID"));
                cartTicket.setCartId(rs.getInt("cartID"));
                cartTicket.setTicketId(rs.getInt("ticketID"));
                cartTicket.setQuantity(rs.getInt("quantity"));
                cartTicket.setTicketDate(rs.getDate("ticketDate"));
                cartTicket.setCreatedDate(rs.getTimestamp("createdDate"));
                cartTicket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Set price and additional info
                cartTicket.setPrice(rs.getDouble("price"));
                cartTicket.setVillageName(rs.getString("villageName"));
                cartTicket.setTicketTypeName(rs.getString("typeName"));
                cartTicket.setAgeRange(rs.getString("ageRange"));
                
                return cartTicket;
            }
        } catch (SQLException e) {
            System.err.println("Error getting cart ticket by details: " + e.getMessage());
        }
        return null;
    }

    /**
     * Check ticket availability before adding to cart
     */
    public boolean checkTicketAvailability(int ticketId, Date date, int quantity) {
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
                // If no availability record exists, assume default 20 slots available
                return quantity <= 20;
            }
        } catch (SQLException e) {
            System.err.println("Error checking ticket availability: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clear all tickets from cart
     */
    public boolean clearCartTickets(int cartId) {
        String sql = "DELETE FROM CartTicket WHERE cartID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected >= 0; // Return true even if no items to delete
        } catch (SQLException e) {
            System.err.println("Error clearing cart tickets: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total ticket count in cart
     */
    public int getCartTicketCount(int cartId) {
        String sql = "SELECT ISNULL(SUM(quantity), 0) as totalCount FROM CartTicket WHERE cartID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("totalCount");
            }
        } catch (SQLException e) {
            System.err.println("Error getting cart ticket count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get total value of tickets in cart
     */
    public double getCartTicketTotalValue(int cartId) {
        String sql = "SELECT ISNULL(SUM(ct.quantity * vt.price), 0) as totalValue " +
                     "FROM CartTicket ct " +
                     "JOIN VillageTicket vt ON ct.ticketID = vt.ticketID " +
                     "WHERE ct.cartID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getDouble("totalValue");
            }
        } catch (SQLException e) {
            System.err.println("Error getting cart ticket total value: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Get tickets for specific date range in cart
     */
    public List<CartTicket> getCartTicketsByDateRange(int cartId, Date startDate, Date endDate) {
        List<CartTicket> cartTickets = new ArrayList<>();
        String sql = "SELECT ct.itemID, ct.cartID, ct.ticketID, ct.quantity, " +
                     "ct.ticketDate, ct.createdDate, ct.updatedDate, " +
                     "vt.price, cv.villageName, tt.typeName, tt.ageRange " +
                     "FROM CartTicket ct " +
                     "JOIN VillageTicket vt ON ct.ticketID = vt.ticketID " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE ct.cartID = ? AND ct.ticketDate BETWEEN ? AND ? " +
                     "ORDER BY ct.ticketDate";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, cartId);
            st.setDate(2, new java.sql.Date(startDate.getTime()));
            st.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                CartTicket cartTicket = new CartTicket();
                cartTicket.setCartTicketId(rs.getInt("itemID"));
                cartTicket.setCartId(rs.getInt("cartID"));
                cartTicket.setTicketId(rs.getInt("ticketID"));
                cartTicket.setQuantity(rs.getInt("quantity"));
                cartTicket.setTicketDate(rs.getDate("ticketDate"));
                cartTicket.setCreatedDate(rs.getTimestamp("createdDate"));
                cartTicket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Set price and additional info
                cartTicket.setPrice(rs.getDouble("price"));
                cartTicket.setVillageName(rs.getString("villageName"));
                cartTicket.setTicketTypeName(rs.getString("typeName"));
                cartTicket.setAgeRange(rs.getString("ageRange"));
                
                cartTickets.add(cartTicket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting cart tickets by date range: " + e.getMessage());
        }
        return cartTickets;
    }
} 