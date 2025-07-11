/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Admin;

import entity.CraftVillage.CraftVillage;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.VillageService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "AdminVillageManagement", urlPatterns = {"/admin-village-management"})
public class AdminVillageManagement extends HttpServlet {

    private List<CraftVillage> listAllVillage;
    private VillageService vService = new VillageService();
    CraftVillage village = new CraftVillage();

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
        request.getRequestDispatcher("admin-village-management.jsp").forward(request, response);
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

        String typeName = request.getParameter("typeName");
        String villageID = request.getParameter("villageID");

        String villageName = request.getParameter("villageName");
        String typeID = request.getParameter("typeID");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String contactPhone = request.getParameter("contactPhone");
        String contactEmail = request.getParameter("contactEmail");
        String status = request.getParameter("status");
        String sellerId = request.getParameter("sellerId");
        String openingHours = request.getParameter("openingHours");
        String closingDays = request.getParameter("closingDays");
        String mapEmbedUrl = request.getParameter("mapEmbedUrl");
        String virtualTourUrl = request.getParameter("virtualTourUrl");
        String history = request.getParameter("history");
        String specialFeatures = request.getParameter("specialFeatures");
        String famousProducts = request.getParameter("famousProducts");
        String culturalEvents = request.getParameter("culturalEvents");
        String craftProcess = request.getParameter("craftProcess");
        String videoDescriptionUrl = request.getParameter("videoDescriptionUrl");
        String travelTips = request.getParameter("travelTips");
        String mainImageUrl = request.getParameter("mainImageUrl");
        String searchID = request.getParameter("searchID");
        String contentSearch = request.getParameter("contentSearch");

        switch (typeName) {
            case "updateVillage":
                try {
                    village = new CraftVillage(Integer.parseInt(villageID), villageName, Integer.parseInt(typeID), description, address, Double.parseDouble(latitude.equals("") ? "0.0" : latitude), Double.parseDouble(longitude.equals("") ? "0.0" : longitude), contactPhone, contactEmail, Integer.parseInt(status), Integer.parseInt(sellerId), openingHours, closingDays, mapEmbedUrl, virtualTourUrl, history, specialFeatures, famousProducts, culturalEvents, craftProcess, videoDescriptionUrl, travelTips, mainImageUrl);
                    boolean result = vService.updateCraftVillageByAdmin(village);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Update Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Update error Name Village already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Update Fail");
                }
                listAllVillage = vService.getAllCraftVillageActive();
                request.setAttribute("listAllVillage", listAllVillage);
                break;
            case "deleteVillage":
                try {
                    boolean result = vService.deleteVillageByAdmin(Integer.parseInt(villageID));
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
                listAllVillage = vService.getAllCraftVillageActive();
                request.setAttribute("listAllVillage", listAllVillage);
                break;
            case "addVillage":
                try {
                    village = new CraftVillage(villageName, Integer.parseInt(typeID), description, address, Double.parseDouble(latitude.equals("") ? "0.0" : latitude), Double.parseDouble(longitude.equals("") ? "0.0" : longitude), contactPhone, contactEmail, Integer.parseInt(status), Integer.parseInt(sellerId), openingHours, closingDays, mapEmbedUrl, virtualTourUrl, history, specialFeatures, famousProducts, culturalEvents, craftProcess, videoDescriptionUrl, travelTips, mainImageUrl);
                    boolean result = vService.addNewVillageByAdmin(village);
                    if (result) {
                        request.setAttribute("error", "1");
                        request.setAttribute("message", "Create Success");
                    } else {
                        request.setAttribute("error", "0");
                        request.setAttribute("message", "Create Fail: Name Village already exists");
                    }
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Create Fail");
                }
                listAllVillage = vService.getAllCraftVillageActive();
                request.setAttribute("listAllVillage", listAllVillage);
                break;
            case "searchVillage":
                try {
                    listAllVillage = vService.getSearchVillageByAdmin(Integer.parseInt(status), Integer.parseInt(searchID), contentSearch);
                    request.setAttribute("error", "1");
                    request.setAttribute("message", "Search Success");
                    request.setAttribute("listAllVillage", listAllVillage);
                } catch (Exception e) {
                    request.setAttribute("error", "0");
                    request.setAttribute("message", "Search Fail");
                }
                break;
            default:
                throw new AssertionError();
        }
        request.getRequestDispatcher("admin-village-management.jsp").forward(request, response);
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
