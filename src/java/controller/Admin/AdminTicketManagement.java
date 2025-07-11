/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.Ticket.Ticket;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import service.TicketService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminTicketManagement", urlPatterns = {"/admin-ticket-management"})
public class AdminTicketManagement extends HttpServlet {

    List<Ticket> listTicket;
    TicketService tService = new TicketService();
    
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
        
        listTicket = new TicketService().getAllTicketActive();
        request.setAttribute("listTicket", listTicket);
        request.getRequestDispatcher("admin-ticket-management.jsp").forward(request, response);
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
      
        String typeName =request.getParameter("typeName");
        
        String ticketID = request.getParameter("ticketID");
        String villageID = request.getParameter("villageID");
        String typeID = request.getParameter("typeID");
        String price = request.getParameter("price");
        String status = request.getParameter("status");
        
        switch (typeName) {
            case "updateTicket":
                try {
                    Ticket ticket = new Ticket(Integer.parseInt(ticketID), BigDecimal.valueOf(Double.parseDouble(price)), Integer.parseInt(status));
                    boolean result = tService.updateTicketByAdmin(ticket);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Update Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Update error Ticket already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Update Fail");
                }
                listTicket = tService.getAllTicketActive();
                request.setAttribute("listTicket", listTicket);
                break;
            case "createTicket":
                try {
                    Ticket ticket = new Ticket(Integer.parseInt(villageID), Integer.parseInt(typeID), BigDecimal.valueOf(Double.parseDouble(price)), Integer.parseInt(status));
                    boolean result = tService.createTicketByAdmin(ticket);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Create Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Create Fail: Ticket already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Create Fail");
                }
                listTicket = tService.getAllTicketActive();
                request.setAttribute("listTicket", listTicket);
                break;
            case "deleteTicket":
                try {
                    boolean result = tService.deleteTicketByAdmin(Integer.parseInt(ticketID));
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
                listTicket = tService.getAllTicketActive();
                request.setAttribute("listTicket", listTicket);
                break;
            case "searchTicket":
                try {
                    listTicket = tService.searchTicketByAdmin(Integer.parseInt(status), Integer.parseInt(villageID));
                    request.setAttribute("error", "1");
                    request.setAttribute("message", "Search Success");
                    request.setAttribute("listTicket", listTicket);
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Search Fail");
                }
                break;
            default:
                throw new AssertionError();
        }
        request.getRequestDispatcher("admin-ticket-management.jsp").forward(request, response);
        
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
