package listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Khởi tạo trạng thái nhạc khi session được tạo
        se.getSession().setAttribute("musicPlaying", true);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Dọn dẹp
    }
}