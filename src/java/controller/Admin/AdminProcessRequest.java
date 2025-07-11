/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.Account.SellerVerification;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.SellerVerificationService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminProcessRequest", urlPatterns = {"/admin-process-request"})
public class AdminProcessRequest extends HttpServlet {
    
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AdminProcessRequest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminProcessRequest at " + request.getContextPath() + "</h1>");
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
        List<SellerVerification> listRequest = sService.getSellerVertificationFormByAdmin(3);
        request.setAttribute("listRequest", listRequest);
        request.getRequestDispatcher("admin-process-request.jsp").forward(request, response);
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
        String verificationID = request.getParameter("verificationID");
        
        String businessVillageCategry = request.getParameter("businessVillageCategry");
        
        String sellerID = request.getParameter("sellerID");
        String businessVillageName = request.getParameter("businessVillageName");
        String businessVillageAddress = request.getParameter("businessVillageAddress");
        String profileVillagePictureUrl = request.getParameter("profileVillagePictureUrl");
        String contactPerson = request.getParameter("contactPerson");
        String contactPhone = request.getParameter("contactPhone");
        String contactEmail = request.getParameter("contactEmail");
        
        String productProductCategory = request.getParameter("productProductCategory");
        
        String verificationStatus = request.getParameter("verificationStatus");
        String verifiedBy = request.getParameter("adminID");
        String rejectReason = request.getParameter("rejectReason");
        
        boolean result = false;
        
        switch (typeName) {
            case "processRequest":
                if (Integer.parseInt(verificationStatus) == 2) {
                    //Rejected
                    result = sService.rejectedUpgradeAccount(new SellerVerification(Integer.parseInt(verificationID), Integer.parseInt(verificationStatus), Integer.parseInt(verifiedBy), rejectReason));
                } else {
                    //Approved
                    result = sService.approvedUpgradeAccount(new SellerVerification(Integer.parseInt(verificationID), Integer.parseInt(sellerID), businessVillageCategry, businessVillageName, businessVillageAddress, productProductCategory, profileVillagePictureUrl, contactPerson, contactPhone, contactEmail, Integer.parseInt(verificationStatus), Integer.parseInt(verifiedBy)));
                }
                if (result) {
                    request.setAttribute("error", "1");
                    request.setAttribute("message", "Update success");
                } else {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Update fail");
                }
                request.setAttribute("listRequest", sService.getSellerVertificationFormByAdmin(3));
                break;
            case "searchRequest":
                request.setAttribute("listRequest", sService.getSellerVertificationFormByAdmin(Integer.parseInt(verificationStatus)));
                break;            
            default:
                throw new AssertionError();
        }
        
        request.getRequestDispatcher("admin-process-request.jsp").forward(request, response);
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
