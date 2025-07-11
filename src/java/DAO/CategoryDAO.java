package DAO;

import context.DBContext;
import entity.Product.ProductCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO - Data Access Object for ProductCategory
 */
public class CategoryDAO {

    /**
     * Get all active categories from ProductCategory table
     */
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductCategory WHERE status = 1 ORDER BY categoryName";
        
        try {
            Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("description"));
                category.setStatus(rs.getInt("status"));
                category.setCreatedDate(rs.getTimestamp("createdDate"));
                category.setUpdatedDate(rs.getTimestamp("updatedDate"));
                list.add(category);
            }
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get category by ID
     */
    public ProductCategory getCategoryById(int categoryID) {
        String sql = "SELECT * FROM ProductCategory WHERE categoryID = ? AND status = 1";
        
        try {
            Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("description"));
                category.setStatus(rs.getInt("status"));
                category.setCreatedDate(rs.getTimestamp("createdDate"));
                category.setUpdatedDate(rs.getTimestamp("updatedDate"));
                
                rs.close();
                ps.close();
                conn.close();
                return category;
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get categories by name (for searching)
     */
    public List<ProductCategory> getCategoriesByName(String name) {
        List<ProductCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductCategory WHERE categoryName LIKE ? AND status = 1";
        
        try {
            Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setCategoryID(rs.getInt("categoryID"));
                category.setCategoryName(rs.getString("categoryName"));
                category.setDescription(rs.getString("description"));
                category.setStatus(rs.getInt("status"));
                category.setCreatedDate(rs.getTimestamp("createdDate"));
                category.setUpdatedDate(rs.getTimestamp("updatedDate"));
                list.add(category);
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Check if category exists and is active
     */
    public boolean categoryExists(int categoryID) {
        String sql = "SELECT COUNT(*) FROM ProductCategory WHERE categoryID = ? AND status = 1";
        
        try {
            Connection conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                ps.close();
                conn.close();
                return count > 0;
            }
            
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
} 