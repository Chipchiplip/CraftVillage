package controller.Authenticate;

import entity.Account.Account;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.AccountService;

@WebServlet(name = "RegisterControl", urlPatterns = {"/register"})
public class RegisterControl extends HttpServlet {

    AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("Register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get parameters from form
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            String rePassword = request.getParameter("rePassword");
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phoneNumber");

            // Validate form data
            if (userName == null || userName.trim().isEmpty()
                    || password == null || password.trim().isEmpty()
                    || email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Please fill all required fields");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Trim whitespace from inputs
            userName = userName.trim();
            password = password.trim();
            email = email.trim();

            // Check if passwords match
            if (!password.equals(rePassword)) {
                request.setAttribute("error", "Passwords do not match");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check for duplicate username
            if (accountService.checkUsernameExists(userName)) {
                request.setAttribute("error", "Username '" + userName + "' is already taken. Please choose a different username.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check for duplicate email
            if (accountService.checkEmailExists(email)) {
                request.setAttribute("error", "Email '" + email + "' is already registered. Please use a different email or try logging in.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
                return;
            }

            // Check for duplicate phone number (if provided)
            if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
                phoneNumber = phoneNumber.trim();
                if (accountService.checkPhoneNumberExists(phoneNumber)) {
                    request.setAttribute("error", "Phone number '" + phoneNumber + "' is already registered. Please use a different phone number.");
                    request.getRequestDispatcher("Register.jsp").forward(request, response);
                    return;
                }
            }
            // Use Service to register account
            Account newAccount = new Account();
            newAccount.setUserName(userName);
            newAccount.setPassword(password);
            newAccount.setEmail(email);
            newAccount.setFullName(fullName != null ? fullName.trim() : "");
            newAccount.setAddress(address != null ? address.trim() : "");
            newAccount.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : "");

            boolean success = accountService.register(newAccount);

            if (success) {
                // Registration successful, redirect to login page with success message
                HttpSession session = request.getSession();
                session.setAttribute("registerSuccess", "Registration successful! Please login with your new account.");
                session.setAttribute("registeredEmail", email); // Pre-fill email on login page
                response.sendRedirect("login");
            } else {
                request.setAttribute("error", "Registration failed. Please try again or contact support if the problem persists.");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during registration: " + e.getMessage());
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Register Control Servlet";
    }
}
