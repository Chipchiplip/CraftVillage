package DAO;

import entity.Ticket.VillageTicket;
import entity.Ticket.TicketType;
import context.DBContext;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for VillageTicket table
 * Handles CRUD operations and complex queries for village tickets
 */
public class VillageTicketDAO {

    /**
     * Get all tickets for a specific village with ticket type information
     */
    public List<VillageTicket> getTicketsByVillageId(int villageId) {
        List<VillageTicket> tickets = new ArrayList<>();
        String sql = "SELECT vt.ticketID, vt.villageID, vt.typeID, vt.price, " +
                     "vt.status, vt.createdDate, vt.updatedDate, " +
                     "tt.typeName, tt.ageRange, tt.description as typeDescription, " +
                     "cv.villageName " +
                     "FROM VillageTicket vt " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE vt.villageID = ? AND vt.status = 1 " +
                     "ORDER BY tt.typeID";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, villageId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                VillageTicket ticket = new VillageTicket();
                ticket.setTicketID(rs.getInt("ticketID"));
                ticket.setVillageID(rs.getInt("villageID"));
                ticket.setTypeID(rs.getInt("typeID"));
                ticket.setPrice(rs.getBigDecimal("price"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setCreatedDate(rs.getTimestamp("createdDate"));
                ticket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Additional properties from joins
                ticket.setTypeName(rs.getString("typeName"));
                ticket.setAgeRange(rs.getString("ageRange"));
                ticket.setTypeDescription(rs.getString("typeDescription"));
                ticket.setVillageName(rs.getString("villageName"));
                
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting tickets by village ID: " + e.getMessage());
        }
        return tickets;
    }

    /**
     * Get ticket by ID with full information
     */
    public VillageTicket getTicketById(int ticketId) {
        String sql = "SELECT vt.ticketID, vt.villageID, vt.typeID, vt.price, " +
                     "vt.status, vt.createdDate, vt.updatedDate, " +
                     "tt.typeName, tt.ageRange, tt.description as typeDescription, " +
                     "cv.villageName " +
                     "FROM VillageTicket vt " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE vt.ticketID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                VillageTicket ticket = new VillageTicket();
                ticket.setTicketID(rs.getInt("ticketID"));
                ticket.setVillageID(rs.getInt("villageID"));
                ticket.setTypeID(rs.getInt("typeID"));
                ticket.setPrice(rs.getBigDecimal("price"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setCreatedDate(rs.getTimestamp("createdDate"));
                ticket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Additional properties from joins
                ticket.setTypeName(rs.getString("typeName"));
                ticket.setAgeRange(rs.getString("ageRange"));
                ticket.setTypeDescription(rs.getString("typeDescription"));
                ticket.setVillageName(rs.getString("villageName"));
                
                return ticket;
            }
        } catch (SQLException e) {
            System.err.println("Error getting ticket by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all active tickets across all villages
     */
    public List<VillageTicket> getAllActiveTickets() {
        List<VillageTicket> tickets = new ArrayList<>();
        String sql = "SELECT vt.ticketID, vt.villageID, vt.typeID, vt.price, " +
                     "vt.status, vt.createdDate, vt.updatedDate, " +
                     "tt.typeName, tt.ageRange, tt.description as typeDescription, " +
                     "cv.villageName " +
                     "FROM VillageTicket vt " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE vt.status = 1 " +
                     "ORDER BY cv.villageName, tt.typeID";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                VillageTicket ticket = new VillageTicket();
                ticket.setTicketID(rs.getInt("ticketID"));
                ticket.setVillageID(rs.getInt("villageID"));
                ticket.setTypeID(rs.getInt("typeID"));
                ticket.setPrice(rs.getBigDecimal("price"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setCreatedDate(rs.getTimestamp("createdDate"));
                ticket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Additional properties from joins
                ticket.setTypeName(rs.getString("typeName"));
                ticket.setAgeRange(rs.getString("ageRange"));
                ticket.setTypeDescription(rs.getString("typeDescription"));
                ticket.setVillageName(rs.getString("villageName"));
                
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all active tickets: " + e.getMessage());
        }
        return tickets;
    }

    // Alias method for compatibility
    public VillageTicket getTicketByID(int ticketId) {
        return getTicketById(ticketId);
    }

    // Alias method for compatibility 
    public List<VillageTicket> getTicketsByVillageID(int villageId) {
        return getTicketsByVillageId(villageId);
    }

    // Method to get connection for external use
    public Connection getConnection() throws SQLException {
        return new DBContext().getConnection();
    }

    // Additional compatibility methods
    public List<VillageTicket> getTicketPricingSummary() {
        return getAllActiveTickets(); // Return all active tickets as summary
    }

    public boolean updateVillageTicketPrices(int villageId, BigDecimal adultPrice, BigDecimal childPrice, BigDecimal studentPrice) {
        // Find and update tickets by type for this village
        List<VillageTicket> tickets = getTicketsByVillageId(villageId);
        boolean success = true;
        
        for (VillageTicket ticket : tickets) {
            BigDecimal newPrice = null;
            String typeName = ticket.getTypeName();
            
            if ("Adult".equalsIgnoreCase(typeName)) {
                newPrice = adultPrice;
            } else if ("Child".equalsIgnoreCase(typeName)) {
                newPrice = childPrice;
            } else if ("Student".equalsIgnoreCase(typeName)) {
                newPrice = studentPrice;
            }
            
            if (newPrice != null) {
                success &= updateTicketPrice(ticket.getTicketID(), newPrice);
            }
        }
        
        return success;
    }

    /**
     * Get all ticket types
     */
    public List<TicketType> getAllTicketTypes() {
        List<TicketType> types = new ArrayList<>();
        String sql = "SELECT typeID, typeName, description, ageRange, status, createdDate, updatedDate " +
                     "FROM TicketType WHERE status = 1 ORDER BY typeID";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                TicketType type = new TicketType();
                type.setTypeID(rs.getInt("typeID"));
                type.setTypeName(rs.getString("typeName"));
                type.setDescription(rs.getString("description"));
                type.setAgeRange(rs.getString("ageRange"));
                type.setStatus(rs.getInt("status"));
                type.setCreatedDate(rs.getTimestamp("createdDate"));
                type.setUpdatedDate(rs.getTimestamp("updatedDate"));
                types.add(type);
            }
        } catch (SQLException e) {
            System.err.println("Error getting ticket types: " + e.getMessage());
        }
        return types;
    }

    /**
     * Insert new village ticket
     */
    public boolean insertVillageTicket(VillageTicket ticket) {
        String sql = "INSERT INTO VillageTicket (villageID, typeID, price, status, createdDate) " +
                     "VALUES (?, ?, ?, ?, GETDATE())";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticket.getVillageID());
            st.setInt(2, ticket.getTypeID());
            st.setBigDecimal(3, ticket.getPrice());
            st.setInt(4, ticket.getStatus());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting village ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update village ticket
     */
    public boolean updateVillageTicket(VillageTicket ticket) {
        String sql = "UPDATE VillageTicket SET villageID = ?, typeID = ?, price = ?, " +
                     "status = ?, updatedDate = GETDATE() WHERE ticketID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticket.getVillageID());
            st.setInt(2, ticket.getTypeID());
            st.setBigDecimal(3, ticket.getPrice());
            st.setInt(4, ticket.getStatus());
            st.setInt(5, ticket.getTicketID());

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating village ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update ticket price
     */
    public boolean updateTicketPrice(int ticketId, BigDecimal newPrice) {
        String sql = "UPDATE VillageTicket SET price = ?, updatedDate = GETDATE() WHERE ticketID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setBigDecimal(1, newPrice);
            st.setInt(2, ticketId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating ticket price: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deactivate ticket (soft delete)
     */
    public boolean deactivateTicket(int ticketId) {
        String sql = "UPDATE VillageTicket SET status = 0, updatedDate = GETDATE() WHERE ticketID = ?";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ticketId);

            int rowsAffected = st.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deactivating ticket: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if ticket exists for village and type combination
     */
    public boolean ticketExistsForVillageAndType(int villageId, int typeId) {
        String sql = "SELECT COUNT(*) FROM VillageTicket WHERE villageID = ? AND typeID = ? AND status = 1";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, villageId);
            st.setInt(2, typeId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking ticket existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get tickets by price range
     */
    public List<VillageTicket> getTicketsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<VillageTicket> tickets = new ArrayList<>();
        String sql = "SELECT vt.ticketID, vt.villageID, vt.typeID, vt.price, " +
                     "vt.status, vt.createdDate, vt.updatedDate, " +
                     "tt.typeName, tt.ageRange, tt.description as typeDescription, " +
                     "cv.villageName " +
                     "FROM VillageTicket vt " +
                     "JOIN TicketType tt ON vt.typeID = tt.typeID " +
                     "LEFT JOIN CraftVillage cv ON vt.villageID = cv.villageID " +
                     "WHERE vt.price BETWEEN ? AND ? AND vt.status = 1 " +
                     "ORDER BY vt.price";

        try (Connection connection = DBContext.getConnection();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setBigDecimal(1, minPrice);
            st.setBigDecimal(2, maxPrice);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                VillageTicket ticket = new VillageTicket();
                ticket.setTicketID(rs.getInt("ticketID"));
                ticket.setVillageID(rs.getInt("villageID"));
                ticket.setTypeID(rs.getInt("typeID"));
                ticket.setPrice(rs.getBigDecimal("price"));
                ticket.setStatus(rs.getInt("status"));
                ticket.setCreatedDate(rs.getTimestamp("createdDate"));
                ticket.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                // Additional properties from joins
                ticket.setTypeName(rs.getString("typeName"));
                ticket.setAgeRange(rs.getString("ageRange"));
                ticket.setTypeDescription(rs.getString("typeDescription"));
                ticket.setVillageName(rs.getString("villageName"));
                
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            System.err.println("Error getting tickets by price range: " + e.getMessage());
        }
        return tickets;
    }
} 