/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import context.DBContext;
import entity.CartWishList.Wishlist;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
public class WishlistDAO {
    
    private Wishlist mapResultSetToWishList(ResultSet rs) throws SQLException {
        return new Wishlist(
                rs.getInt("wishlistID"),
                rs.getInt("userID"),
                rs.getInt("productID")
        );
    }


    public List<Wishlist> getWishListByUserId(int userId) {
        List<Wishlist> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM WishList WHERE userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToWishList(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public int getWishlistCount(int userId) {
        int count = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT COUNT(*) AS WishlistCount FROM WishList WHERE userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("WishlistCount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }

        return count;
    }

    // Alias method for compatibility
    public List<Wishlist> getWishlistByUser(int userId) {
        return getWishListByUserId(userId);
    }

    public boolean addToWishlist(int userId, int productId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "INSERT INTO WishList (userID, productID) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean removeFromWishlist(int wishlistId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "DELETE FROM WishList WHERE wishlistID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, wishlistId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    // Additional methods required by WishlistService
    public List<Wishlist> getAllWishlists() {
        List<Wishlist> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM WishList";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToWishList(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }
    
    public Wishlist getWishlistById(int wishlistId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM WishList WHERE wishlistID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, wishlistId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToWishList(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }
    
    public boolean addWishlist(Wishlist wishlist) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "INSERT INTO WishList (userID, productID) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, wishlist.getUserID());
            ps.setInt(2, wishlist.getProductID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    public boolean updateWishlist(Wishlist wishlist) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "UPDATE WishList SET userID = ?, productID = ? WHERE wishlistID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, wishlist.getUserID());
            ps.setInt(2, wishlist.getProductID());
            ps.setInt(3, wishlist.getWishlistID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }
    
    // Method to get wishlist with product details for display
    public List<WishlistWithProduct> getWishlistWithProductDetails(int userId) {
        List<WishlistWithProduct> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            System.out.println("[DEBUG] getWishlistWithProductDetails - Connection established for userID: " + userId);
            
            // Use LEFT JOIN to include wishlist items even if product doesn't exist
            String sql = "SELECT w.wishlistID, w.userID, w.productID, w.addedDate, " +
                        "p.name, p.price, p.description, p.mainImageUrl " +
                        "FROM WishList w " +
                        "LEFT JOIN Product p ON w.productID = p.pid " +
                        "WHERE w.userID = ? " +
                        "ORDER BY w.addedDate DESC";
            
            System.out.println("[DEBUG] Executing SQL: " + sql);
            System.out.println("[DEBUG] With userID parameter: " + userId);
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
                System.out.println("[DEBUG] Processing row " + rowCount);
                
                WishlistWithProduct item = new WishlistWithProduct();
                item.setWishlistID(rs.getInt("wishlistID"));
                item.setUserID(rs.getInt("userID"));
                item.setProductID(rs.getInt("productID"));
                item.setAddedDate(rs.getTimestamp("addedDate"));
                
                System.out.println("[DEBUG] Row " + rowCount + " - WishlistID: " + item.getWishlistID() + 
                                 ", ProductID: " + item.getProductID() + ", AddedDate: " + item.getAddedDate());
                
                // Handle case where product might not exist
                String productName = rs.getString("name");
                if (productName != null) {
                    item.setProductName(productName);
                    item.setProductPrice(rs.getDouble("price"));
                    item.setProductDescription(rs.getString("description"));
                    item.setProductImage(rs.getString("mainImageUrl"));
                    System.out.println("[DEBUG] Row " + rowCount + " - Product found: " + productName);
                } else {
                    // Product doesn't exist, use default values
                    item.setProductName("Sản phẩm không tồn tại (ID: " + item.getProductID() + ")");
                    item.setProductPrice(0.0);
                    item.setProductDescription("Sản phẩm này có thể đã bị xóa khỏi hệ thống");
                    item.setProductImage("assets/images/products/default.jpg");
                    System.out.println("[DEBUG] Row " + rowCount + " - Product NOT found for ID: " + item.getProductID());
                }
                list.add(item);
                System.out.println("[DEBUG] Added item to list. Current list size: " + list.size());
            }
            
            System.out.println("[DEBUG] getWishlistWithProductDetails - UserID: " + userId + 
                             ", Total rows processed: " + rowCount + ", Final list size: " + list.size());
            
        } catch (Exception e) {
            System.err.println("[ERROR] getWishlistWithProductDetails - UserID: " + userId + " - Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
                System.out.println("[DEBUG] getWishlistWithProductDetails - Resources closed");
            } catch (Exception e) {
                System.err.println("[ERROR] Error closing resources: " + e.getMessage());
            }
        }
        return list;
    }
    
    // Debug method to check wishlist data
    public void debugWishlistData(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            
            // Check wishlist items
            String sql = "SELECT * FROM WishList WHERE userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            System.out.println("[DEBUG] Wishlist items for user " + userId + ":");
            while (rs.next()) {
                System.out.println("  WishlistID: " + rs.getInt("wishlistID") + 
                                 ", ProductID: " + rs.getInt("productID") + 
                                 ", AddedDate: " + rs.getTimestamp("addedDate"));
            }
            rs.close();
            ps.close();
            
            // Check if products exist
            sql = "SELECT w.productID, p.pid, p.name FROM WishList w " +
                  "LEFT JOIN Product p ON w.productID = p.pid WHERE w.userID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            System.out.println("[DEBUG] Product check for user " + userId + ":");
            while (rs.next()) {
                int wishlistProductID = rs.getInt("productID");
                int productPID = rs.getInt("pid");
                String productName = rs.getString("name");
                
                if (productName != null) {
                    System.out.println("  ProductID " + wishlistProductID + " exists: " + productName);
                } else {
                    System.out.println("  ProductID " + wishlistProductID + " DOES NOT EXIST in Product table");
                }
            }
            
        } catch (Exception e) {
            System.err.println("[ERROR] Debug wishlist data failed");
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
    }
    
    // Helper class for wishlist with product details
    public static class WishlistWithProduct {
        private int wishlistID;
        private int userID;
        private int productID;
        private java.sql.Timestamp addedDate;
        private String productName;
        private double productPrice;
        private String productDescription;
        private String productImage;
        
        // Getters and setters
        public int getWishlistID() { return wishlistID; }
        public void setWishlistID(int wishlistID) { this.wishlistID = wishlistID; }
        
        public int getUserID() { return userID; }
        public void setUserID(int userID) { this.userID = userID; }
        
        public int getProductID() { return productID; }
        public void setProductID(int productID) { this.productID = productID; }
        
        public java.sql.Timestamp getAddedDate() { return addedDate; }
        public void setAddedDate(java.sql.Timestamp addedDate) { this.addedDate = addedDate; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public double getProductPrice() { return productPrice; }
        public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
        
        public String getProductDescription() { return productDescription; }
        public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
        
        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }
    }
    
    private void closeResources(java.sql.Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        }
    }
}
