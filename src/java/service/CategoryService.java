package service;

import DAO.CategoryDAO;
import entity.Product.ProductCategory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CategoryService - Business Logic Layer for Category
 * Dedicated service for category operations following MVC pattern
 * Provides proper error handling and business logic
 */
public class CategoryService {
    
    private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());
    private final CategoryDAO categoryDAO;
    
    /**
     * Constructor with dependency injection
     */
    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }
    
    /**
     * Get all active categories with business logic validation
     */
    public List<ProductCategory> getAllCategories() {
        try {
            LOGGER.log(Level.INFO, "Getting all categories");
            List<ProductCategory> categories = categoryDAO.getAllCategories();
            
            if (categories.isEmpty()) {
                LOGGER.log(Level.WARNING, "No categories found in database");
            } else {
                LOGGER.log(Level.INFO, "Successfully retrieved {0} categories", categories.size());
            }
            
            return categories;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CategoryService.getAllCategories()", e);
            throw new RuntimeException("Service error while retrieving categories", e);
        }
    }
    
    /**
     * Get category by ID with validation
     */
    public ProductCategory getCategoryById(int categoryID) {
        if (categoryID <= 0) {
            throw new IllegalArgumentException("Category ID must be positive");
        }
        
        try {
            LOGGER.log(Level.INFO, "Getting category by ID: {0}", categoryID);
            ProductCategory category = categoryDAO.getCategoryById(categoryID);
            
            if (category == null) {
                LOGGER.log(Level.WARNING, "Category not found for ID: {0}", categoryID);
            } else {
                LOGGER.log(Level.INFO, "Found category: {0}", category.getCategoryName());
            }
            
            return category;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CategoryService.getCategoryById() for ID: " + categoryID, e);
            throw new RuntimeException("Service error while retrieving category by ID: " + categoryID, e);
        }
    }
    
    /**
     * Search categories by name
     */
    public List<ProductCategory> searchCategoriesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }
        
        try {
            LOGGER.log(Level.INFO, "Searching categories by name: {0}", name);
            List<ProductCategory> categories = categoryDAO.getCategoriesByName(name.trim());
            
            LOGGER.log(Level.INFO, "Found {0} categories for search term: {1}", 
                      new Object[]{categories.size(), name});
            
            return categories;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CategoryService.searchCategoriesByName() for name: " + name, e);
            throw new RuntimeException("Service error while searching categories by name: " + name, e);
        }
    }
    
    /**
     * Check if category exists and is active
     */
    public boolean categoryExists(int categoryID) {
        if (categoryID <= 0) {
            return false;
        }
        
        try {
            LOGGER.log(Level.INFO, "Checking if category exists: {0}", categoryID);
            boolean exists = categoryDAO.categoryExists(categoryID);
            
            LOGGER.log(Level.INFO, "Category {0} exists: {1}", new Object[]{categoryID, exists});
            return exists;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in CategoryService.categoryExists() for ID: " + categoryID, e);
            return false; // Default to false on error for safety
        }
    }
    
    /**
     * Get category name by ID (helper method for display)
     */
    public String getCategoryNameById(int categoryID) {
        try {
            ProductCategory category = getCategoryById(categoryID);
            return category != null ? category.getCategoryName() : "Unknown Category";
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error getting category name for ID: " + categoryID, e);
            return "Unknown Category";
        }
    }
    
    /**
     * Validate category data (useful for admin operations)
     */
    public boolean isValidCategory(ProductCategory category) {
        if (category == null) {
            return false;
        }
        
        return category.getCategoryName() != null && 
               !category.getCategoryName().trim().isEmpty() &&
               category.getStatus() == 1;
    }
} 