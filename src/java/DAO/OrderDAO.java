package DAO;

import entity.Orders.Order;
import entity.Orders.OrderDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import context.DBContext;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

public class OrderDAO {
    private static final Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    private static final String SELECT_ALL_ORDERS = "SELECT * FROM Orders ORDER BY orderDate DESC";
    private static final String SELECT_ORDER_BY_ID = "SELECT * FROM Orders WHERE orderID = ?";
    private static final String SELECT_ORDERS_BY_USER_ID = "SELECT * FROM Orders WHERE userID = ? ORDER BY orderDate DESC";
    private static final String INSERT_ORDER = "INSERT INTO Orders (userID, orderDate, totalAmount, status, shippingAddress, phoneNumber, email, paymentMethod, paymentStatus, createdDate) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, GETDATE())";
    private static final String UPDATE_ORDER_STATUS = "UPDATE Orders SET status = ?, updatedDate = GETDATE() WHERE orderID = ?";
    private static final String UPDATE_PAYMENT_STATUS = "UPDATE Orders SET paymentStatus = ?, updatedDate = GETDATE() WHERE orderID = ?";
    private static final String DELETE_ORDER = "UPDATE Orders SET status = 2, updatedDate = GETDATE() WHERE orderID = ?";  // 2 = cancelled
    private static final String SELECT_ORDER_DETAILS = "SELECT * FROM OrderDetail WHERE orderID = ?";
    private static final String INSERT_ORDER_DETAIL = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
    private static final String GET_ORDERS_BY_STATUS = "SELECT * FROM Orders WHERE status = ? ORDER BY orderDate DESC";
    private static final String GET_ORDERS_BY_DATE_RANGE = "SELECT * FROM Orders WHERE orderDate BETWEEN ? AND ? ORDER BY orderDate DESC";

    // Helper method to convert status string to int
    private int getStatusInt(String status) {
        switch (status.toLowerCase()) {
            case "cart":
            case "pending": return 0;  // đang xử lí
            case "paid":
            case "completed": return 1;  // đã thanh toán
            case "cancelled": return 2;  // đã huỷ
            case "refunded": return 3;  // hoàn trả
            default: return 0;
        }
    }

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    //----------------------------------------------------------------------------------------------------------------
 
    public int getnewOrderId() throws SQLException {
        String sql = "SELECT TOP 1 id FROM Orders ORDER BY id DESC";
        int newId = 1;

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                newId = rs.getInt("id") + 1;
            }

        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy ID đơn hàng mới nhất: " + e.getMessage());
        }

        return newId;
    }
    
    public void addOrderDetail(int orderId, int productId, int quantity, double price) throws SQLException {
        String sql = "INSERT INTO OrderDetail (order_id, product_id, quantity, price, subtotall) VALUES (?, ?, ?, ?, ?)";

        double subtotal = quantity * price;

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.setDouble(5, subtotal);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Thêm chi tiết đơn hàng thất bại, không có dòng nào được thêm.");
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage(), e);
        }
    }

    public int createOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Orders (userID, total_price, status) VALUES (?, ?, ?)"; // Bỏ orderDate
        int orderId = -1;

        try (Connection conn = new DBContext().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserID());
            stmt.setDouble(2, order.getTotalAmount().doubleValue());
            stmt.setInt(3, order.getStatus());  // Use int status directly

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Tạo đơn hàng thất bại, không có dòng nào được thêm.");
            }

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                } else {
                    throw new SQLException("Tạo đơn hàng thất bại, không lấy được ID.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi tạo đơn hàng: " + e.getMessage(), e);
        }

        return orderId;
    }

    //------------------------------------------------------------------------------------------------------------
    // Lấy danh sách đơn hàng của người dùng
    public List<Order> getOrdersByUserId(int userId) {
    List<Order> list = new ArrayList<>();
    String query = "SELECT * FROM Orders WHERE userID = ?";

    try (Connection conn = new DBContext().getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) { // Lặp qua từng dòng kết quả
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("userID"),
                    rs.getString("orderDate"),
                    rs.getDouble("total_price"),
                    rs.getString("status")
                ));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
    
    // Lấy danh sách đơn hàng của seller (dựa vào sản phẩm thuộc về seller)
    public List<Order> getOrdersBySellerId(int sellerId) {
        List<Order> list = new ArrayList<>();
        String query = "SELECT DISTINCT o.* FROM Orders o " +
                       "JOIN OrderDetail od ON o.id = od.order_id " +
                       "JOIN Product p ON od.product_id = p.pid " +
                       "WHERE p.sell_id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, sellerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order order = Order.fromResultSet(rs);
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy tất cả đơn hàng (cho admin)
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_ALL_ORDERS);
            rs = ps.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setOrderDetails(getOrderDetails(order.getOrderID()));
                orders.add(order);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all orders", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return orders;
    }
    
    // Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderId, int status) {
        String query = "UPDATE Orders SET status = ? WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, status); // Changed from setString to setInt
            ps.setInt(2, orderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Overloaded method for backward compatibility
    public boolean updateOrderStatus(int orderId, String statusString) {
        int status = convertStringStatusToInt(statusString);
        return updateOrderStatus(orderId, status);
    }
    
    // Helper method to convert String status to int
    private int convertStringStatusToInt(String statusString) {
        if (statusString == null) return 0;
        switch (statusString.toLowerCase()) {
            case "pending": case "processing": return 0;
            case "delivered": case "completed": return 1;
            case "cancelled": return 2;
            case "refunded": return 3;
            default: return 0;
        }
    }
    
    // Update payment status
    public boolean updatePaymentStatus(int orderId, int paymentStatus) {
        String query = "UPDATE Orders SET paymentStatus = ? WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, paymentStatus); // Changed from setString to setInt
            ps.setInt(2, orderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Order getOrderById(int orderId) {
        Order order = null;
        String query = "SELECT * FROM Orders WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                order = Order.fromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean cancelOrder(int orderId) {
        String query = "UPDATE Orders SET status = 'cancelled' WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

        public Map<String, Double> getRevenueByMonth() {
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        String query = "SELECT YEAR(orderDate) AS year, MONTH(orderDate) AS month, SUM(total_price) AS revenue " +
                       "FROM Orders WHERE status = 'completed' " +
                       "GROUP BY YEAR(orderDate), MONTH(orderDate) " +
                       "ORDER BY YEAR(orderDate), MONTH(orderDate)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String key = rs.getInt("year") + "-" + String.format("%02d", rs.getInt("month"));
                revenueMap.put(key, rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueMap;
    }

    public Map<String, Double> getRevenueByYear() {
        Map<String, Double> revenueMap = new LinkedHashMap<>();
        String query = "SELECT YEAR(orderDate) AS year, SUM(total_price) AS revenue " +
                       "FROM Orders WHERE status = 'completed' " +
                       "GROUP BY YEAR(orderDate) " +
                       "ORDER BY YEAR(orderDate)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                String key = String.valueOf(rs.getInt("year"));
                revenueMap.put(key, rs.getDouble("revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return revenueMap;
    }

    public List<Map<String, Object>> getTopSellingProducts() {
        List<Map<String, Object>> topProducts = new ArrayList<>();
        String query = "SELECT TOP 10 p.name, SUM(od.quantity) AS totalSold, SUM(od.subtotall) AS totalRevenue " +
                       "FROM OrderDetail od " +
                       "JOIN Product p ON od.product_id = p.pid " +
                       "JOIN Orders o ON od.order_id = o.id " +
                       "WHERE o.status = 'completed' " +
                       "GROUP BY p.pid, p.name " +
                       "ORDER BY totalSold DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> product = new LinkedHashMap<>();
                product.put("name", rs.getString("name"));
                product.put("totalSold", rs.getInt("totalSold"));
                product.put("totalRevenue", rs.getDouble("totalRevenue"));
                topProducts.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topProducts;
    }

    public static void main(String[] args) {
        OrderDAO dao = new OrderDAO();
        
        // Test getOrdersByUserId
        List<Order> orders = dao.getOrdersByUserId(1);
        System.out.println("Orders for user 1: " + orders.size());
        
        // Test getRevenueByMonth
        Map<String, Double> monthlyRevenue = dao.getRevenueByMonth();
        System.out.println("Monthly revenue: " + monthlyRevenue);
        
        // Test getTopSellingProducts
        List<Map<String, Object>> topProducts = dao.getTopSellingProducts();
        System.out.println("Top selling products: " + topProducts.size());
        
        // Test updateOrderStatus
        boolean updated = dao.updateOrderStatus(1, "completed");
        System.out.println("Order status updated: " + updated);
        
        // Test getOrderById
        Order order = dao.getOrderById(1);
        System.out.println("Order by ID: " + (order != null ? order.toString() : "Not found"));
        
        // Test cancelOrder
        boolean cancelled = dao.cancelOrder(2);
        System.out.println("Order cancelled: " + cancelled);
    }

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
                OrderDetail detail = new OrderDetail();
                detail.setOrderDetailID(rs.getInt("orderDetailID"));
                detail.setOrderID(rs.getInt("orderID"));
                detail.setProductID(rs.getInt("productID"));
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getBigDecimal("price"));
                details.add(detail);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting order details for order ID: " + orderId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return details;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderID(rs.getInt("orderID"));
        order.setUserID(rs.getInt("userID"));
        order.setOrderDate(rs.getTimestamp("orderDate"));
        order.setTotalAmount(rs.getBigDecimal("totalAmount"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shippingAddress"));
        order.setPhoneNumber(rs.getString("phoneNumber"));
        order.setEmail(rs.getString("email"));
        order.setPaymentMethod(rs.getString("paymentMethod"));
        order.setPaymentStatus(rs.getString("paymentStatus"));
        order.setCreatedDate(rs.getTimestamp("createdDate"));
        order.setUpdatedDate(rs.getTimestamp("updatedDate"));
        return order;
    }

    public boolean updateOrder(Order order) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "UPDATE Orders SET userID = ?, total_price = ?, status = ?, " +
                        "shippingAddress = ?, phoneNumber = ?, email = ?, " +
                        "paymentMethod = ?, paymentStatus = ?, updatedDate = GETDATE() " +
                        "WHERE id = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order.getUserID());
            ps.setDouble(2, order.getTotalAmount().doubleValue());
            ps.setInt(3, order.getStatus()); // Use int status directly
            ps.setString(4, order.getShippingAddress());
            ps.setString(5, order.getPhoneNumber());
            ps.setString(6, order.getEmail());
            ps.setString(7, order.getPaymentMethod());
            ps.setInt(8, order.getPaymentStatus()); // Use int paymentStatus directly
            ps.setInt(9, order.getOrderID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order: " + order.getOrderID(), e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteOrder(int id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            // First delete all order details
            String deleteDetailsSql = "DELETE FROM OrderDetail WHERE order_id = ?";
            ps = conn.prepareStatement(deleteDetailsSql);
            ps.setInt(1, id);
            ps.executeUpdate();

            // Then delete the order
            String deleteOrderSql = "DELETE FROM Orders WHERE id = ?";
            ps = conn.prepareStatement(deleteOrderSql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            conn.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error deleting order ID: " + id, e);
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error resetting auto-commit", e);
            }
            closeResources(conn, ps, null);
        }
    }

    public boolean updateOrderDetail(int orderId, int productId, int quantity, double price) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "UPDATE OrderDetail SET quantity = ?, price = ?, subtotall = ? " +
                        "WHERE order_id = ? AND product_id = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setDouble(2, price);
            ps.setDouble(3, quantity * price); // Calculate subtotal
            ps.setInt(4, orderId);
            ps.setInt(5, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating order detail", e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteOrderDetail(int orderId, int productId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "DELETE FROM OrderDetail WHERE order_id = ? AND product_id = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2, productId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting order detail", e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
} 