package controller.craft;

import DAO.CraftTypeDAO;
import entity.Account.Account;
import entity.CraftVillage.CraftType;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "ManagerCraftType", urlPatterns = {"/managerCraftType"})
public class ManagerCraftType extends HttpServlet {

    private CraftTypeDAO craftTypeDAO;

    @Override
    public void init() throws ServletException {
        craftTypeDAO = new CraftTypeDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        
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
                    addCraftType(request, response);
                    break;
                case "update":
                    updateCraftType(request, response);
                    break;
                case "delete":
                    deleteCraftType(request, response);
                    break;
                default:
                    listCraftTypes(request, response);
                    break;
            }
        } else {
            listCraftTypes(request, response);
        }
    }

    private void listCraftTypes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }

        int recordsPerPage = 10;
        List<CraftType> listCraftTypes = craftTypeDAO.getAllCraftTypes((page - 1) * recordsPerPage, recordsPerPage);
        int totalCraftTypes = craftTypeDAO.getTotalCraftTypes();
        int totalPages = (int) Math.ceil(totalCraftTypes * 1.0 / recordsPerPage);

        request.setAttribute("listCraftTypes", listCraftTypes);
        request.setAttribute("totalCraftTypes", totalCraftTypes);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("ManagerCraftType.jsp").forward(request, response);
    }

    private void addCraftType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String craftTypeName = request.getParameter("craftTypeName");
            String description = request.getParameter("description");
            int status = Integer.parseInt(request.getParameter("status"));

            CraftType craftType = new CraftType();
            craftType.setCraftTypeName(craftTypeName);
            craftType.setDescription(description);
            craftType.setStatus(status);
            craftType.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            boolean success = craftTypeDAO.addCraftType(craftType);
            if (success) {
                request.getSession().setAttribute("successMessage", "Craft type added successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to add craft type");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        response.sendRedirect("managerCraftType");
    }

    private void updateCraftType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int craftTypeID = Integer.parseInt(request.getParameter("craftTypeID"));
            String craftTypeName = request.getParameter("craftTypeName");
            String description = request.getParameter("description");
            int status = Integer.parseInt(request.getParameter("status"));

            CraftType craftType = new CraftType();
            craftType.setCraftTypeID(craftTypeID);
            craftType.setCraftTypeName(craftTypeName);
            craftType.setDescription(description);
            craftType.setStatus(status);
            craftType.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

            boolean success = craftTypeDAO.updateCraftType(craftType);
            if (success) {
                request.getSession().setAttribute("successMessage", "Craft type updated successfully");
            } else {
                request.setAttribute("errorMessage", "Failed to update craft type");
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        response.sendRedirect("managerCraftType");
    }

    private void deleteCraftType(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String craftTypeID = request.getParameter("craftTypeID");
        String bulkDeleteIds = request.getParameter("bulkDeleteIds");
        
        boolean success = false;
        if (bulkDeleteIds != null && !bulkDeleteIds.isEmpty()) {
            String[] ids = bulkDeleteIds.split(",");
            success = true;
            for (String id : ids) {
                if (!craftTypeDAO.deleteCraftType(Integer.parseInt(id))) {
                    success = false;
                    break;
                }
            }
        } else if (craftTypeID != null && !craftTypeID.isEmpty()) {
            success = craftTypeDAO.deleteCraftType(Integer.parseInt(craftTypeID));
        }

        if (success) {
            request.getSession().setAttribute("successMessage", "Craft type(s) deactivated successfully");
        } else {
            request.setAttribute("errorMessage", "Failed to deactivate craft type(s)");
        }
        response.sendRedirect("managerCraftType");
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
        return "Servlet for managing craft types";
    }
} 