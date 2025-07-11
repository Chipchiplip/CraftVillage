package controller.account;

import controller.Authenticate.LoginGmail;
import entity.Account.Account;
import entity.Account.GoogleAccount;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.IAccountService;
import service.AccountService;
import service.IWishlistService;
import service.WishlistService;

@WebServlet(name = "LoginControl", urlPatterns = {"/login"})
public class LoginControl extends HttpServlet {

    private IAccountService accountService = new AccountService();
    IWishlistService wService = new WishlistService();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("user");
        String password = request.getParameter("pass");

        // Controller calls Service for business logic
        Account a = accountService.login(username, password);

        if (a == null) {
            request.setAttribute("mess", "Wrong user or pass");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("acc", a);
            session.setAttribute("account", a);  // FOR MENU.JSP COMPATIBILITY
            session.setAttribute("userID", a.getUserID());
            session.setAttribute("userName", a.getUserName());
            session.setAttribute("fullName", a.getFullName()); // ✅ Added fullName
            session.setAttribute("roleID", a.getRoleID());
            session.setMaxInactiveInterval(1800); // 30 minutes

            // Get wishlist count
            try {
                int wishlistCount = wService.getWishListCount(a.getUserID());
                session.setAttribute("wishlistCount", wishlistCount);
            } catch (Exception e) {
                session.setAttribute("wishlistCount", 0);
            }
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code"); // Lấy mã code từ Google
        

        if (code == null || code.isEmpty()) {
            request.getRequestDispatcher("Login.jsp").forward(request, response);
            return;
        }

        try {
            // Bước 1: Lấy access token từ Google
            LoginGmail gg = new LoginGmail();
            String accessToken = gg.getToken(code);


            if (accessToken == null) {
                throw new IOException("Failed to get access token");
            }

            // Bước 2: Lấy thông tin user từ Google
            GoogleAccount acc = gg.getUserInfo(accessToken);


            if (acc == null || acc.getEmail() == null) {
                throw new IOException("Failed to get Google account info");
            }

            // Bước 3: Controller calls Service to check user in database
            Account a = accountService.loginByEmail(acc.getEmail());

            // Kiểm tra nếu không tìm thấy tài khoản trong database
            if (a == null) {

                request.setAttribute("error", "Email not found. Please register or try again.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            } else {
                // Nếu tìm thấy tài khoản, thiết lập session
                HttpSession session = request.getSession();
                session.setAttribute("acc", a);
                session.setAttribute("account", a);  // FOR MENU.JSP COMPATIBILITY
                session.setAttribute("userID", a.getUserID());
                session.setAttribute("userName", a.getUserName());
                session.setAttribute("fullName", a.getFullName()); // ✅ Added fullName
                session.setAttribute("roleID", a.getRoleID());
                session.setMaxInactiveInterval(1800); // 30 minutes

                // Get wishlist count
                try {
                    int wishlistCount = wService.getWishListCount(a.getUserID());
                    session.setAttribute("wishlistCount", wishlistCount);
                } catch (Exception e) {

                    session.setAttribute("wishlistCount", 0);
                }
                response.sendRedirect("home");

            }
        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("error", "Error during Google login: " + e.getMessage());
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String rememberMe = request.getParameter("rememberMe");
        String u = request.getParameter("user");
        String p = request.getParameter("pass");



        // Controller calls Service for business logic
        Account a = accountService.login(u, p);

        if (a == null) {
            request.setAttribute("error", "username or password invalid");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("acc", a);
            session.setAttribute("account", a);  // FOR MENU.JSP COMPATIBILITY
            session.setAttribute("userID", a.getUserID());
            session.setAttribute("userName", a.getUserName());
            session.setAttribute("fullName", a.getFullName()); 
            session.setAttribute("roleID", a.getRoleID());
            session.setMaxInactiveInterval(1800); // 30 minutes

            // Get wishlist count
            try {
                int wishlistCount = wService.getWishListCount(a.getUserID());
                session.setAttribute("wishlistCount", wishlistCount);
            } catch (Exception e) {

                session.setAttribute("wishlistCount", 0);
            }

            if (rememberMe != null && rememberMe.equals("on")) {
                Cookie usernameCookie = new Cookie("COOKIE_USERNAME", u);
                Cookie passwordCookie = new Cookie("COOKIE_PASSWORD", p);
                usernameCookie.setMaxAge(60 * 60 * 24);
                passwordCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);
            }
            response.sendRedirect("home");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
