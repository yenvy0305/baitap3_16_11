package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;

@WebServlet("/video")
public class VideoListController extends VideoControllerSupport {
    private static final int PAGE_SIZE = 6;
    private final VideoService service = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = requireUser(req, resp);
        if (user == null) return;

        int page = 1;
        try {
            page = Math.max(1, Integer.parseInt(req.getParameter("page")));
        } catch (RuntimeException ignored) {
            // Dùng trang đầu tiên nếu tham số page thiếu hoặc không hợp lệ.
        }

        long totalVideos = service.countByUser(user.getId());
        int totalPages = (int) Math.max(1, (totalVideos + PAGE_SIZE - 1) / PAGE_SIZE);
        page = Math.min(page, totalPages);
        req.setAttribute("videos", service.getByUser(
                user.getId(), (page - 1) * PAGE_SIZE, PAGE_SIZE));
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalVideos", totalVideos);
        req.getRequestDispatcher("/views/video/list.jsp").forward(req, resp);
    }
}
