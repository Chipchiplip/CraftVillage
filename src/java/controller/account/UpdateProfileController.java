package controller.account;

import entity.Account.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AccountService;

@WebServlet(name = "UpdateProfileController", urlPatterns = {"/updateProfile"})
public class UpdateProfileController extends HttpServlet {

    private AccountService accountService = new AccountService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            response.sendRedirect("Login.jsp");
            return;
        }
        
        // Get parameters from form
        int id = account.getUserID(); // Use logged in user's ID
        String username = request.getParameter("username"); // Note: username is readonly, không update
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        
        // Get original account data to compare changes
        Account originalAccount = (Account) session.getAttribute("acc");
        
        // Validate email uniqueness (skip if unchanged)
        if (email != null && !email.isEmpty() && !email.equals(originalAccount.getEmail())) {
            if (accountService.checkEmailExists(email)) {
                request.setAttribute("message", "Email address is already being used by another account.");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
                return;
            }
        }
        // Validate phone number uniqueness (skip if unchanged)
        if (phone != null && !phone.isEmpty() && !phone.equals(originalAccount.getPhoneNumber())) {
            if (accountService.checkPhoneNumberExists(phone)) {
                request.setAttribute("message", "Phone number is already being used by another account.");
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
                return;
            }
        }
        // Handle password change if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!handlePasswordChange(request, response, account, id, currentPassword, newPassword)) {
                return; // Error message already set in handlePasswordChange
            }
        }
        // Update account information (don't update password or username)
        // Username is readonly - không update
        account.setEmail(email);
        account.setFullName(fullName);
        account.setPhoneNumber(phone);
        account.setAddress(address);
        
        boolean success = accountService.updateProfile(account);
        if (success) {
            // Refresh account info in session
            Account updatedAccount = accountService.getAccountById(id);
            session.setAttribute("acc", updatedAccount);
            request.setAttribute("message", "Profile updated successfully");
            request.setAttribute("messageType", "success");
        } else {
            request.setAttribute("message", "Failed to update profile");
            request.setAttribute("messageType", "error");
        }
        request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
    }
    
    private boolean handlePasswordChange(HttpServletRequest request, HttpServletResponse response, 
                                        Account account, int userId,
                                        String currentPassword, String newPassword) 
                                        throws ServletException, IOException {
        // Validate new password
        if (newPassword.length() < 6) {
            request.setAttribute("message", "New password must be at least 6 characters long");
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
            return false;
        }
        // Verify current password and update
        boolean changed = accountService.changePassword(userId, currentPassword, newPassword);
        if (!changed) {
            request.setAttribute("message", "Current password is incorrect");
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
            return false;
        }
        return true;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("userprofile");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
} 