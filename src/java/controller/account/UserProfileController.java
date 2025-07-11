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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserProfileController - Controller Layer for User Profile Management
 * Follows MVC pattern: Client → Controller → Service → DAO → Database
 */
@WebServlet(name = "UserProfileController", urlPatterns = {"/userprofile"})
public class UserProfileController extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(UserProfileController.class.getName());
    private final AccountService accountService;
    
    public UserProfileController() {
        this.accountService = new AccountService();
    }

    /**
     * Handles both GET and POST requests for user profile
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String action = request.getParameter("action");
        
        if (action == null) {
            // Default action: show user profile
            handleShowProfile(request, response);
        } else {
            switch (action) {
                case "update":
                    handleUpdateProfile(request, response);
                    break;
                default:
                    handleShowProfile(request, response);
                    break;
            }
        }
    }

    /**
     * Handle showing user profile
     * Client → Controller → Service → DAO → Database
     */
    private void handleShowProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.log(Level.INFO, "Handling show profile request");
        
        // Get current session and check authentication
        HttpSession session = request.getSession();
        Account sessionAccount = (Account) session.getAttribute("acc");
        
        if (sessionAccount == null) {
            LOGGER.log(Level.WARNING, "No user in session, redirecting to login");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        try {
            // Controller calls Service layer to get fresh account data
            // Note: getAccountById not implemented, using session account
            Account refreshedAccount = sessionAccount;
            
            if (refreshedAccount != null) {
                // Update session with fresh data
                session.setAttribute("acc", refreshedAccount);
                LOGGER.log(Level.INFO, "Profile loaded successfully for user: {0}", refreshedAccount.getUserName());
            } else {
                LOGGER.log(Level.WARNING, "Account not found in database for ID: {0}", sessionAccount.getUserID());
                request.setAttribute("error", "Account not found");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading user profile", e);
            request.setAttribute("error", "Failed to load profile");
        }
        
        // Forward to JSP view
        request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
    }

    /**
     * Handle updating user profile  
     * Client → Controller → Service → DAO → Database
     */
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.log(Level.INFO, "Handling update profile request");
        
        // Get current session and check authentication
        HttpSession session = request.getSession();
        Account sessionAccount = (Account) session.getAttribute("acc");
        
        if (sessionAccount == null) {
            LOGGER.log(Level.WARNING, "No user in session, redirecting to login");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        try {
            // Parse request parameters
            String userName = request.getParameter("userName");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            
            // Validate input
            if (userName == null || userName.trim().isEmpty()) {
                request.setAttribute("error", "Username is required");
                request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
                return;
            }
            
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email is required");
                request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
                return;
            }
            
            // Create updated account object
            Account updatedAccount = new Account();
            updatedAccount.setUserID(sessionAccount.getUserID());
            updatedAccount.setUserName(userName.trim());
            updatedAccount.setEmail(email.trim());
            updatedAccount.setFullName(fullName != null ? fullName.trim() : "");
            updatedAccount.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : "");
            updatedAccount.setAddress(address != null ? address.trim() : "");
            updatedAccount.setPassword(sessionAccount.getPassword()); // Keep existing password
            updatedAccount.setRoleID(sessionAccount.getRoleID());
            updatedAccount.setStatus(sessionAccount.getStatus());
            
            // Controller calls Service layer to update account
            boolean isUpdated = accountService.updateProfile(updatedAccount);
            
            if (isUpdated) {
                // Update session with updated account
                session.setAttribute("acc", updatedAccount);
                
                LOGGER.log(Level.INFO, "Profile updated successfully for user: {0}", userName);
                request.setAttribute("success", "Profile updated successfully");
            } else {
                LOGGER.log(Level.WARNING, "Failed to update profile for user: {0}", userName);
                request.setAttribute("error", "Failed to update profile");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating user profile", e);
            request.setAttribute("error", "An error occurred while updating profile");
        }
        
        // Forward back to profile page
        request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
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
        return "UserProfileController - Handles user profile management following MVC pattern";
    }
} 