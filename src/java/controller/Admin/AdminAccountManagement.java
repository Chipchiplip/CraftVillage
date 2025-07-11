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
import java.util.List;
import service.AccountService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminAccountManagement", urlPatterns = {"/admin-account-management"})
public class AdminAccountManagement extends HttpServlet {

    private List<Account> listAccount;

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
        listAccount = new AccountService().getAllAccounts();
        request.setAttribute("listAccount", listAccount);
        request.getRequestDispatcher("admin-account-management.jsp").forward(request, response);
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
        AccountService aService = new AccountService();
        String typeName = request.getParameter("typeName");
        String userID = request.getParameter("userID");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String roleID = request.getParameter("roleID");
        String status = request.getParameter("status");
        String searchID = request.getParameter("searchID");
        String contentSearch = request.getParameter("contentSearch");

        switch (typeName) {
            case "updateProfile":
                try {
                    Account account = new Account(Integer.parseInt(userID), userName, password, email, address, phoneNumber, Integer.parseInt(status), Integer.parseInt(roleID), fullName);
                    boolean result = aService.updateProfile(account);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Update Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Update error Email or phone number already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Update Fail");
                }
                listAccount = new AccountService().getAllAccounts();
                request.setAttribute("listAccount", listAccount);
                break;
            case "addAccount":
                try {
                    Account account = new Account(userName, password, email, address, phoneNumber, Integer.parseInt(status), Integer.parseInt(roleID), fullName);
                    boolean result = aService.addNewAccountFull(account);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Add Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Add error Email or phone number already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Add Fail");
                }
                listAccount = new AccountService().getAllAccounts();
                request.setAttribute("listAccount", listAccount);
                break;
            case "searchAccount":
                try {
                    listAccount = new AccountService().getSearchAccount(Integer.parseInt(status), Integer.parseInt(searchID), contentSearch);
                    request.setAttribute("error", "1");
                    request.setAttribute("message", "Search Success");
                    request.setAttribute("listAccount", listAccount);
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Search Fail");
                }
                break;
            default:
                throw new AssertionError();
        }
        request.getRequestDispatcher("admin-account-management.jsp").forward(request, response);
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
