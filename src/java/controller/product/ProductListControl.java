package controller.product;

import java.io.IOException;
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
 * ProductListControl - Controller for product listing functionality
 * Follows MVC pattern: Client → Controller → Service → DAO → Database
 */
@WebServlet(name = "ProductListControl", urlPatterns = {"/productlist"})
public class ProductListControl extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(ProductListControl.class.getName());
    private final ProductService productService;
    private final CategoryService categoryService;
    
    public ProductListControl() {
        this.productService = new ProductService();
        this.categoryService = new CategoryService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        try {
            LOGGER.log(Level.INFO, "Processing product list request");
            
            String action = request.getParameter("action");
            String categoryId = request.getParameter("cid");
            
            List<Product> products;
            List<ProductCategory> categories = categoryService.getAllCategories();
            List<Product> newestProducts = productService.getTop5NewestProducts();
            
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                // Filter by category
                products = productService.getProductByCategoryID(categoryId);
                request.setAttribute("selectedCategoryId", categoryId);
                LOGGER.log(Level.INFO, "Loading products for category: {0}", categoryId);
            } else if ("newest".equals(action)) {
                // Show newest products
                products = newestProducts;
                LOGGER.log(Level.INFO, "Loading newest products");
            } else {
                // Show all products
                products = productService.getAllProducts();
                LOGGER.log(Level.INFO, "Loading all products");
            }
            
            // Set attributes for JSP
            request.setAttribute("listP", products);
            request.setAttribute("products", products); // For compatibility
            request.setAttribute("listCC", categories);
            request.setAttribute("categories", categories); // For compatibility
            request.setAttribute("list5", newestProducts);
            
            LOGGER.log(Level.INFO, "Product list loaded. Found {0} products", products.size());
            
            // Forward to Shop.jsp (using existing JSP)
            request.getRequestDispatcher("Shop.jsp").forward(request, response);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in product list functionality", e);
            
            // Set error message and forward with empty lists
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách sản phẩm: " + e.getMessage());
            request.setAttribute("listP", java.util.Collections.emptyList());
            request.setAttribute("listCC", java.util.Collections.emptyList());
            request.setAttribute("list5", java.util.Collections.emptyList());
            
            request.getRequestDispatcher("Shop.jsp").forward(request, response);
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
        return "ProductListControl - Handles product listing requests following MVC pattern";
    }
} 