package controller.craft;

import DAO.VillageTicketDAO;
import DAO.TicketOrderDAO;
import DAO.CraftVillageDAO;
import entity.Ticket.VillageTicket;
import entity.Ticket.TicketType;
import entity.Orders.TicketOrder;
import entity.Orders.TicketCode;
import entity.Account.Account;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for handling village ticket operations
 */
@WebServlet(name = "VillageTicketControl", urlPatterns = {"/villageticket"})
public class VillageTicketControl extends HttpServlet {
    
    private VillageTicketDAO villageTicketDAO = new VillageTicketDAO();
    private TicketOrderDAO ticketOrderDAO = new TicketOrderDAO();
    private CraftVillageDAO villageDAO = new CraftVillageDAO();
    private Gson gson = new Gson();

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
            case "admin":
                showAdminTicketManagement(request, response);
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
            response.sendRedirect("villageticket");
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
                response.sendRedirect("villageticket");
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
            var village = villageDAO.getVillageById(villageId);
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
            var village = villageDAO.getVillageById(villageId);
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
            
            // Build ticket items JSON
            JsonArray ticketItems = new JsonArray();
            List<VillageTicket> availableTickets = villageTicketDAO.getTicketsByVillageID(villageId);
            
            BigDecimal totalAmount = BigDecimal.ZERO;
            int totalQuantity = 0;
            
            for (VillageTicket ticket : availableTickets) {
                String quantityParam = request.getParameter("quantity_" + ticket.getTicketID());
                if (quantityParam != null && !quantityParam.isEmpty()) {
                    int quantity = Integer.parseInt(quantityParam);
                    if (quantity > 0) {
                        JsonObject item = new JsonObject();
                        item.addProperty("ticketID", ticket.getTicketID());
                        item.addProperty("quantity", quantity);
                        ticketItems.add(item);
                        
                        totalAmount = totalAmount.add(ticket.getPrice().multiply(BigDecimal.valueOf(quantity)));
                        totalQuantity += quantity;
                    }
                }
            }

            if (ticketItems.size() == 0) {
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
                gson.toJson(ticketItems)
            );

            if (orderID > 0) {
                // Redirect to payment or success page
                // VNPay payment integration removed
                if ("vnpay".equals(paymentMethod)) {
                    // Redirect to order success instead of payment gateway
                    response.sendRedirect("order-success?orderId=" + orderID);
                } else {
                    response.sendRedirect("villageticket?action=mytickets&success=true&orderID=" + orderID);
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
        String statusFilter = request.getParameter("status");
        List<TicketCode> tickets = ticketOrderDAO.getUserTickets(account.getUserID(), statusFilter);
        
        request.setAttribute("orders", orders);
        request.setAttribute("tickets", tickets);
        request.setAttribute("statusFilter", statusFilter);
        
        // Check for success message
        String success = request.getParameter("success");
        String orderID = request.getParameter("orderID");
        if ("true".equals(success) && orderID != null) {
            request.setAttribute("successMessage", "Đặt vé thành công! Mã đơn hàng: " + orderID);
        }
        
        request.getRequestDispatcher("my-tickets.jsp").forward(request, response);
    }

    /**
     * Show admin ticket management
     */
    private void showAdminTicketManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null || account.getRoleID() != 1) {
            response.sendRedirect("home");
            return;
        }

        // Get all orders
        List<TicketOrder> orders = ticketOrderDAO.getAllTicketOrders();
        
        // Get ticket pricing summary
        List<VillageTicket> pricingSummary = villageTicketDAO.getTicketPricingSummary();
        
        request.setAttribute("orders", orders);
        request.setAttribute("pricingSummary", pricingSummary);
        
        request.getRequestDispatcher("admin-tickets.jsp").forward(request, response);
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
                var village = villageDAO.getVillageById(villageId);
                if (village == null || village.getSellerId() != account.getUserID()) {
                    response.sendRedirect("manager");
                    return;
                }
            }

            // Get village info
            var village = villageDAO.getVillageById(villageId);
            
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
     * Update ticket pricing
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
                var village = villageDAO.getVillageById(villageId);
                if (village == null || village.getSellerId() != account.getUserID()) {
                    response.sendRedirect("manager");
                    return;
                }
            }

            BigDecimal adultPrice = new BigDecimal(request.getParameter("adultPrice"));
            BigDecimal childPrice = new BigDecimal(request.getParameter("childPrice"));
            BigDecimal studentPrice = new BigDecimal(request.getParameter("studentPrice"));

            boolean success = villageTicketDAO.updateVillageTicketPrices(villageId, adultPrice, childPrice, studentPrice);

            if (success) {
                response.sendRedirect("villageticket?action=pricing&villageId=" + villageId + "&success=true");
            } else {
                response.sendRedirect("villageticket?action=pricing&villageId=" + villageId + "&error=true");
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
     * Validate ticket
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
        
        JsonObject result = new JsonObject();
        result.addProperty("success", success);
        if (success) {
            result.addProperty("message", "Vé hợp lệ và đã được sử dụng");
        } else {
            result.addProperty("message", "Vé không hợp lệ hoặc đã được sử dụng");
        }
        
        response.getWriter().write(gson.toJson(result));
    }
} 