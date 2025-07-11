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
 * ChangePasswordServlet - Controller Layer for Password Management
 * Follows MVC pattern: Client → Controller → Service → DAO → Database
 * 
 * Responsibilities:
 * - Handle HTTP requests for password change
 * - Validate input parameters
 * - Call appropriate service methods for password operations
 * - Handle authentication and authorization
 * - Forward to appropriate JSP views
 * - Provide user feedback for success/error cases
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/changePassword"})
public class ChangePasswordServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());
    
    // Dependency injection for Service layer
    private final AccountService accountService;
    
    public ChangePasswordServlet() {
        this.accountService = new AccountService();
    }

    /**
     * Handle GET request - Show change password form
     * Client → Controller → JSP View
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        LOGGER.log(Level.INFO, "Handling change password GET request");
        
        // Check authentication
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            LOGGER.log(Level.WARNING, "Unauthenticated access to change password page");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        LOGGER.log(Level.INFO, "Showing change password form for user: {0}", account.getUserName());
        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }

    /**
     * Handle POST request - Process password change
     * Client → Controller → Service → DAO → Database
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        LOGGER.log(Level.INFO, "Handling change password POST request");
        
        // Check authentication
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("acc");
        
        if (account == null) {
            LOGGER.log(Level.WARNING, "Unauthenticated access to change password action");
            response.sendRedirect("Login.jsp");
            return;
        }
        
        try {
            // Parse and validate request parameters
            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Input validation
            String validationError = validatePasswordInput(currentPassword, newPassword, confirmPassword);
            if (validationError != null) {
                LOGGER.log(Level.WARNING, "Password validation failed for user {0}: {1}", 
                          new Object[]{account.getUserName(), validationError});
                request.setAttribute("message", validationError);
                request.setAttribute("messageType", "error");
                request.getRequestDispatcher("changePassword.jsp").forward(request, response);
                return;
            }
            
            // Controller calls Service layer to change password
            // Service handles business logic and calls DAO for database operations
            boolean isPasswordChanged = accountService.changePassword(
                account.getUserID(), 
                currentPassword, 
                newPassword
            );
            
            if (isPasswordChanged) {
                // Success: refresh account data in session
                Account refreshedAccount = accountService.getAccountById(account.getUserID());
                if (refreshedAccount != null) {
                    session.setAttribute("acc", refreshedAccount);
                }
                
                LOGGER.log(Level.INFO, "Password changed successfully for user: {0}", account.getUserName());
                request.setAttribute("message", "Password changed successfully");
                request.setAttribute("messageType", "success");
                
            } else {
                LOGGER.log(Level.WARNING, "Password change failed for user: {0} - current password incorrect", 
                          account.getUserName());
                request.setAttribute("message", "Current password is incorrect");
                request.setAttribute("messageType", "error");
            }
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during password change for user: " + account.getUserName(), e);
            request.setAttribute("message", "An error occurred while changing password. Please try again.");
            request.setAttribute("messageType", "error");
        }
        
        // Forward back to change password page with result
        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }
    
    /**
     * Validate password input parameters
     * Business validation logic in Controller layer before calling Service
     * 
     * @param currentPassword Current password from form
     * @param newPassword New password from form
     * @param confirmPassword Confirm password from form
     * @return Error message if validation fails, null if valid
     */
    private String validatePasswordInput(String currentPassword, String newPassword, String confirmPassword) {
        
        // Check for null or empty fields
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return "Current password is required";
        }
        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return "New password is required";
        }
        
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Password confirmation is required";
        }
        
        // Check if new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            return "New password and confirmation do not match";
        }
        
        // Password strength validation
        if (newPassword.length() < 6) {
            return "New password must be at least 6 characters long";
        }
        
        // Additional password strength checks can be added here
        if (newPassword.equals(currentPassword)) {
            return "New password must be different from current password";
        }
        
        // All validations passed
        return null;
    }
    
    @Override
    public String getServletInfo() {
        return "ChangePasswordServlet - Handles password change requests following MVC pattern";
    }
} 