/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import DAO.ProductDAO;
import entity.Product.Product;
import entity.Product.ProductCategory;
import entity.Product.ProductReview;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 *
 * @author ACER
 */
public class ProductService implements IProductService{

    ProductDAO pDAO = new ProductDAO();
    
    @Override
    public int addProduct(Product product) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateProduct(Product product){
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Product getProductById(int productId) {
        return pDAO.getProductByID(String.valueOf(productId));
    }

    public Product getProductByID(String id) {
        return pDAO.getProductByID(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return pDAO.getAllProducts();
    }

    public List<Product> getProductByCategoryID(String categoryId) {
        return pDAO.getProductByCategoryID(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return pDAO.searchByName(keyword);
    }
    
    /**
     * Search products by name - additional method for compatibility
     */
    public List<Product> searchByName(String name) {
        return pDAO.searchByName(name);
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        return pDAO.getProductByCategoryID(String.valueOf(categoryId));
    }

    @Override
    public List<Product> getProductsByVillage(int villageId) {
        return pDAO.getProductsByVillage(villageId);
    }

    @Override
    public List<Product> getProductsByCraftType(int typeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getFeaturedProducts() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getAvailableProducts() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Map<String, Integer> getProductCountByCategory() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getTopClickedProducts(int limit) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getTopRatedProducts(int limit) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateClickCount(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean updateStock(int productId, int quantityChange) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getImageUrlsByProductId(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ProductReview> getReviewsByProductId(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double calculateAverageRating(int productId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getProductsBySellerId(int sellerId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getAllProductActive() {
        return pDAO.getAllProductActive();
    }

    @Override
    public List<Product> getActivateProducts() {
        return pDAO.getAllProductActive();
    }
    
    @Override
    public List<ProductCategory> getAllCategory() {
        return pDAO.getAllCategory();
    }

    // Alias method for compatibility
    public List<entity.Product.Category> getAllCategories() {
        // Convert ProductCategory to Category if needed, or return as is
        List<ProductCategory> categories = getAllCategory();
        // For now, just return an empty list or handle conversion
        return new java.util.ArrayList<>();
    }

    // Additional compatibility method
    public List<Product> getActiveProductsBySellID(int sellerId) {
        // Return products by seller ID (for now, return empty list)
        return new ArrayList<>();
    }

    @Override
    public List<Product> getTop5NewestProducts() {
        return pDAO.getTop5NewestProducts();
    }

    @Override
    public List<Product> getAllProductActiveByAdmin() {
        return pDAO.getAllProductActiveByAdmin();
    }
    
    @Override
    public boolean addProductByAdmin(Product product) {
        return true;
    }
    
    @Override
    public boolean updateProductByAdmin(Product product){
        return pDAO.updateProductByAdmin(product);
    }
    
    @Override
    public boolean createProductByAdmin(Product product){
        return pDAO.createProductByAdmin(product);
    }
    
    @Override
    public boolean deleteProductByAdmin(int productId) {
        return pDAO.deleteProductByAdmin(productId);
    }
    
    @Override
    public List<Product> getSearchProductByAdmin(int status, int searchID, String contentSearch) {
        return pDAO.getSearchProductByAdmin(status, searchID, contentSearch);
    }

    @Override
    public List<Product> getProductByCategory(int categoryID) {
        return pDAO.getProductByCategory(categoryID);
    }

    @Override
    public List<Product> getProductOutOfStockByAdmin() {
        return pDAO.getProductOutOfStockByAdmin();
    }

    @Override
    public String getCategoryNameByCategoryID(int categoryID) {
        return pDAO.getCategoryNameByCategoryID(categoryID);
    }

    @Override
    public List<Product> getTopRatedByAdmin() {
        return pDAO.getTopRatedByAdmin();
    }
    
    /**
     * Get products by name with price range and order filtering
     * This method provides advanced filtering capabilities
     */
    public List<Product> getProductsByNameAndPriceRangeAndOrder(String name, String priceRange, String orderBy) {
        try {
            // For now, just return basic search results
            // Advanced filtering can be implemented in DAO layer later
            List<Product> products = searchByName(name);
            
            // Basic price filtering
            if (priceRange != null && !priceRange.equals("all")) {
                products = filterByPriceRange(products, priceRange);
            }
            
            // Basic ordering
            if (orderBy != null && !orderBy.equals("menu_order")) {
                products = orderProducts(products, orderBy);
            }
            
            return products;
        } catch (Exception e) {
            // Fallback to basic search if filtering fails
            return searchByName(name);
        }
    }
    
    /**
     * Filter products by price range
     */
    private List<Product> filterByPriceRange(List<Product> products, String priceRange) {
        // Basic implementation - can be enhanced
        return products.stream()
            .filter(product -> {
                double price = product.getPrice().doubleValue();
                switch (priceRange) {
                    case "0-100000":
                        return price <= 100000;
                    case "100000-500000":
                        return price > 100000 && price <= 500000;
                    case "500000-1000000":
                        return price > 500000 && price <= 1000000;
                    case "1000000+":
                        return price > 1000000;
                    default:
                        return true;
                }
            })
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Order products by specified criteria
     */
    private List<Product> orderProducts(List<Product> products, String orderBy) {
        // Basic implementation - can be enhanced
        switch (orderBy) {
            case "price_asc":
                products.sort((p1, p2) -> Double.compare(p1.getPrice().doubleValue(), p2.getPrice().doubleValue()));
                break;
            case "price_desc":
                products.sort((p1, p2) -> Double.compare(p2.getPrice().doubleValue(), p1.getPrice().doubleValue()));
                break;
            case "name_asc":
                products.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                break;
            case "name_desc":
                products.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
                break;
            default:
                // menu_order - keep original order
                break;
        }
        return products;
    }
}
