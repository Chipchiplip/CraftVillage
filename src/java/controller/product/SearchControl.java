/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.ProductService;
import service.CategoryService;
import entity.Product.Product;
import entity.Product.ProductCategory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SearchControl - Controller for product search functionality
 * Follows MVC pattern: Client → Controller → Service → DAO → Database
 */
@WebServlet(name = "SearchControl", urlPatterns = {"/search"})
public class SearchControl extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(SearchControl.class.getName());
    private final ProductService productService;
    private final CategoryService categoryService;
    
    public SearchControl() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        try {
            LOGGER.log(Level.INFO, "Processing search request");
            
            // Get search keyword from different possible parameters
            String searchKeyword = request.getParameter("txt");
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                searchKeyword = request.getParameter("name");
            }
            if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
                searchKeyword = "";
            }
            
            LOGGER.log(Level.INFO, "Search keyword: {0}", searchKeyword);
            
            // Controller calls Service layer for business logic
            List<Product> searchResults = productService.searchByName(searchKeyword);
            List<ProductCategory> categories = categoryService.getAllCategories();
            List<Product> newestProducts = productService.getTop5NewestProducts();
            List<Product> allProducts = productService.getAllProducts();
            
            // Handle price filtering and ordering
            String priceRange = request.getParameter("price");
            if (priceRange == null || priceRange.isEmpty()) {
                priceRange = "all";
            }
            
            String orderBy = request.getParameter("orderby");
            if (orderBy == null || orderBy.isEmpty()) {
                orderBy = "menu_order";
            }
            
            // Get filtered and ordered products if methods exist
            List<Product> filteredProducts = searchResults;
            try {
                // Try to use advanced filtering if available
                filteredProducts = productService.getProductsByNameAndPriceRangeAndOrder(
                    searchKeyword, priceRange, orderBy);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Advanced filtering not available, using basic search results", e);
                filteredProducts = searchResults;
            }
            
            // Set attributes for JSP
            request.setAttribute("productList", filteredProducts);
            request.setAttribute("listP", filteredProducts); // For compatibility
            request.setAttribute("listPP", allProducts);
            request.setAttribute("listCC", categories);
            request.setAttribute("list5", newestProducts);
            request.setAttribute("selectedPrice", priceRange);
            request.setAttribute("searchKeyword", searchKeyword);
            request.setAttribute("txt", searchKeyword); // For compatibility
            
            LOGGER.log(Level.INFO, "Search completed. Found {0} products for keyword: {1}", 
                      new Object[]{filteredProducts.size(), searchKeyword});
            
            // Forward to Category.jsp
            request.getRequestDispatcher("Category.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in search functionality", e);
            
            // Set error message and forward to error page or show empty results
            request.setAttribute("error", "Có lỗi xảy ra khi tìm kiếm sản phẩm: " + e.getMessage());
            request.setAttribute("productList", java.util.Collections.emptyList());
            request.setAttribute("listP", java.util.Collections.emptyList());
            request.setAttribute("listCC", java.util.Collections.emptyList());
            request.setAttribute("list5", java.util.Collections.emptyList());
            request.setAttribute("listPP", java.util.Collections.emptyList());
            
            request.getRequestDispatcher("Category.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "SearchControl - Handles product search requests following MVC pattern";
    }
} 