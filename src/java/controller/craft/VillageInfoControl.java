package controller.craft;

import DAO.VillageDAO;
import entity.CraftVillage.Village;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "VillageInfoControl", urlPatterns = {"/village-info"})
public class VillageInfoControl extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            VillageDAO villageDAO = new VillageDAO();
            String villageIdParam = request.getParameter("id");
            List<Village> villages;
            if (villageIdParam != null && !villageIdParam.isEmpty()) {
                try {
                    int villageId = Integer.parseInt(villageIdParam);
                    Village village = villageDAO.getVillageById(villageId);
                    villages = new java.util.ArrayList<>();
                    if (village != null) {
                        villages.add(village);
                    }
                } catch (NumberFormatException e) {
                    villages = villageDAO.getAllVillages();
                }
            } else {
                villages = villageDAO.getAllVillages();
            }
            // Always set listVillages for the menu
            request.setAttribute("listVillages", villageDAO.getAllVillages());
            request.setAttribute("villages", villages);
            request.getRequestDispatcher("VillageInfo.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect("404Loi.jsp");
        }
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
} 