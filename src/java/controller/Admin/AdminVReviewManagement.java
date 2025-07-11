/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.CraftVillage.CraftReview;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.ReviewService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminVReviewManagement", urlPatterns = {"/admin-vreview-management"})
public class AdminVReviewManagement extends HttpServlet {

    List<CraftReview> listVReview;
    ReviewService rService = new ReviewService();

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminVReviewManagement</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminVReviewManagement at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        String villageID = request.getParameter("villageID");
        String villageName = request.getParameter("villageName");
        
        listVReview = rService.getAllVillageReviewByAdmin(Integer.parseInt(villageID));
        request.setAttribute("listPReview", listVReview);
        
        request.setAttribute("listReviewToday", rService.searchVillageReviewToday(Integer.parseInt(villageID)));
        
        request.setAttribute("villageID", villageID);
        request.setAttribute("villageName", villageName);
        request.getRequestDispatcher("admin-vreview-management.jsp").forward(request, response);
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
        String typeName = request.getParameter("typeName");
        String reviewID = request.getParameter("reviewID");
        String responseText = request.getParameter("responseText");
        String userID = request.getParameter("userID");
        String villageID = request.getParameter("villageID");
        String villageName = request.getParameter("villageName");

        switch (typeName) {
            case "deleteReview":
                try {
                    boolean result = rService.deleteVillageReviewByAdmin(Integer.parseInt(reviewID));
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Delete Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Delete Fail");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Delete Fail");
                }
                listVReview =  rService.getAllVillageReviewByAdmin(Integer.parseInt(villageID));
                request.setAttribute("listVReview", listVReview);
                break;
            case "respondReview":
                try {
                    boolean result = rService.responseVillageReviewByAdmin(Integer.parseInt(reviewID), responseText);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Response Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Response Fail");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Response Fail");
                }
                listVReview = rService.getAllVillageReviewByAdmin(Integer.parseInt(villageID));
                request.setAttribute("listVReview", listVReview);
                break;
            case "searchReview":
                try {
                    listVReview = rService.searchVillageReviewByAdmin(Integer.parseInt(userID));
                    if (!listVReview.isEmpty()) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Search Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "No comments");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Response Fail");
                } 
                request.setAttribute("listReviewToday", rService.searchProductReviewToday(Integer.parseInt(villageID)));
                request.setAttribute("listVReview", listVReview);
                break;
            default:
                throw new AssertionError();
        }
        request.setAttribute("villageName", villageName);
        request.setAttribute("villageID", villageID);
        request.getRequestDispatcher("admin-vreview-management.jsp").forward(request, response);
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
