package DAO;

import context.DBContext;
import entity.Orders.OrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDetailDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDetailDAO.class.getName());

    private static final String SELECT_ORDER_DETAILS = "SELECT od.*, p.name as productName, p.imageUrl " +
            "FROM OrderDetail od " +
            "JOIN Product p ON od.productID = p.pid " +
            "WHERE od.orderID = ?";
    private static final String SELECT_ORDER_DETAIL = "SELECT od.*, p.name as productName, p.imageUrl " +
            "FROM OrderDetail od " +
            "JOIN Product p ON od.productID = p.pid " +
            "WHERE od.orderDetailID = ?";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO OrderDetail (orderID, productID, price, quantity, createdDate) " +
            "VALUES (?, ?, ?, ?, GETDATE())";
    private static final String UPDATE_ORDER_DETAIL = "UPDATE OrderDetail SET price = ?, quantity = ?, updatedDate = GETDATE() " +
            "WHERE orderDetailID = ?";
    private static final String DELETE_ORDER_DETAIL = "DELETE FROM OrderDetail WHERE orderDetailID = ?";
    private static final String GET_ORDER_TOTAL = "SELECT SUM(price * quantity) as total FROM OrderDetail WHERE orderID = ?";

    // Helper method to close resources
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error closing database resources", e);
        }
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_ORDER_DETAILS);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            while (rs.next()) {
                details.add(mapResultSetToOrderDetail(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order details for order ID: " + orderId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return details;
    }

    public OrderDetail getOrderDetail(int orderDetailId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_ORDER_DETAIL);
            ps.setInt(1, orderDetailId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToOrderDetail(rs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order detail with ID: " + orderDetailId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public int addOrderDetail(OrderDetail detail) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int orderDetailId = -1;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(INSERT_ORDER_DETAIL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, detail.getOrderID());
            ps.setInt(2, detail.getProductID());
            ps.setDouble(3, detail.getPrice());
            ps.setInt(4, detail.getQuantity());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    orderDetailId = rs.getInt(1);
                    LOGGER.log(Level.INFO, "Order detail added successfully with ID: {0}", orderDetailId);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding order detail", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return orderDetailId;
    }

    public boolean updateOrderDetail(OrderDetail detail) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_ORDER_DETAIL);
            ps.setDouble(1, detail.getPrice());
            ps.setInt(2, detail.getQuantity());
            ps.setInt(3, detail.getOrderDetailID());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Order detail updated successfully for ID: {0}", detail.getOrderDetailID());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No order detail found to update with ID: {0}", detail.getOrderDetailID());
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order detail with ID: " + detail.getOrderDetailID(), e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteOrderDetail(int orderDetailId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(DELETE_ORDER_DETAIL);
            ps.setInt(1, orderDetailId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Order detail deleted successfully for ID: {0}", orderDetailId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No order detail found to delete with ID: {0}", orderDetailId);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order detail with ID: " + orderDetailId, e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public double getOrderTotal(int orderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(GET_ORDER_TOTAL);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order total for order ID: " + orderId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return 0.0;
    }

    private OrderDetail mapResultSetToOrderDetail(ResultSet rs) throws SQLException {
        return new OrderDetail(
            rs.getInt("orderDetailID"),
            rs.getInt("orderID"),
            rs.getInt("productID"),
            rs.getString("productName"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("imageUrl"),
            rs.getTimestamp("createdDate"),
            rs.getTimestamp("updatedDate")
        );
    }
}