/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.Product.Product;
import entity.Ticket.Ticket;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import service.ProductService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminProductManagement", urlPatterns = {"/admin-product-management"})
public class AdminProductManagement extends HttpServlet {

    private List<Product> listProduct;
    List<Ticket> listTicket;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listProduct = new ProductService().getAllProductActiveByAdmin();
        request.setAttribute("listProduct", listProduct);
        request.getRequestDispatcher("admin-product-management.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductService pService = new ProductService();
        String typeName = request.getParameter("typeName");
        String pidStr = request.getParameter("pid");
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        String stockStr = request.getParameter("stock");
        String stockAddStr = request.getParameter("stockAdd");
        String sku = request.getParameter("sku");
        String villageIDStr = request.getParameter("villageID");
        String categoryIDStr = request.getParameter("categoryID");
        String craftTypeIDStr = request.getParameter("craftTypeID");
        String mainImageUrl = request.getParameter("mainImageUrl");
        String weightStr = request.getParameter("weight");
        String dimensions = request.getParameter("dimensions");
        String materials = request.getParameter("materials");
        String careInstructions = request.getParameter("careInstructions");
        String warranty = request.getParameter("warranty");
        String status = request.getParameter("status");
        String searchID = request.getParameter("searchID");
        String contentSearch = request.getParameter("contentSearch");

        switch (typeName) {
            case "updateProduct":
                try {
                    Product product = new Product(Integer.parseInt(pidStr), name, BigDecimal.valueOf(Double.parseDouble(priceStr)), description, Integer.parseInt(stockStr), Integer.parseInt(stockAddStr), Integer.parseInt(status), Integer.parseInt(villageIDStr), Integer.parseInt(categoryIDStr), mainImageUrl, Integer.parseInt(craftTypeIDStr), sku, BigDecimal.valueOf(Double.parseDouble(weightStr)), dimensions, materials, careInstructions, warranty);
                    boolean result = pService.updateProductByAdmin(product);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Update Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Update error Name Product already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Update Fail");
                }
                listProduct = pService.getAllProductActive();
                request.setAttribute("listProduct", listProduct);
                break;
            case "createProduct":
                try {
                    Product product = new Product(name, BigDecimal.valueOf(Double.parseDouble(priceStr)), description, Integer.parseInt(stockStr), Integer.parseInt(status), Integer.parseInt(villageIDStr), Integer.parseInt(categoryIDStr), mainImageUrl, Integer.parseInt(craftTypeIDStr), sku, BigDecimal.valueOf(Double.parseDouble(weightStr)), dimensions, materials, careInstructions, warranty);
                    boolean result = pService.createProductByAdmin(product);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Create Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Create Fail: Name Product already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Create Fail");
                }
                listProduct = pService.getAllProductActive();
                request.setAttribute("listProduct", listProduct);
                break;
            case "deleteProduct":
                try {
                    boolean result = pService.deleteProductByAdmin(Integer.parseInt(pidStr));
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Delete Success");
                    } else {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Deactive success");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Delete Fails");
                }
                listProduct = pService.getAllProductActive();
                request.setAttribute("listProduct", listProduct);
                break;
            case "searchProduct":
                try {
                    listProduct = new ProductService().getSearchProductByAdmin(Integer.parseInt(status), Integer.parseInt(searchID), contentSearch);
                    request.setAttribute("error", "1");
                    request.setAttribute("message", "Search Success");
                    request.setAttribute("listProduct", listProduct);
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Search Fail");
                }
                break;
            default:
                throw new AssertionError();
        }
        request.getRequestDispatcher("admin-product-management.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
