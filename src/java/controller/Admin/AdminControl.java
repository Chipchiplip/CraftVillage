/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.Account.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import service.AccountService;
import service.PaymentService;
import service.ReportService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminControl", urlPatterns = {"/admin"})
public class AdminControl extends HttpServlet {

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
        ReportService rService = new ReportService();
        AccountService aService = new AccountService();
        PaymentService pService = new PaymentService();
        List<Account> accountCount = aService.getAllAccounts();
        request.setAttribute("accountCount", accountCount);
        Map<Integer, BigDecimal> revenueMap = pService.getRevenueByYear(Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("revenueMap", revenueMap);
        Map<Integer, Integer> monthlyRegister = rService.getMonthlyAccountRegistrations(Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("monthlyRegister", monthlyRegister);
        Map<Integer, Integer> statusOrder = rService.getOrderStatusSummaryByMonthYear(Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("statusOrder", statusOrder);
        BigDecimal currentRevenue = rService.getRevenueByDayMonthYear(Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("currentRevenue", currentRevenue);
        int currentPost = rService.getNumberCraftPostByDayMonthYear(Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("currentPost", currentPost);
        int currentProductPost = rService.getNumberProductPostByDayMonthYear(Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("currentProductPost", currentProductPost);
        int currentTicketPost = rService.getNumberTicketPostByDayMonthYear(Calendar.getInstance().get(Calendar.DATE),Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
        request.setAttribute("currentTicketPost", currentTicketPost);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
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
        processRequest(request, response);
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
