package controller.craft;

import DAO.CraftVillageDAO;
import entity.Account.Account;
import entity.CraftVillage.CraftVillage;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ManagerCraftVillage", urlPatterns = {"/managerCraftVillage"})
public class ManagerCraftVillage extends HttpServlet {

    private CraftVillageDAO craftVillageDAO;

    @Override
    public void init() throws ServletException {
        craftVillageDAO = new CraftVillageDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        // Kiểm tra đăng nhập và quyền truy cập
        if (account == null) {
            response.sendRedirect("login");
            return;
        }
        
        // Kiểm tra quyền truy cập (roleID = 2 hoặc 3)
        if (account.getRoleID() != 2 && account.getRoleID() != 3) {
            response.sendRedirect("home");
            return;
        }

        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "add":
                    addCraftVillage(request, response);
                    break;
                case "update":
                    updateCraftVillage(request, response);
                    break;
                case "delete":
                    deleteCraftVillage(request, response);
                    break;
                default:
                    listCraftVillages(request, response);
                    break;
            }
        } else {
            listCraftVillages(request, response);
        }
    }

    private void listCraftVillages(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        int recordsPerPage = 10;
        List<CraftVillage> listVillages = craftVillageDAO.getAllCraftVillages((page - 1) * recordsPerPage, recordsPerPage);
        int totalVillages = craftVillageDAO.getTotalCraftVillages();
        int totalPages = (int) Math.ceil(totalVillages * 1.0 / recordsPerPage);

        request.setAttribute("listVillages", listVillages);
        request.setAttribute("totalVillages", totalVillages);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("ManagerCraftVillage.jsp").forward(request, response);
    }

    private void addCraftVillage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String villageName = request.getParameter("villageName");
            String description = request.getParameter("description");
            String address = request.getParameter("address");
            double latitude = Double.parseDouble(request.getParameter("latitude"));
            double longitude = Double.parseDouble(request.getParameter("longitude"));
            String contactPhone = request.getParameter("contactPhone");
            String contactEmail = request.getParameter("contactEmail");
            String mainImageUrl = request.getParameter("mainImageUrl");
            int status = Integer.parseInt(request.getParameter("status"));

            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("acc");
            int sellerId = account.getUserID();

            CraftVillage village = new CraftVillage();
            village.setVillageName(villageName);
            village.setDescription(description);
            village.setAddress(address);
            village.setLatitude(latitude);
            village.setLongitude(longitude);
            village.setContactPhone(contactPhone);
            village.setContactEmail(contactEmail);
            village.setMainImageUrl(mainImageUrl);
            village.setStatus(status);
            village.setSellerId(sellerId);
            village.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            boolean success = craftVillageDAO.addVillage(village);
            if (success) {
                request.getSession().setAttribute("successMessage", "Craft village added successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to add craft village");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        response.sendRedirect("managerCraftVillage");
    }

    private void updateCraftVillage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int villageID = Integer.parseInt(request.getParameter("villageID"));
            String villageName = request.getParameter("villageName");
            String description = request.getParameter("description");
            String address = request.getParameter("address");
            String contactPhone = request.getParameter("contactPhone");
            String contactEmail = request.getParameter("contactEmail");
            int status = Integer.parseInt(request.getParameter("status"));

            CraftVillage village = new CraftVillage();
            village.setVillageID(villageID);
            village.setVillageName(villageName);
            village.setDescription(description);
            village.setAddress(address);
            village.setContactPhone(contactPhone);
            village.setContactEmail(contactEmail);
            village.setStatus(status);
            village.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

            boolean success = craftVillageDAO.updateVillage(village);
            if (success) {
                request.getSession().setAttribute("successMessage", "Craft village updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update craft village");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        response.sendRedirect("managerCraftVillage");
    }

    private void deleteCraftVillage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String villageID = request.getParameter("villageID");
        String bulkDeleteIds = request.getParameter("bulkDeleteIds");
        
        boolean success = false;
        if (bulkDeleteIds != null && !bulkDeleteIds.isEmpty()) {
            String[] ids = bulkDeleteIds.split(",");
            success = true;
            for (String id : ids) {
                if (!craftVillageDAO.updateVillageStatus(Integer.parseInt(id), 0)) {
                    success = false;
                    break;
                }
            }
        } else if (villageID != null && !villageID.isEmpty()) {
            success = craftVillageDAO.updateVillageStatus(Integer.parseInt(villageID), 0);
        }

        if (success) {
            request.getSession().setAttribute("successMessage", "Craft village(s) deactivated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to deactivate craft village(s)");
        }
        response.sendRedirect("managerCraftVillage");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing craft villages";
    }
} 