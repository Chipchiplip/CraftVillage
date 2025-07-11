/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import entity.Product.Product;
import entity.Product.ProductCategory;
import entity.Product.ProductReview;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pc
 */
public interface IProductService {

    //CRUD Basic
    List<Product> getActivateProducts();
    
    int addProduct(Product product) throws Exception;

    boolean updateProduct(Product product);

    boolean deleteProductByAdmin(int productId);

    Product getProductById(int productId);

    List<Product> getAllProducts();

    boolean updateProductByAdmin(Product product);

    boolean addProductByAdmin(Product product);

    boolean createProductByAdmin(Product product);

    //Search & Filter
    List<Product> searchProducts(String keyword); // theo tên/mô tả

    List<Product> getSearchProductByAdmin(int status, int searchID, String contentSearch);
    
    List<Product> getProductsByCategory(int categoryId);

    List<Product> getProductsByVillage(int villageId);

    List<Product> getProductsByCraftType(int typeId);

    List<Product> getFeaturedProducts(); // SP nổi bật

    List<Product> getAvailableProducts(); // SP còn hàng

    List<Product> getAllProductActive(); // Lay cac san pham hoat dong

    List<Product> getAllProductActiveByAdmin();
    
    List<Product> getProductByCategory(int categoryID);

    List<Product> getProductOutOfStockByAdmin();
    
    String getCategoryNameByCategoryID(int categoryID);
    
    List<Product> getTopRatedByAdmin();
    //Report for Admin
    Map<String, Integer> getProductCountByCategory(); // Pie chart

    List<Product> getTopClickedProducts(int limit);

    List<Product> getTopRatedProducts(int limit);

    boolean updateClickCount(int productId); // tăng clickCount

    boolean updateStock(int productId, int quantityChange); // trừ khi mua

    //Review
    List<String> getImageUrlsByProductId(int productId);

    List<ProductReview> getReviewsByProductId(int productId);

    double calculateAverageRating(int productId);

    //Report for Seller
    List<Product> getProductsBySellerId(int sellerId);

    //Category
    List<ProductCategory> getAllCategory();

    List<Product> getTop5NewestProducts();
}
