package DAO;

import context.DBContext;
import entity.CraftVillage.CraftReview;
import entity.Product.ProductReview;
import java.util.logging.Level;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import java.util.logging.Logger;

public class ReviewDAO {

    private static final Logger LOGGER = Logger.getLogger(ReviewDAO.class.getName());

    // Map dữ liệu từ ResultSet thành đối tượng CraftReview (cho làng nghề)
    private CraftReview mapResultSetToCraftReview(ResultSet rs) throws SQLException {
        return new CraftReview(
                rs.getInt("reviewID"),
                rs.getInt("villageID"),
                rs.getInt("userID"),
                rs.getInt("rating"),
                rs.getString("reviewText"),
                rs.getTimestamp("reviewDate"),
                rs.getString("response"),
                rs.getTimestamp("responseDate")
        );
    }

    // Map dữ liệu từ ResultSet thành đối tượng ProductReview (cho sản phẩm)
    private ProductReview mapResultSetToProductReview(ResultSet rs) throws SQLException {
        return new ProductReview(
                rs.getInt("reviewID"),
                rs.getInt("productID"),
                rs.getInt("userID"),
                rs.getInt("rating"),
                rs.getString("reviewText"),
                rs.getTimestamp("reviewDate"),
                rs.getString("response"),
                rs.getTimestamp("responseDate")
        );
    }

    // Đóng kết nối và tài nguyên JDBC
    private void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
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
            e.printStackTrace();
        }
    }

    // Lấy tất cả đánh giá sản phẩm theo productID
    public List<ProductReview> getAllProductReviewByAdmin(int productID) {
        String query = "SELECT * FROM ProductReview WHERE productID = ? ORDER BY reviewDate DESC";
        List<ProductReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productID);

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProductReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    // Lấy tất cả đánh giá làng nghề theo villageID
    public List<CraftReview> getAllVillageReviewByAdmin(int villageID) {
        String query = "SELECT * FROM VillageReview WHERE villageID = ? ORDER BY reviewDate DESC";
        List<CraftReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, villageID);

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToCraftReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public boolean responseProductReviewByAdmin(int reviewID, String responseText) {
        String sql = "{call sp_ResponseProductReviewByAdmin(?, ?, ?)}"; // Không dùng ?= call
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, reviewID);
            cs.setString(2, responseText);
            cs.registerOutParameter(3, Types.INTEGER); // OUTPUT param @result

            cs.execute();

            int result = cs.getInt(3); // Lấy giá trị từ OUTPUT param
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error responding to review: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteProductReviewByAdmin(int reviewID) {
        String sql = "{call sp_DeleteProductReviewByAdmin(?, ?)}"; // 1 input, 1 output
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, reviewID);
            cs.registerOutParameter(2, Types.INTEGER); // @result

            cs.execute();

            int result = cs.getInt(2); // lấy kết quả từ OUTPUT
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting review: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean responseVillageReviewByAdmin(int reviewID, String responseText) {
        String sql = "{call sp_ResponseVillageReviewByAdmin(?, ?, ?)}"; // Không dùng ?= call
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, reviewID);
            cs.setString(2, responseText);
            cs.registerOutParameter(3, Types.INTEGER); // OUTPUT param @result

            cs.execute();

            int result = cs.getInt(3); // Lấy giá trị từ OUTPUT param
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error responding to review: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteVillageReviewByAdmin(int reviewID) {
        String sql = "{call sp_DeleteVillageReviewByAdmin(?, ?)}"; // 1 input, 1 output
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, reviewID);
            cs.registerOutParameter(2, Types.INTEGER); // @result

            cs.execute();

            int result = cs.getInt(2); // lấy kết quả từ OUTPUT
            return result == 1;

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting review: " + e.getMessage(), e);
        }
        return false;
    }

    public List<ProductReview> searchProductReviewByAdmin(int userID) {
        String query = "SELECT * FROM ProductReview WHERE userID = ?";
        List<ProductReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID); // Gán giá trị cho tham số ?
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProductReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log lỗi nếu cần
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<CraftReview> searchVillageReviewByAdmin(int userID) {
        String query = "SELECT * FROM VillageReview WHERE userID = ?";
        List<CraftReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID); // Gán giá trị cho tham số ?
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCraftReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log lỗi nếu cần
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<CraftReview> searchVillageReviewToday(int villageID) {
        String query = "SELECT * FROM VillageReview WHERE villageID = ? and  CAST(reviewDate AS DATE) = CAST(GETDATE() AS DATE)";
        List<CraftReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, villageID); // Gán giá trị cho tham số ?
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToCraftReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log nếu cần
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }

    public List<ProductReview> searchProductReviewToday(int productID) {
        String query = "SELECT * FROM ProductReview WHERE productID = ?  and CAST(reviewDate AS DATE) = CAST(GETDATE() AS DATE)";
        List<ProductReview> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
             ps.setInt(1, productID); 
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProductReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log nếu cần
        } finally {
            closeResources(conn, ps, rs);
        }

        return list;
    }
    
    public boolean addVillageReview(CraftReview review) {
    String sql = "{call sp_addVillageReview(?, ?, ?, ?, ?)}";

    try (Connection con = DBContext.getConnection();
         CallableStatement cs = con.prepareCall(sql)) {

        cs.setInt(1, review.getVillageID());
        cs.setInt(2, review.getUserID());
        cs.setString(3, review.getReviewText());
        cs.setInt(4, review.getRating());
        cs.registerOutParameter(5, Types.INTEGER);

        cs.execute();

        int result = cs.getInt(5);
        return result == 1;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    // NEW METHODS: Review eligibility checking
    
    /**
     * Check if order is eligible for review (delivered + paid)
     * @param orderID The order ID to check
     * @return true if order status=1 (delivered) AND paymentStatus=1 (paid)
     */
    public boolean isOrderEligibleForReview(int orderID) {
        String query = "SELECT COUNT(*) FROM Orders WHERE id = ? AND status = 1 AND paymentStatus = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderID);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking order eligibility: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    /**
     * Check if user has already reviewed a specific product in a specific order
     * @param userID The user ID
     * @param productID The product ID  
     * @param orderID The order ID
     * @return true if user has already reviewed this product from this order
     */
    public boolean hasUserReviewedProduct(int userID, int productID, int orderID) {
        String query = "SELECT COUNT(*) FROM ProductReview pr " +
                      "WHERE pr.userID = ? AND pr.productID = ? " +
                      "AND EXISTS (SELECT 1 FROM OrderDetail od WHERE od.product_id = ? AND od.order_id = ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userID);
            ps.setInt(2, productID);
            ps.setInt(3, productID);
            ps.setInt(4, orderID);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user reviewed product: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return false;
    }
    
    /**
     * Get all products from an order that user can review
     * @param userID The user ID
     * @param orderID The order ID
     * @return List of product IDs that can be reviewed
     */
    public List<Integer> getReviewableProductsFromOrder(int userID, int orderID) {
        List<Integer> reviewableProducts = new ArrayList<>();
        
        // First check if order is eligible for review
        if (!isOrderEligibleForReview(orderID)) {
            return reviewableProducts; // Empty list
        }
        
        String query = "SELECT DISTINCT od.product_id FROM OrderDetail od " +
                      "JOIN Orders o ON od.order_id = o.id " +
                      "WHERE o.id = ? AND o.userID = ? " +
                      "AND NOT EXISTS (SELECT 1 FROM ProductReview pr WHERE pr.userID = ? AND pr.productID = od.product_id)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderID);
            ps.setInt(2, userID);
            ps.setInt(3, userID);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                reviewableProducts.add(rs.getInt("product_id"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting reviewable products: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
        }
        
        return reviewableProducts;
    }
    
    /**
     * Check if user can review a specific product from a specific order
     * @param userID The user ID
     * @param productID The product ID
     * @param orderID The order ID
     * @return true if user can review this product
     */
    public boolean canUserReviewProduct(int userID, int productID, int orderID) {
        // Check order eligibility first
        if (!isOrderEligibleForReview(orderID)) {
            return false;
        }
        
        // Check if user hasn't already reviewed this product
        if (hasUserReviewedProduct(userID, productID, orderID)) {
            return false;
        }
        
        // Check if this product exists in this order for this user
        String query = "SELECT COUNT(*) FROM OrderDetail od " +
                      "JOIN Orders o ON od.order_id = o.id " +
                      "WHERE o.id = ? AND o.userID = ? AND od.product_id = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderID);
            ps.setInt(2, userID);
            ps.setInt(3, productID);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking if user can review product: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, rs);
        }
        
        return false;
    }
    
    /**
     * Add a product review with order validation
     * @param review The product review
     * @param orderID The order ID this review relates to
     * @return true if review was added successfully
     */
    public boolean addProductReviewFromOrder(ProductReview review, int orderID) {
        // Validate that user can review this product from this order
        if (!canUserReviewProduct(review.getUserID(), review.getProductID(), orderID)) {
            LOGGER.log(Level.WARNING, "User {0} cannot review product {1} from order {2}", 
                      new Object[]{review.getUserID(), review.getProductID(), orderID});
            return false;
        }
        
        String query = "INSERT INTO ProductReview (productID, userID, rating, reviewText, reviewDate) " +
                      "VALUES (?, ?, ?, ?, GETDATE())";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBContext.getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, review.getProductID());
            ps.setInt(2, review.getUserID());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding product review: " + e.getMessage(), e);
        } finally {
            closeResources(conn, ps, null);
        }
        
        return false;
    }
    
    // Test thử
    public static void main(String[] args) {
        ReviewDAO dao = new ReviewDAO();
        System.out.println(dao.addVillageReview(new CraftReview(6, 1, 2, "Huhu")));
    }
}

