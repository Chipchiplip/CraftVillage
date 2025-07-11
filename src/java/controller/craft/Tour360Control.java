package controller.craft;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import service.ProductService;
import service.IProductService;
import entity.Product.Product;

@WebServlet(name = "Tour360Control", urlPatterns = {"/tour360"})
public class Tour360Control extends HttpServlet {
    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy villageID từ request parameter
        String villageIdStr = request.getParameter("villageID");
        int villageId = 1; // Default to village 1 if not specified
        
        if (villageIdStr != null && !villageIdStr.isEmpty()) {
            try {
                villageId = Integer.parseInt(villageIdStr);
            } catch (NumberFormatException e) {
                // Log error and keep default value
                System.out.println("Invalid villageID: " + villageIdStr);
            }
        }
        
        // Lấy danh sách sản phẩm active theo làng
        List<Product> products = productService.getActiveProductsBySellID(villageId);
        
        // Debug log
        System.out.println("[DEBUG] Village ID: " + villageId);
        System.out.println("[DEBUG] Number of products loaded: " + (products != null ? products.size() : 0));
        if (products != null) {
            for (Product p : products) {
                System.out.println("[DEBUG] Product: " + p.getName() + " - Price: " + p.getPrice());
            }
        }
        
        // Set danh sách sản phẩm vào request
        request.setAttribute("products", products);
        
        // Forward đến trang tour360.jsp với đường dẫn đầy đủ
        request.getRequestDispatcher("tour360.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 