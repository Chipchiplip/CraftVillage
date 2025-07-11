package controller.Authenticate;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet xử lý đăng xuất người dùng
 */
@WebServlet(name = "LogoutControl", urlPatterns = {"/logout"})
public class LogoutControl extends HttpServlet {

    /**
     * Xử lý yêu cầu GET để đăng xuất người dùng
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session hiện tại, không tạo mới nếu chưa có
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Hủy toàn bộ session để đăng xuất người dùng
        }
        response.sendRedirect("login"); // Chuyển hướng về trang đăng nhập
    }

    /**
     * Xử lý yêu cầu POST để đăng xuất người dùng (chuyển hướng sang GET)
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Trả về mô tả ngắn gọn về Servlet này
     * @return Mô tả Servlet
     */
    @Override
    public String getServletInfo() {
        return "Servlet xử lý đăng xuất người dùng";
    }
}
