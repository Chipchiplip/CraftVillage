package controller.craft;

import DAO.VillageTicketDAO;
import DAO.TicketOrderDAO;
import DAO.CraftVillageDAO;
import entity.Ticket.VillageTicket;
import entity.Ticket.TicketType;
import entity.Orders.TicketOrder;
import entity.Orders.TicketCode;
import entity.Account.Account;
import entity.CraftVillage.CraftVillage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple controller for handling village ticket operations
 */
@WebServlet(name = "SimpleVillageTicketControl", urlPatterns = {"/ticket"})
public class SimpleVillageTicketControl extends HttpServlet {
    
    private VillageTicketDAO villageTicketDAO = new VillageTicketDAO();
    private TicketOrderDAO ticketOrderDAO = new TicketOrderDAO();
    private CraftVillageDAO villageDAO = new CraftVillageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                showVillageTickets(request, response);
                break;
            case "buy":
                showBuyTicketPage(request, response);
                break;
            case "mytickets":
                showMyTickets(request, response);
                break;
            case "pricing":
                showTicketPricing(request, response);
                break;
            case "validate":
                showTicketValidation(request, response);
                break;
            default:
                showVillageTickets(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("ticket");
            return;
        }

        switch (action) {
            case "purchase":
                purchaseTickets(request, response);
                break;
            case "updatepricing":
                updateTicketPricing(request, response);
                break;
            case "validateticket":
                validateTicket(request, response);
                break;
            default:
                response.sendRedirect("ticket");
                break;
        }
    }

    /**
     * Show village tickets for a specific village
     */
    private void showVillageTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String villageIdStr = request.getParameter("villageId");
        if (villageIdStr == null || villageIdStr.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        try {
            int villageId = Integer.parseInt(villageIdStr);
            
            // Get village info
            CraftVillage village = villageDAO.getVillageById(villageId);
            if (village == null) {
                response.sendRedirect("home");
                return;
            }

            // Get tickets for this village
            List<VillageTicket> tickets = villageTicketDAO.getTicketsByVillageID(villageId);
            
            request.setAttribute("village", village);
            request.setAttribute("tickets", tickets);
            request.setAttribute("hasTickets", !tickets.isEmpty());
            
            request.getRequestDispatcher("village-tickets.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    /**
     * Show buy ticket page
     */
    private void showBuyTicketPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String villageIdStr = request.getParameter("villageId");
        if (villageIdStr == null || villageIdStr.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        try {
            int villageId = Integer.parseInt(villageIdStr);
            
            // Get village info
            CraftVillage village = villageDAO.getVillageById(villageId);
            if (village == null) {
                response.sendRedirect("home");
                return;
            }

            // Get tickets for this village
            List<VillageTicket> tickets = villageTicketDAO.getTicketsByVillageID(villageId);
            if (tickets.isEmpty()) {
                request.setAttribute("error", "Làng này chưa có vé tham quan");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("village", village);
            request.setAttribute("tickets", tickets);
            
            request.getRequestDispatcher("buy-tickets.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    /**
     * Process ticket purchase
     */
    private void purchaseTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            int villageId = Integer.parseInt(request.getParameter("villageId"));
            String customerName = request.getParameter("customerName");
            String customerPhone = request.getParameter("customerPhone");
            String customerEmail = request.getParameter("customerEmail");
            String paymentMethod = request.getParameter("paymentMethod");
            String note = request.getParameter("note");
            
            // Build ticket quantities map
            Map<Integer, Integer> ticketQuantities = new HashMap<>();
            List<VillageTicket> availableTickets = villageTicketDAO.getTicketsByVillageID(villageId);
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            int totalQuantity = 0;
            
            for (VillageTicket ticket : availableTickets) {
                String quantityParam = request.getParameter("quantity_" + ticket.getTicketID());
                if (quantityParam != null && !quantityParam.isEmpty()) {
                    int quantity = Integer.parseInt(quantityParam);
                    if (quantity > 0) {
                        ticketQuantities.put(ticket.getTicketID(), quantity);
                        totalAmount = totalAmount.add(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
                        totalQuantity += quantity;
                    }
                }
            }

            if (ticketQuantities.isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn ít nhất một loại vé");
                showBuyTicketPage(request, response);
                return;
            }

            // Create order
            int orderID = ticketOrderDAO.createTicketOrder(
                account.getUserID(),
                villageId,
                customerName,
                customerPhone,
                customerEmail,
                paymentMethod,
                note,
                ticketQuantities
            );

            if (orderID > 0) {
                // Redirect to payment or success page
                // VNPay payment integration removed  
                if ("vnpay".equals(paymentMethod)) {
                    // Redirect to order success instead of payment gateway
                    response.sendRedirect("order-success?orderId=" + orderID);
                } else {
                    response.sendRedirect("ticket?action=mytickets&success=true&orderID=" + orderID);
                }
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng");
                showBuyTicketPage(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            showBuyTicketPage(request, response);
        }
    }

    /**
     * Show user's tickets
     */
    private void showMyTickets(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            response.sendRedirect("login");
            return;
        }

        // Get user's ticket orders
        List<TicketOrder> orders = ticketOrderDAO.getTicketOrdersByUserID(account.getUserID());
        
        // Get user's individual tickets
        List<TicketCode> tickets = ticketOrderDAO.getUserTickets(account.getUserID());
        
        request.setAttribute("orders", orders);
        request.setAttribute("tickets", tickets);
        
        // Check for success message
        String success = request.getParameter("success");
        String orderID = request.getParameter("orderID");
        if ("true".equals(success) && orderID != null) {
            request.setAttribute("successMessage", "Đặt vé thành công! Mã đơn hàng: " + orderID);
        }
        
        request.getRequestDispatcher("my-tickets.jsp").forward(request, response);
    }

    /**
     * Show ticket pricing management
     */
    private void showTicketPricing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null || (account.getRoleID() != 1 && account.getRoleID() != 2)) {
            response.sendRedirect("home");
            return;
        }

        String villageIdStr = request.getParameter("villageId");
        if (villageIdStr == null || villageIdStr.isEmpty()) {
            response.sendRedirect("manager");
            return;
        }

        try {
            int villageId = Integer.parseInt(villageIdStr);
            
            // Check if user is seller and owns this village
            if (account.getRoleID() == 2) {
                CraftVillage village = villageDAO.getVillageById(villageId);
                if (village == null || village.getSellerId() != account.getUserID()) {
                    response.sendRedirect("manager");
                    return;
                }
            }

            // Get village info
            CraftVillage village = villageDAO.getVillageById(villageId);
            
            // Get current ticket pricing
            List<VillageTicket> tickets = villageTicketDAO.getTicketsByVillageID(villageId);
            
            // Get all ticket types
            List<TicketType> ticketTypes = villageTicketDAO.getAllTicketTypes();
            
            request.setAttribute("village", village);
            request.setAttribute("tickets", tickets);
            request.setAttribute("ticketTypes", ticketTypes);
            
            request.getRequestDispatcher("ticket-pricing.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("manager");
        }
    }

    /**
     * Update ticket pricing using stored procedure
     */
    private void updateTicketPricing(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null || (account.getRoleID() != 1 && account.getRoleID() != 2)) {
            response.sendRedirect("home");
            return;
        }

        try {
            int villageId = Integer.parseInt(request.getParameter("villageId"));
            
            // Check if user is seller and owns this village
            if (account.getRoleID() == 2) {
                CraftVillage village = villageDAO.getVillageById(villageId);
                if (village == null || village.getSellerId() != account.getUserID()) {
                    response.sendRedirect("manager");
                    return;
                }
            }

            BigDecimal adultPrice = new BigDecimal(request.getParameter("adultPrice"));
            BigDecimal childPrice = new BigDecimal(request.getParameter("childPrice"));
            BigDecimal studentPrice = new BigDecimal(request.getParameter("studentPrice"));

            // Use simple stored procedure
            String sql = "{CALL UpdateTicketPrices(?, ?, ?, ?)}";
            
            try (java.sql.CallableStatement cs = villageTicketDAO.getConnection().prepareCall(sql)) {
                cs.setInt(1, villageId);
                cs.setBigDecimal(2, adultPrice);
                cs.setBigDecimal(3, childPrice);
                cs.setBigDecimal(4, studentPrice);
                
                boolean success = cs.executeUpdate() > 0;

                if (success) {
                    response.sendRedirect("ticket?action=pricing&villageId=" + villageId + "&success=true");
                } else {
                    response.sendRedirect("ticket?action=pricing&villageId=" + villageId + "&error=true");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manager");
        }
    }

    /**
     * Show ticket validation page
     */
    private void showTicketValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null || (account.getRoleID() != 1 && account.getRoleID() != 2)) {
            response.sendRedirect("home");
            return;
        }
        
        request.getRequestDispatcher("ticket-validation.jsp").forward(request, response);
    }

    /**
     * Validate ticket using simple method
     */
    private void validateTicket(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null || (account.getRoleID() != 1 && account.getRoleID() != 2)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String ticketCode = request.getParameter("ticketCode");
        String usedBy = account.getUserName();

        boolean success = ticketOrderDAO.validateAndUseTicket(ticketCode, usedBy);

        // Return JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String result = success ? 
            "{\"success\":true,\"message\":\"Vé hợp lệ và đã được sử dụng\"}" :
            "{\"success\":false,\"message\":\"Vé không hợp lệ hoặc đã được sử dụng\"}";
        
        response.getWriter().write(result);
    }
} 