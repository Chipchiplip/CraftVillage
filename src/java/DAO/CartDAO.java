package DAO;

import context.DBContext;
import entity.CartWishList.Cart;
import entity.CartWishList.CartItem;
import entity.CartWishList.CartTicket;
import DAO.CartTicketDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartDAO {
    private static final Logger LOGGER = Logger.getLogger(CartDAO.class.getName());

    private static final String SELECT_CART_BY_USER = 
        "SELECT c.cartID, c.userID, c.createdDate, c.updatedDate, " +
        "ci.itemID as cartItemID, ci.productID, ci.quantity, ci.createdDate as itemCreatedDate, ci.updatedDate as itemUpdatedDate, " +
        "p.name as productName, p.price, p.mainImageUrl as imageUrl " +
        "FROM Cart c " +
        "LEFT JOIN CartItem ci ON c.cartID = ci.cartID " +
        "LEFT JOIN Product p ON ci.productID = p.pid " +
        "WHERE c.userID = ?";

    private static final String INSERT_CART = 
        "INSERT INTO Cart (userID, createdDate) VALUES (?, GETDATE())";

    private static final String UPDATE_CART = 
        "UPDATE Cart SET updatedDate = GETDATE() WHERE cartID = ?";

    private static final String INSERT_CART_ITEM = 
        "INSERT INTO CartItem (cartID, productID, quantity, createdDate) VALUES (?, ?, ?, GETDATE())";

    private static final String UPDATE_CART_ITEM = 
        "UPDATE CartItem SET quantity = ?, updatedDate = GETDATE() WHERE itemID = ?";

    private static final String DELETE_CART_ITEM = 
        "DELETE FROM CartItem WHERE itemID = ?";

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
    
    // Helper method to convert status integer to string
    private String getStatusString(int status) {
        switch (status) {
            case 0: return "cart";      // đang xử lí (cart)
            case 1: return "paid";      // đã thanh toán
            case 2: return "cancelled"; // đã huỷ
            case 3: return "refunded";  // hoàn trả
            default: return "unknown";
        }
    }

    public Cart getCartByUser(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Cart cart = null;
        List<CartItem> items = new ArrayList<>();
        List<CartTicket> tickets = new ArrayList<>();
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_CART_BY_USER);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (cart == null) {
                    cart = new Cart(
                        rs.getInt("cartID"),
                        rs.getInt("userID"),
                        items,
                        tickets,
                        0.0, // totalAmount sẽ tính sau
                        "cart",
                        rs.getTimestamp("createdDate"),
                        rs.getTimestamp("updatedDate")
                    );
                }
                int productId = rs.getInt("productID");
                if (!rs.wasNull() && productId > 0) {
                    CartItem item = new CartItem(
                        rs.getInt("cartItemID"),
                        rs.getInt("cartID"),
                        productId,
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl")
                    );
                    items.add(item);
                }
            }
            // Lấy ticket từ CartTicketDAO
            if (cart != null) {
                CartTicketDAO cartTicketDAO = new CartTicketDAO();
                tickets = cartTicketDAO.getCartTicketsByCartId(cart.getCartID());
                cart.setTickets(tickets);
                // Tính tổng tiền
                double total = 0.0;
                for (CartItem item : items) {
                    total += item.getPrice() * item.getQuantity();
                }
                for (CartTicket ticket : tickets) {
                    total += ticket.getPrice() * ticket.getQuantity();
                }
                cart.setTotalAmount(total);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting cart for user ID: " + userId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return cart;
    }

    public int createCart(Cart cart) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cartId = -1;
        try {
            conn = new DBContext().getConnection();
            // Kiểm tra đã có cart chưa
            String checkSql = "SELECT cartID FROM Cart WHERE userID = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, cart.getUserID());
            rs = ps.executeQuery();
            if (rs.next()) {
                cartId = rs.getInt("cartID");
            } else {
                ps = conn.prepareStatement(INSERT_CART, PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setInt(1, cart.getUserID());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        cartId = rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating cart", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return cartId;
    }

    public boolean addCartItem(CartItem item) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            // Kiểm tra sản phẩm đã có trong cart chưa
            String checkSql = "SELECT itemID, quantity FROM CartItem WHERE cartID = ? AND productID = ?";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, item.getCartID());
            ps.setInt(2, item.getProductID());
            rs = ps.executeQuery();
            if (rs.next()) {
                // Đã có, cập nhật số lượng
                int existingId = rs.getInt("itemID");
                int existingQty = rs.getInt("quantity");
                String updateSql = "UPDATE CartItem SET quantity = ?, updatedDate = GETDATE() WHERE itemID = ?";
                ps = conn.prepareStatement(updateSql);
                ps.setInt(1, existingQty + item.getQuantity());
                ps.setInt(2, existingId);
                int rows = ps.executeUpdate();
                return rows > 0;
            } else {
                // Thêm mới
                ps = conn.prepareStatement(INSERT_CART_ITEM);
                ps.setInt(1, item.getCartID());
                ps.setInt(2, item.getProductID());
                ps.setInt(3, item.getQuantity());
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding/updating cart item", e);
            return false;
        } finally {
            closeResources(conn, ps, rs);
        }
    }

    public boolean updateCartItem(CartItem item) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_CART_ITEM);
            ps.setInt(1, item.getQuantity());
            ps.setInt(2, item.getCartItemID());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating cart item", e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteCartItem(int cartItemId) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(DELETE_CART_ITEM);
            ps.setInt(1, cartItemId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting cart item", e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean clearCart(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            // Lấy cartID
            String getCartSql = "SELECT cartID FROM Cart WHERE userID = ?";
            ps = conn.prepareStatement(getCartSql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int cartId = rs.getInt("cartID");
                
                // Xóa toàn bộ CartItem
                String deleteItemsSql = "DELETE FROM CartItem WHERE cartID = ?";
                ps = conn.prepareStatement(deleteItemsSql);
                ps.setInt(1, cartId);
                ps.executeUpdate();
                
                // Xóa toàn bộ CartTicket
                String deleteTicketsSql = "DELETE FROM CartTicket WHERE cartID = ?";
                ps = conn.prepareStatement(deleteTicketsSql);
                ps.setInt(1, cartId);
                ps.executeUpdate();
                
                return true;
            } else {
                return true; // Không có cart cũng coi như đã clear
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error clearing cart for user ID: " + userId, e);
            return false;
        } finally {
            closeResources(conn, ps, rs);
        }
    }
} 