package controller.product;

import entity.Product.ProductCategory;
import service.ProductService;
import service.CategoryService;
import entity.Product.Product;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ProductControl", urlPatterns = {"/product"})
public class ProductControl extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProductControl.class.getName());
    private ProductService productService;
    private CategoryService categoryService;

    /**
     * Constructor with dependency injection
     */
    public ProductControl() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // Get action parameter to determine what to do
        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            // Default behavior - show products with filtering
            handleDefaultProductList(request, response);
        } else {
            // Handle specific actions
            handleActionRequest(request, response, action);
        }
    }

    /**
     * Handle default product list with price filtering and ordering
     */
    private void handleDefaultProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get sorting and filtering parameters
        String priceRange = request.getParameter("price");
        String orderBy = request.getParameter("orderby");
        
        // Set default values if parameters are null
        if (priceRange == null || priceRange.isEmpty()) {
            priceRange = "all";
        }
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "menu_order";
        }
        
        // Get products with sorting and filtering
        List<Product> listP = productService.getProductsByNameAndPriceRangeAndOrder("", priceRange, orderBy);
        
        // Get top 5 newest products for sidebar
        List<Product> list5 = productService.getTop5NewestProducts();
        
        // Get categories for mapping categoryID to categoryName
        List<ProductCategory> categories = categoryService.getAllCategories();
        
        // Set attributes for JSP
        request.setAttribute("listP", listP);
        request.setAttribute("list5", list5);
        request.setAttribute("listCC", categories);
        request.setAttribute("selectedPrice", priceRange);
        
        // Forward to product.jsp
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    /**
     * Handle action-based requests
     */
    private void handleActionRequest(HttpServletRequest request, HttpServletResponse response, String action)
            throws ServletException, IOException {
        
        try {
            switch (action) {
                case "list":
                    handleListProducts(request, response);
                    break;
                case "detail":
                    handleProductDetail(request, response);
                    break;
                case "category":
                    handleProductsByCategory(request, response);
                    break;
                case "search":
                    handleSearchProducts(request, response);
                    break;
                case "newest":
                    handleNewestProducts(request, response);
                    break;
                default:
                    handleListProducts(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ProductControl for action: " + action, e);
            handleError(request, response, "An error occurred while processing your request.");
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
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "create";
        }
        
        try {
            switch (action) {
                case "create":
                    handleCreateProduct(request, response);
                    break;
                case "update":
                    handleUpdateProduct(request, response);
                    break;
                case "delete":
                    handleDeleteProduct(request, response);
                    break;
                default:
                    handleError(request, response, "Invalid action specified.");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in ProductControl.doPost() for action: " + action, e);
            handleError(request, response, "An error occurred while processing your request.");
        }
    }

    /**
     * Handles listing all active products
     */
    private void handleListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.log(Level.INFO, "Handling list products request");
        
        // Controller calls Service
        List<Product> products = productService.getActivateProducts();
        List<ProductCategory> categories = categoryService.getAllCategories();
        
        // Get top 5 newest products for sidebar
        List<Product> list5 = productService.getTop5NewestProducts();
        
        // Set data for JSP
        request.setAttribute("products", products);
        request.setAttribute("listP", products); // For compatibility
        request.setAttribute("categories", categories);
        request.setAttribute("listCC", categories); // For compatibility
        request.setAttribute("list5", list5);
        
        // Forward to JSP
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }
    
    /**
     * Handles product detail view
     */
    private void handleProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");
        LOGGER.log(Level.INFO, "Handling product detail request for ID: {0}", productId);
        
        if (productId == null || productId.trim().isEmpty()) {
            handleError(request, response, "Product ID is required.");
            return;
        }
        
        // Controller calls Service
        Product product = productService.getProductByID(productId);
        
        if (product == null) {
            handleError(request, response, "Product not found.");
            return;
        }
        
        // Set data for JSP
        request.setAttribute("product", product);
        
        // Forward to product detail JSP
        request.getRequestDispatcher("productDetail.jsp").forward(request, response);
    }

    /**
     * Handles products by category
     */
    private void handleProductsByCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String categoryId = request.getParameter("cid");
        LOGGER.log(Level.INFO, "Handling products by category request for category ID: {0}", categoryId);
        
        if (categoryId == null || categoryId.trim().isEmpty()) {
            handleError(request, response, "Category ID is required.");
            return;
        }
        
        // Controller calls Service
        List<Product> products = productService.getProductByCategoryID(categoryId);
        List<ProductCategory> categories = categoryService.getAllCategories();
        List<Product> list5 = productService.getTop5NewestProducts();
        
        // Set data for JSP
        request.setAttribute("products", products);
        request.setAttribute("listP", products);
        request.setAttribute("categories", categories);
        request.setAttribute("listCC", categories);
        request.setAttribute("list5", list5);
        request.setAttribute("selectedCategoryId", categoryId);
        
        // Forward to JSP
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    /**
     * Handles search products
     */
    private void handleSearchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchQuery = request.getParameter("search");
        String priceRange = request.getParameter("price");
        String orderBy = request.getParameter("orderby");
        
        LOGGER.log(Level.INFO, "Handling search products request with query: {0}", searchQuery);
        
        // Set default values
        if (priceRange == null || priceRange.isEmpty()) {
            priceRange = "all";
        }
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = "menu_order";
        }
        
        List<Product> products;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Use advanced search with filtering
            products = productService.getProductsByNameAndPriceRangeAndOrder(searchQuery, priceRange, orderBy);
        } else {
            // Default search
            products = productService.getActivateProducts();
        }
        
        List<ProductCategory> categories = categoryService.getAllCategories();
        List<Product> list5 = productService.getTop5NewestProducts();
        
        // Set data for JSP
        request.setAttribute("products", products);
        request.setAttribute("listP", products);
        request.setAttribute("categories", categories);
        request.setAttribute("listCC", categories);
        request.setAttribute("list5", list5);
        request.setAttribute("searchQuery", searchQuery);
        request.setAttribute("selectedPrice", priceRange);
        
        // Forward to JSP
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    /**
     * Handles newest products
     */
    private void handleNewestProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.log(Level.INFO, "Handling newest products request");
        
        // Controller calls Service
        List<Product> products = productService.getTop5NewestProducts();
        List<ProductCategory> categories = categoryService.getAllCategories();
        
        // Set data for JSP
        request.setAttribute("products", products);
        request.setAttribute("listP", products);
        request.setAttribute("categories", categories);
        request.setAttribute("listCC", categories);
        request.setAttribute("list5", products);
        
        // Forward to JSP
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    /**
     * Handles create product (admin functionality)
     */
    private void handleCreateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This would be admin functionality - placeholder for future implementation
        LOGGER.log(Level.INFO, "Create product functionality not implemented yet");
        handleError(request, response, "Create product functionality is not available yet.");
    }

    /**
     * Handles update product (admin functionality)
     */
    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This would be admin functionality - placeholder for future implementation
        LOGGER.log(Level.INFO, "Update product functionality not implemented yet");
        handleError(request, response, "Update product functionality is not available yet.");
    }

    /**
     * Handles delete product (admin functionality)
     */
    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This would be admin functionality - placeholder for future implementation
        LOGGER.log(Level.INFO, "Delete product functionality not implemented yet");
        handleError(request, response, "Delete product functionality is not available yet.");
    }

    /**
     * Handles error responses
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        
        LOGGER.log(Level.WARNING, "Error occurred: {0}", errorMessage);
        
        request.setAttribute("error", errorMessage);
        request.setAttribute("listP", java.util.Collections.emptyList());
        request.setAttribute("listCC", java.util.Collections.emptyList());
        request.setAttribute("list5", java.util.Collections.emptyList());
        
        request.getRequestDispatcher("product.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "ProductControl - Main controller for product functionality following MVC pattern";
    }
} 