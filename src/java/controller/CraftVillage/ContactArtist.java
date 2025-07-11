/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.CraftVillage;

import entity.MessageNotification.Message;
import entity.MessageNotification.MessageThread;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import service.AccountService;
import service.MessageService;
import service.VillageService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "ContactArtist", urlPatterns = {"/contact-artist"})
public class ContactArtist extends HttpServlet {

    MessageService mService = new MessageService();
    VillageService vService = new VillageService();
    AccountService aService = new AccountService();
    List<Message> listMessage;
    MessageThread messageThread;

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

        String villageID = request.getParameter("villageID");
        String userID = request.getParameter("userID");
        String sellerID = request.getParameter("sellerID");
        String threadIDStr = request.getParameter("threadID");
        String fullName = aService.getAccountById(Integer.parseInt(sellerID)).getFullName();
        int threadID;
        try {
            if (!mService.checkMessageThreadExist(Integer.parseInt(userID), Integer.parseInt(sellerID))) {
                String villageName = vService.getVillageNameByID(Integer.parseInt(villageID));
                villageName = villageName + " contact to " + fullName;
                mService.addNewMessageThread(new MessageThread(Integer.parseInt(userID), Integer.parseInt(sellerID), villageName));
            }
            try {
                threadID = Integer.parseInt(threadIDStr);
            } catch (Exception e) {
                threadID = mService.getThreadID(Integer.parseInt(userID), Integer.parseInt(sellerID));
            }

            listMessage = mService.getMessageByThreadID(threadID);
            request.setAttribute("listMessage", listMessage);

            messageThread = mService.getMessageThread(Integer.parseInt(userID), Integer.parseInt(sellerID));
            request.setAttribute("messageThread", messageThread);
request.getRequestDispatcher("contact-artist.jsp").forward(request, response);
        } catch (Exception e) {
           
        }
         
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

        try {
            // Lấy parameters
            int threadID = Integer.parseInt(request.getParameter("threadID"));
            int senderID = Integer.parseInt(request.getParameter("senderID"));
            String messageContent = request.getParameter("messageContent");
            String attachmentUrl = request.getParameter("attachmentUrl");

            // Tạo Message
            Message message = new Message(threadID, senderID, messageContent, attachmentUrl);
            boolean success = mService.sendMessage(message);

            request.setAttribute("error", success ? "1" : "0");
            request.setAttribute("message", success ? "Send Success" : "Send Fail");

            // Lấy dữ liệu để hiển thị lại
            listMessage = mService.getMessageByThreadID(threadID);
            request.setAttribute("listMessage", listMessage);

            request.setAttribute("messageThread", messageThread);

            request.getRequestDispatcher("contact-artist.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "0");
            request.setAttribute("message", "Send Fail");
            request.getRequestDispatcher("contact-artist.jsp").forward(request, response);
        }
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
