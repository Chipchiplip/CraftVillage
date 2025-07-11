/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import context.DBContext;
import entity.Product.Product;
import entity.Product.ProductCategory;
import entity.Ticket.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.Types;
import java.sql.CallableStatement;

/**
 *
 * @author ACER
 */
public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("pid"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("description"),
                rs.getInt("stock"),
                rs.getInt("status"),
                rs.getInt("villageID"),
                rs.getInt("categoryID"),
                rs.getString("mainImageUrl"),
                rs.getInt("clickCount"),
                rs.getTimestamp("lastClicked"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
    }

    private ProductCategory mapResultSetToProductCategory(ResultSet rs) throws SQLException {
        return new ProductCategory(
                rs.getInt("categoryID"),
                rs.getString("categoryName"),
                rs.getString("description"),
                rs.getInt("status"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
    }

    private Product mapResultSetToProduct1(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("pid"),
                rs.getString("name"),
                rs.getBigDecimal("price"),
                rs.getString("description"),
                rs.getInt("stock"),
                rs.getInt("status"),
                rs.getInt("villageID"),
                rs.getInt("categoryID"),
                rs.getInt("craftTypeID"),
                rs.getString("mainImageUrl"),
                rs.getInt("clickCount"),
                rs.getTimestamp("lastClicked"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate"),
                rs.getString("sku"),
                rs.getBigDecimal("weight"),
                rs.getString("dimensions"),
                rs.getString("materials"),
                rs.getString("careInstructions"),
                rs.getString("warranty"),
                rs.getBigDecimal("averageRating"),
                rs.getInt("totalReviews")
        );
    }

    private ProductCategory mapResultSetToProductCategary(ResultSet rs) throws SQLException {
        return new ProductCategory(
                rs.getInt("categoryID"),
                rs.getString("categoryName"),
                rs.getString("description"),
                rs.getInt("status"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("ticketID"),
                rs.getInt("villageID"),
                rs.getInt("typeID"),
                rs.getBigDecimal("price"),
                rs.getInt("status"),
                rs.getTimestamp("createdDate"),
                rs.getTimestamp("updatedDate")
        );
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product getProductByID(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM Product WHERE pid = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM Product";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getProductByCategoryID(String categoryId) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM Product WHERE categoryID = ? AND status = 1";
            ps = conn.prepareStatement(sql);
            ps.setString(1, categoryId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getAllProductActive() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM Product WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct1(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getTop5NewestProducts() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT TOP 5 * FROM Product WHERE status = 1 ORDER BY createdDate DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<ProductCategory> getAllCategory() {
        List<ProductCategory> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM ProductCategory WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProductCategary(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getAllProductActiveByAdmin() {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            // Query cho user - chỉ lấy sản phẩm active của seller
            String sql = "SELECT * FROM Product WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct1(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> searchByName(String name) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM Product WHERE name LIKE ? AND status = 1";
            ps = conn.prepareStatement(sql);
            String searchPattern = "%" + name.trim() + "%";
            ps.setString(1, searchPattern);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Ticket> getAllTicketActive() {
        List<Ticket> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT * FROM VillageTicket WHERE status = 1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToTicket(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public boolean updateProductByAdmin(Product product) {
        String sql = "{CALL UpdateProductFull(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, product.getPid());
            cs.setString(2, product.getName());
            cs.setBigDecimal(3, product.getPrice());
            cs.setString(4, product.getDescription());
            cs.setInt(5, product.getStock());
            cs.setInt(6, product.getStockAdd());
            cs.setInt(7, product.getStatus());
            cs.setInt(8, product.getVillageID());
            cs.setInt(9, product.getCategoryID());
            cs.setString(10, product.getMainImageUrl());
            cs.setInt(11, product.getCraftTypeID());
            cs.setString(12, product.getSku());
            cs.setBigDecimal(13, product.getWeight());
            cs.setString(14, product.getDimensions());
            cs.setString(15, product.getMaterials());
            cs.setString(16, product.getCareInstructions());
            cs.setString(17, product.getWarranty());

            // Output param
            cs.registerOutParameter(18, Types.INTEGER);

            cs.execute();

            int result = cs.getInt(18);
            return result == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createProductByAdmin(Product product) {
        String sql = "{CALL CreateProductFull( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, product.getName());
            cs.setBigDecimal(2, product.getPrice());
            cs.setString(3, product.getDescription());
            cs.setInt(4, product.getStock());
            cs.setInt(5, product.getStatus());
            cs.setInt(6, product.getVillageID());
            cs.setInt(7, product.getCategoryID());
            cs.setString(8, product.getMainImageUrl());
            cs.setInt(9, product.getCraftTypeID());
            cs.setString(10, product.getSku());
            cs.setBigDecimal(11, product.getWeight());
            cs.setString(12, product.getDimensions());
            cs.setString(13, product.getMaterials());
            cs.setString(14, product.getCareInstructions());
            cs.setString(15, product.getWarranty());

            // Output parameter
            cs.registerOutParameter(16, Types.INTEGER);

            cs.execute();

            int result = cs.getInt(16);

            switch (result) {
                case 1:
                    return true;
                case -1:
                    System.out.println("Tên sản phẩm đã tồn tại.");
                    break;
                case 0:
                default:
                    System.out.println("Đã xảy ra lỗi khi tạo sản phẩm.");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProductByAdmin(int productId) {
        String sql = "{CALL DeleteProductByAdmin( ?, ?)}";
        try (Connection con = DBContext.getConnection(); CallableStatement cs = con.prepareCall(sql)) {
            cs.setInt(1, productId);
            // Output parameter
            cs.registerOutParameter(2, Types.INTEGER);

            cs.execute();
            if (cs.getInt(2) == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getSearchProductByAdmin(int status, int searchID, String contentSearch) {
        String query;
        contentSearch = contentSearch.trim();
        switch (searchID) {
            case 1:
                query = "SELECT * FROM Product WHERE  status = ? and categoryID LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 2:
                query = "SELECT * FROM Product WHERE status = ? and name COLLATE Latin1_General_CI_AI LIKE ? ORDER BY name ASC ";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 3:
                query = "SELECT * FROM Product WHERE status = ? and name COLLATE Latin1_General_CI_AI LIKE ? ORDER BY name DESC ";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 4:
                query = "SELECT * FROM Product WHERE status = ? AND name COLLATE Latin1_General_CI_AI LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 5:
                query = "SELECT * FROM Product WHERE status = ? and pid LIKE ?";
                contentSearch = "%" + contentSearch + "%";
                break;
            case 6:
                query = "SELECT * FROM Product WHERE  status = ?  AND price BETWEEN ? AND ?";
                break;
            case 7:
                query = "SELECT *  FROM Product  WHERE status = ?  AND CONVERT(date, createdDate) = CONVERT(date, GETDATE())";
                break;
            default:
                query = "SELECT * FROM Product WHERE status = ? and name COLLATE Latin1_General_CI_AI LIKE ?";
                contentSearch = "%" + contentSearch + "%"; // Cho phép tìm gần đúng
                break;
        }

        List<Product> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, status);
            if (searchID == 6) {
                double price = Double.parseDouble(contentSearch);
                ps.setDouble(2, price * 0.8);
                ps.setDouble(3, price * 1.2);
            } else if (searchID == 7) {
            } else {
                ps.setString(2, contentSearch);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct1(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    /**
     * Get products by name with price range and order filtering Advanced search
     * with filtering and sorting capabilities
     */
    public List<Product> getProductsByNameAndPriceRangeAndOrder(String name, String priceRange, String orderBy) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE status = 1");

            // Add name filter if provided
            if (name != null && !name.trim().isEmpty()) {
                sql.append(" AND (name LIKE ? OR description LIKE ?)");
            }

            // Add price range filter if provided
            if (priceRange != null && !priceRange.equals("all")) {
                switch (priceRange) {
                    case "0-100000":
                        sql.append(" AND price <= 100000");
                        break;
                    case "100000-500000":
                        sql.append(" AND price > 100000 AND price <= 500000");
                        break;
                    case "500000-1000000":
                        sql.append(" AND price > 500000 AND price <= 1000000");
                        break;
                    case "1000000+":
                        sql.append(" AND price > 1000000");
                        break;
                }
            }

            // Add ordering
            if (orderBy != null && !orderBy.equals("menu_order")) {
                switch (orderBy) {
                    case "date":
                        sql.append(" ORDER BY createdDate DESC");
                        break;
                    case "price":
                        sql.append(" ORDER BY price ASC");
                        break;
                    case "price-desc":
                        sql.append(" ORDER BY price DESC");
                        break;
                    default:
                        sql.append(" ORDER BY createdDate DESC");
                        break;
                }
            } else {
                sql.append(" ORDER BY createdDate DESC");
            }

            ps = conn.prepareStatement(sql.toString());

            // Set parameters for name search if provided
            if (name != null && !name.trim().isEmpty()) {
                String searchParam = "%" + name.trim() + "%";
                ps.setString(1, searchParam);
                ps.setString(2, searchParam);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getProductByCategory(int categoryID) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE  categoryID = ? and status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, categoryID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct1(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    /**
     * Get products by category with price range and order filtering
     */
    public List<Product> getProductsByCategoryAndPriceRangeAndOrder(String cateID, String priceRange, String orderBy) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM Product WHERE status = 1 AND categoryID = ?");

            // Add price range filter if provided
            if (priceRange != null && !priceRange.equals("all")) {
                switch (priceRange) {
                    case "0-100000":
                        sql.append(" AND price <= 100000");
                        break;
                    case "100000-500000":
                        sql.append(" AND price > 100000 AND price <= 500000");
                        break;
                    case "500000-1000000":
                        sql.append(" AND price > 500000 AND price <= 1000000");
                        break;
                    case "1000000+":
                        sql.append(" AND price > 1000000");
                        break;
                }
            }

            // Add ordering
            if (orderBy != null && !orderBy.equals("menu_order")) {
                switch (orderBy) {
                    case "date":
                        sql.append(" ORDER BY createdDate DESC");
                        break;
                    case "price":
                        sql.append(" ORDER BY price ASC");
                        break;
                    case "price-desc":
                        sql.append(" ORDER BY price DESC");
                        break;
                    default:
                        sql.append(" ORDER BY createdDate DESC");
                        break;
                }
            } else {
                sql.append(" ORDER BY createdDate DESC");
            }

            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, cateID);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getProductOutOfStockByAdmin() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE  stock = 0 and status = 1 ORDER BY categoryID";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct1(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }

    public String getCategoryNameByCategoryID(int categoryID) {
        String query = "SELECT categoryName FROM ProductCategory WHERE categoryID = ? AND status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, categoryID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("categoryName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return "";
    }

    public List<Product> getTopRatedByAdmin() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE averageRating >= 4.5 and averageRating <= 5 AND status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct1(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Nên dùng logging thay vì printStackTrace trong production
        }
        return list;
    }

    /**
     * Get most clicked products
     */
    public List<Product> getMostClickedProducts(int limit) {
        List<Product> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "SELECT TOP (?) * FROM Product WHERE status = 1 ORDER BY clickCount DESC, lastClicked DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, ps, rs);
        }
        return list;
    }

    public List<Product> getProductsByVillage(int villageId) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Product WHERE  villageID = ? and status = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, villageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct1(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log tốt hơn
        }
        return list;
    }
    
    public static void main(String[] args) {
        // System.out.println(new ProductDAO().createProductByAdmin(new Product("New3", BigDecimal.valueOf(1000000.00), "A", 1, 1, 1, 1, "A", 1, "A", BigDecimal.valueOf(10), "A", "A", "A", "A")));
        // System.out.println(new ProductDAO().getSearchProductByAdmin(1, 3, "Ne"));
        // System.out.println(new ProductDAO().getProductOutOfStockByAdmin());
        //System.out.println(new ProductDAO().getCategoryNameByCategoryID(1));
        System.out.println(new ProductDAO().getProductsByVillage(1));
    }

    
}
