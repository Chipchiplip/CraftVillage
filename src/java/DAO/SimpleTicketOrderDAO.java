package DAO;

import entity.Orders.TicketOrder;
import entity.Orders.TicketOrderDetail;
import entity.Orders.TicketCode;
import entity.Ticket.VillageTicket;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import context.DBContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple DAO class for TicketOrder operations
 * Compatible with basic SQL Server stored procedures
 */
public class SimpleTicketOrderDAO {
    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;

    public SimpleTicketOrderDAO() {
        try {
            this.connection = new DBContext().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(SimpleTicketOrderDAO.class.getName()).log(Level.SEVERE, "Failed to get database connection.", ex);
        }
    }

    /**
     * Create a new ticket order manually (without complex JSON parsing)
     */
    public int createTicketOrder(int userID, int villageID, String customerName, 
                               String customerPhone, String customerEmail, 
                               String paymentMethod, String note, 
                               Map<Integer, Integer> ticketQuantities) {
        
        int orderID = 0;
        
        try {
            connection.setAutoCommit(false);
            
            // Calculate total price and quantity
            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalQuantity = 0;
            
            VillageTicketDAO villageTicketDAO = new VillageTicketDAO();
            
            for (Map.Entry<Integer, Integer> entry : ticketQuantities.entrySet()) {
                int ticketID = entry.getKey();
                int quantity = entry.getValue();
                
                if (quantity > 0) {
                    VillageTicket ticket = villageTicketDAO.getTicketByID(ticketID);
                    if (ticket != null && ticket.getVillageID() == villageID) {
                        totalPrice = totalPrice.add(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
                        totalQuantity += quantity;
                    }
                }
            }
            
            if (totalQuantity == 0) {
                connection.rollback();
                return 0;
            }
            
            // Create order
            String orderSql = "INSERT INTO TicketOrder (userID, villageID, totalPrice, totalQuantity, " +
                             "customerName, customerPhone, customerEmail, paymentMethod, note) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userID);
                ps.setInt(2, villageID);
                ps.setBigDecimal(3, totalPrice);
                ps.setInt(4, totalQuantity);
                ps.setString(5, customerName);
                ps.setString(6, customerPhone);
                ps.setString(7, customerEmail);
                ps.setString(8, paymentMethod);
                ps.setString(9, note);
                
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    orderID = rs.getInt(1);
                }
            }
            
            if (orderID == 0) {
                connection.rollback();
                return 0;
            }
            
            // Create order details and ticket codes
            for (Map.Entry<Integer, Integer> entry : ticketQuantities.entrySet()) {
                int ticketID = entry.getKey();
                int quantity = entry.getValue();
                
                if (quantity > 0) {
                    VillageTicket ticket = villageTicketDAO.getTicketByID(ticketID);
                    if (ticket != null && ticket.getVillageID() == villageID) {
                        
                        // Insert order detail
                        String detailSql = "INSERT INTO TicketOrderDetail (orderID, ticketID, quantity, price, subtotal) " +
                                          "VALUES (?, ?, ?, ?, ?)";
                        
                        int detailID = 0;
                        try (PreparedStatement ps = connection.prepareStatement(detailSql, Statement.RETURN_GENERATED_KEYS)) {
                            ps.setInt(1, orderID);
                            ps.setInt(2, ticketID);
                            ps.setInt(3, quantity);
                            ps.setBigDecimal(4, ticket.getPrice());
                            ps.setBigDecimal(5, ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
                            
                            ps.executeUpdate();
                            
                            ResultSet rs = ps.getGeneratedKeys();
                            if (rs.next()) {
                                detailID = rs.getInt(1);
                            }
                        }
                        
                        // Generate ticket codes
                        if (detailID > 0) {
                            createTicketCodes(detailID, orderID, quantity);
                        }
                    }
                }
            }
            
            connection.commit();
            return orderID;
            
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Create ticket codes for an order detail
     */
    private void createTicketCodes(int detailID, int orderID, int quantity) throws SQLException {
        String sql = "INSERT INTO TicketCode (orderDetailID, ticketCode, expiryDate) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 1; i <= quantity; i++) {
                String ticketCode = generateTicketCode(orderID, i);
                Date expiryDate = new Date(System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000)); // 30 days
                
                ps.setInt(1, detailID);
                ps.setString(2, ticketCode);
                ps.setDate(3, expiryDate);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    /**
     * Generate ticket code
     */
    private String generateTicketCode(int orderID, int counter) {
        return String.format("VT%06d%02d", orderID, counter);
    }

    /**
     * Validate and use ticket using simple stored procedure
     */
    public boolean validateAndUseTicket(String ticketCode, String usedBy) {
        String sql = "{CALL ValidateTicket(?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, ticketCode);
            cs.setString(2, usedBy);
            
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                String result = rs.getString("Result");
                return "SUCCESS".equals(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get user tickets with codes
     */
    public List<TicketCode> getUserTickets(int userID) {
        List<TicketCode> tickets = new ArrayList<>();
        String sql = "SELECT tc.*, tod.*, to.customerName, vt.ticketName " +
                     "FROM TicketCode tc " +
                     "JOIN TicketOrderDetail tod ON tc.orderDetailID = tod.orderDetailID " +
                     "JOIN TicketOrder to ON tod.orderID = to.orderID " +
                     "JOIN VillageTicket vt ON tod.ticketID = vt.ticketID " +
                     "WHERE to.userID = ? " +
                     "ORDER BY tc.createdDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                TicketCode ticket = new TicketCode();
                ticket.setTicketCodeID(rs.getInt("ticketCodeID"));
                ticket.setOrderDetailID(rs.getInt("orderDetailID"));
                ticket.setTicketCode(rs.getString("ticketCode"));
                ticket.setExpiryDate(rs.getTimestamp("expiryDate"));
                ticket.setUsedDate(rs.getTimestamp("usedDate"));
                ticket.setUsedBy(rs.getString("usedBy"));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     * Get ticket order by ID
     */
    public TicketOrder getTicketOrderByID(int orderID) {
        String sql = "SELECT * FROM TicketOrder WHERE orderID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                TicketOrder order = new TicketOrder();
                order.setOrderID(rs.getInt("orderID"));
                order.setUserID(rs.getInt("userID"));
                order.setVillageID(rs.getInt("villageID"));
                order.setTotalPrice(rs.getBigDecimal("totalPrice"));
                order.setTotalQuantity(rs.getInt("totalQuantity"));
                order.setStatus(rs.getInt("status"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setPaymentStatus(rs.getInt("paymentStatus"));
                order.setCustomerName(rs.getString("customerName"));
                order.setCustomerPhone(rs.getString("customerPhone"));
                order.setCustomerEmail(rs.getString("customerEmail"));
                order.setNote(rs.getString("note"));
                order.setCreatedDate(rs.getTimestamp("createdDate"));
                order.setUpdatedDate(rs.getTimestamp("updatedDate"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get ticket orders by user ID
     */
    public List<TicketOrder> getTicketOrdersByUserID(int userID) {
        List<TicketOrder> orders = new ArrayList<>();
        String sql = "SELECT * FROM TicketOrder WHERE userID = ? ORDER BY createdDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                TicketOrder order = new TicketOrder();
                order.setOrderID(rs.getInt("orderID"));
                order.setUserID(rs.getInt("userID"));
                order.setVillageID(rs.getInt("villageID"));
                order.setTotalPrice(rs.getBigDecimal("totalPrice"));
                order.setTotalQuantity(rs.getInt("totalQuantity"));
                order.setStatus(rs.getInt("status"));
                order.setPaymentMethod(rs.getString("paymentMethod"));
                order.setPaymentStatus(rs.getInt("paymentStatus"));
                order.setCustomerName(rs.getString("customerName"));
                order.setCustomerPhone(rs.getString("customerPhone"));
                order.setCustomerEmail(rs.getString("customerEmail"));
                order.setNote(rs.getString("note"));
                order.setCreatedDate(rs.getTimestamp("createdDate"));
                order.setUpdatedDate(rs.getTimestamp("updatedDate"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Update order status
     */
    public boolean updateOrderStatus(int orderID, String status) {
        String sql = "UPDATE TicketOrder SET status = ?, updatedDate = GETDATE() WHERE orderID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update payment status
     */
    public boolean updatePaymentStatus(int orderID, String paymentStatus) {
        String sql = "UPDATE TicketOrder SET paymentStatus = ?, updatedDate = GETDATE() WHERE orderID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, paymentStatus);
            ps.setInt(2, orderID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TicketOrder mapResultSetToTicketOrder(ResultSet rs) throws SQLException {
        TicketOrder order = new TicketOrder();
        order.setOrderID(rs.getInt("orderID"));
        order.setUserID(rs.getInt("userID"));
        order.setVillageID(rs.getInt("villageID"));
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setTotalQuantity(rs.getInt("totalQuantity"));
        order.setStatus(rs.getInt("status"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        order.setPaymentStatus(rs.getInt("paymentStatus"));
        order.setCustomerName(rs.getString("customerName"));
        order.setCustomerPhone(rs.getString("customerPhone"));
        order.setCustomerEmail(rs.getString("customerEmail"));
        order.setNote(rs.getString("note"));
        order.setCreatedDate(rs.getTimestamp("createdDate"));
        order.setUpdatedDate(rs.getTimestamp("updatedDate"));
        return order;
    }
} 