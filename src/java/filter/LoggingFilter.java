package filter;

import entity.Account.Account;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = {"/manager", "/managerAccount", "/add-product"}) // Add all protected URLs here
public class LoggingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // false = do not create new session

        String path = req.getServletPath();

        // Check if user is logged in
        if (session == null || session.getAttribute("acc") == null) {
            LOGGER.info("Unauthorized access attempt to " + path + ". Redirecting to login.");
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // User is logged in, check roles
        Account acc = (Account) session.getAttribute("acc");
        boolean isAdmin = acc.getRoleID() == 3;
        boolean isSeller = acc.getRoleID() == 2;

        // Authorization logic
        if (path.startsWith("/managerAccount")) {
            if (!isAdmin) {
                LOGGER.warning("Access denied for user " + acc.getUserName()+ " to " + path);
                res.sendRedirect(req.getContextPath() + "/login"); // Or a 403 forbidden page
                return;
            }
        }

        if (path.startsWith("/manager")) { // General manager pages
            if (!isAdmin && !isSeller) {
                LOGGER.warning("Access denied for user " + acc.getUserName() + " to " + path);
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }
        
        // If all checks pass, continue to the requested resource
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if any
    }

    @Override
    public void destroy() {
        // Cleanup code, if any
    }
}