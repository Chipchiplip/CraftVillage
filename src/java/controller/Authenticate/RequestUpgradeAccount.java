/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Authenticate;

import DAO.CraftVillageDAO;
import DAO.ProductDAO;
import entity.Account.SellerVerification;
import entity.CraftVillage.CraftType;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.SellerVerificationService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "RequestUpgradeAccount", urlPatterns = {"/request-upgrade"})
public class RequestUpgradeAccount extends HttpServlet {

    SellerVerificationService sService = new SellerVerificationService();

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
        request.getRequestDispatcher("UpgradeAccount.jsp").forward(request, response);
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
        String userID = request.getParameter("userID");
        String businessType = request.getParameter("businessType");
        String businessVillageCategrySelect = request.getParameter("businessVillageCategrySelect");
        String businessVillageCategry = request.getParameter("businessVillageCategry");
        String businessVillageName = request.getParameter("businessVillageName");
        String businessVillageAddress = request.getParameter("businessVillageAddress");
        String productProductCategorySelect = request.getParameter("productProductCategorySelect");
        String productProductCategory = request.getParameter("productProductCategory");
        String profileVillagePictureUrl = request.getParameter("profileVillagePictureUrl");
        String contactPerson = request.getParameter("contactPerson");
        String contactPhone = request.getParameter("contactPhone");
        String contactEmail = request.getParameter("contactEmail");
        String idCardNumber = request.getParameter("idCardNumber");
        String idCardFrontUrl = request.getParameter("idCardFrontUrl");
        String idCardBackUrl = request.getParameter("idCardBackUrl");
        String businessLicense = request.getParameter("businessLicense");
        String taxCode = request.getParameter("taxCode");
        String documentUrl = request.getParameter("documentUrl");
        String note = request.getParameter("note");

        try {
            boolean result = false;
            if (!businessVillageCategrySelect.isEmpty()) {
                    businessVillageCategry = new CraftVillageDAO().getCraftTypeNameByID(Integer.parseInt(businessVillageCategrySelect));
                }
                if (!productProductCategorySelect.isEmpty()) {
                    productProductCategory = new ProductDAO().getCategoryNameByCategoryID(Integer.parseInt(productProductCategorySelect));
                }
            if (businessType.equals("Individual")) {             
                result = sService.requestUpgradeForIndividual(new SellerVerification(Integer.parseInt(userID), businessType, businessVillageCategry, businessVillageName, businessVillageAddress, productProductCategory, profileVillagePictureUrl, contactPerson, contactPhone, contactEmail, idCardNumber, idCardFrontUrl, idCardBackUrl, note));
            }
            else {
                result = sService.requestUpgradeForCraftVillage(new SellerVerification(businessType, Integer.parseInt(userID), businessVillageCategry, businessVillageName, businessVillageAddress, productProductCategory, profileVillagePictureUrl, contactPerson, contactPhone, contactEmail, businessLicense, taxCode, documentUrl, note));
            }
            if (result == true) {
                request.setAttribute("error", "1");
                request.setAttribute("message", "Send request success");
            } else {
                request.setAttribute("error", "0");
                request.setAttribute("message", "Village Name already existed");
            }

        } catch (Exception e) {
            request.setAttribute("error", "0");
            request.setAttribute("message", "System error");
        }
        request.getRequestDispatcher("UpgradeAccount.jsp").forward(request, response);
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
