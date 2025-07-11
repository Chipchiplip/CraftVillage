package DAO;

import context.DBContext;
import entity.Product.ProductImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductImageDAO {
    private static final Logger LOGGER = Logger.getLogger(ProductImageDAO.class.getName());

    private static final String SELECT_ALL_IMAGES = "SELECT * FROM ProductImage WHERE status = 1 ORDER BY productId, displayOrder";
    private static final String SELECT_IMAGES_BY_PRODUCT = "SELECT * FROM ProductImage WHERE productId = ? AND status = 1 ORDER BY displayOrder";
    private static final String SELECT_IMAGE_BY_ID = "SELECT * FROM ProductImage WHERE imageId = ?";
    private static final String SELECT_MAIN_IMAGE = "SELECT * FROM ProductImage WHERE productId = ? AND status = 1 AND displayOrder = 0";
    private static final String INSERT_IMAGE = "INSERT INTO ProductImage (productId, imageUrl, altText, displayOrder, status, createdDate) " +
            "VALUES (?, ?, ?, ?, ?, GETDATE())";
    private static final String UPDATE_IMAGE = "UPDATE ProductImage SET imageUrl = ?, altText = ?, displayOrder = ?, " +
            "status = ?, updatedDate = GETDATE() WHERE imageId = ?";
    private static final String DELETE_IMAGE = "UPDATE ProductImage SET status = 0, updatedDate = GETDATE() WHERE imageId = ?";
    private static final String UPDATE_DISPLAY_ORDER = "UPDATE ProductImage SET displayOrder = ?, updatedDate = GETDATE() WHERE imageId = ?";

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

    public List<ProductImage> getAllImages() {
        List<ProductImage> images = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_ALL_IMAGES);
            rs = ps.executeQuery();

            while (rs.next()) {
                images.add(mapResultSetToProductImage(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all product images", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return images;
    }

    public List<ProductImage> getImagesByProduct(int productId) {
        List<ProductImage> images = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_IMAGES_BY_PRODUCT);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            while (rs.next()) {
                images.add(mapResultSetToProductImage(rs));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting images for product ID: " + productId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return images;
    }

    public ProductImage getImageById(int imageId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_IMAGE_BY_ID);
            ps.setInt(1, imageId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProductImage(rs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting image with ID: " + imageId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public ProductImage getMainImage(int productId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(SELECT_MAIN_IMAGE);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToProductImage(rs);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting main image for product ID: " + productId, e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return null;
    }

    public int addImage(ProductImage image) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int imageId = -1;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(INSERT_IMAGE, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, image.getProductID());
            ps.setString(2, image.getImageUrl());
            ps.setString(3, image.getAltText());
            ps.setInt(4, image.getDisplayOrder());
            ps.setInt(5, image.getStatus());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    imageId = rs.getInt(1);
                    LOGGER.log(Level.INFO, "Product image added successfully with ID: {0}", imageId);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding product image", e);
        } finally {
            closeResources(conn, ps, rs);
        }
        return imageId;
    }

    public boolean updateImage(ProductImage image) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_IMAGE);
            ps.setString(1, image.getImageUrl());
            ps.setString(2, image.getAltText());
            ps.setInt(3, image.getDisplayOrder());
            ps.setInt(4, image.getStatus());
            ps.setInt(5, image.getImageID());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Product image updated successfully for ID: {0}", image.getImageID());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No product image found to update with ID: {0}", image.getImageID());
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating product image with ID: " + image.getImageID(), e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean deleteImage(int imageId) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(DELETE_IMAGE);
            ps.setInt(1, imageId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Product image deleted successfully for ID: {0}", imageId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No product image found to delete with ID: {0}", imageId);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting product image with ID: " + imageId, e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    public boolean updateDisplayOrder(int imageId, int displayOrder) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_DISPLAY_ORDER);
            ps.setInt(1, displayOrder);
            ps.setInt(2, imageId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Display order updated successfully for image ID: {0}", imageId);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No product image found to update display order with ID: {0}", imageId);
                return false;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating display order for image ID: " + imageId, e);
            return false;
        } finally {
            closeResources(conn, ps, null);
        }
    }

    private ProductImage mapResultSetToProductImage(ResultSet rs) throws SQLException {
        return new ProductImage(
            rs.getInt("imageId"),
            rs.getInt("productId"),
            rs.getString("imageUrl"),
            rs.getString("altText"),
            rs.getInt("displayOrder"),
            rs.getInt("status"),
            rs.getTimestamp("createdDate"),
            rs.getTimestamp("updatedDate")
        );
    }
} 