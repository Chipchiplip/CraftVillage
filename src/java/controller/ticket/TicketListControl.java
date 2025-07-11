/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ticket;

import DAO.CraftVillageDAO;
import DAO.TicketAvailabilityDAO;
import DAO.VillageTicketDAO;
import entity.CraftVillage.CraftVillage;
import entity.Ticket.VillageTicket;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import service.ITicketAvailabilityService;
import service.TicketAvailabilityService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "TicketListControl", urlPatterns = {"/ticket-list"})
public class TicketListControl extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private VillageTicketDAO villageTicketDAO = new VillageTicketDAO();
    private CraftVillageDAO villageDAO = new CraftVillageDAO();
    private TicketAvailabilityDAO availabilityDAO = new TicketAvailabilityDAO();
    private ITicketAvailabilityService availabilityService = new TicketAvailabilityService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                showTicketList(request, response);
                break;
            case "village":
                showVillageTickets(request, response);
                break;
            default:
                showTicketList(request, response);
                break;
        }
    }

    /**
     * Show all available tickets grouped by village
     */
    private void showTicketList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get all active tickets
        List<VillageTicket> allTickets = villageTicketDAO.getAllActiveTickets();
        
        // Get unique village IDs from tickets
        List<Integer> villageIds = allTickets.stream()
                .map(VillageTicket::getVillageID)
                .distinct()
                .collect(Collectors.toList());
        
        // Get village information for those villages
        List<CraftVillage> villages = villageIds.stream()
                .map(villageId -> villageDAO.getVillageById(villageId))
                .filter(village -> village != null && village.getStatus() == 1) // Only active villages
                .collect(Collectors.toList());
        
        // Get ticket summary for each village
        Map<Integer, List<VillageTicket>> villageTicketsMap = new HashMap<>();
        Map<Integer, Boolean> villageAvailabilityMap = new HashMap<>();
        
        for (CraftVillage village : villages) {
            List<VillageTicket> tickets = villageTicketDAO.getTicketsByVillageId(village.getVillageID());
            villageTicketsMap.put(village.getVillageID(), tickets);
            
            // Check if any tickets have availability in the next 30 days using service
            boolean hasAvailability = tickets.stream()
                    .anyMatch(ticket -> !availabilityService.getAvailableDatesForTicket(ticket.getTicketID()).isEmpty());
            villageAvailabilityMap.put(village.getVillageID(), hasAvailability);
        }
        
        // Set attributes for JSP
        request.setAttribute("villages", villages);
        request.setAttribute("villageTicketsMap", villageTicketsMap);
        request.setAttribute("villageAvailabilityMap", villageAvailabilityMap);
        request.setAttribute("hasVillages", !villages.isEmpty());
        
        request.getRequestDispatcher("TicketList.jsp").forward(request, response);
    }

    /**
     * Show tickets for a specific village
     */
    private void showVillageTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String villageIdStr = request.getParameter("villageId");
        if (villageIdStr == null || villageIdStr.isEmpty()) {
            response.sendRedirect("ticket-list");
            return;
        }

        try {
            int villageId = Integer.parseInt(villageIdStr);
            
            // Get village information
            CraftVillage village = villageDAO.getVillageById(villageId);
            if (village == null || village.getStatus() != 1) {
                response.sendRedirect("ticket-list");
                return;
            }

            // Get tickets for this village
            List<VillageTicket> tickets = villageTicketDAO.getTicketsByVillageId(villageId);
            
            // Get availability information for each ticket
            Map<Integer, List<entity.Ticket.TicketAvailability>> ticketAvailabilityMap = new HashMap<>();
            
            for (VillageTicket ticket : tickets) {
                List<entity.Ticket.TicketAvailability> availableDates = availabilityService.getAvailableDatesForTicket(ticket.getTicketID());
                ticketAvailabilityMap.put(ticket.getTicketID(), availableDates);
            }
            
            // Set attributes for JSP
            request.setAttribute("village", village);
            request.setAttribute("tickets", tickets);
            request.setAttribute("ticketAvailabilityMap", ticketAvailabilityMap);
            request.setAttribute("hasTickets", !tickets.isEmpty());
            
            request.getRequestDispatcher("VillageTickets.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("ticket-list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
