package vn.iotstar.controller;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.model.User;
import vn.iotstar.model.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.UploadUtil;

@WebServlet("/video/add")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 200L * 1024 * 1024,
        maxRequestSize = 210L * 1024 * 1024)
public class VideoAddController extends VideoControllerSupport {
    private final VideoService service = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (requireUser(req, resp) == null) return;
        req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = requireUser(req, resp);
        if (user == null) return;
        req.setCharacterEncoding("UTF-8");

        String title = parameter(req, "title").trim();
        if (title.isBlank()) {
            req.setAttribute("alert", "Tiêu đề video không được để trống.");
            req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
            return;
        }

        String savedFile = null;
        try {
            Part file = req.getPart("videoFile");
            savedFile = UploadUtil.saveVideo(file);
            if (savedFile == null) {
                req.setAttribute("alert", "Vui lòng chọn file video.");
                req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
                return;
            }

            Video video = new Video();
            video.setTitle(title);
            video.setDescription(parameter(req, "description").trim());
            video.setFileName(savedFile);
            video.setMimeType(UploadUtil.videoMimeType(savedFile));
            video.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            service.insert(video, user.getId());
            resp.sendRedirect(req.getContextPath() + "/video?created=1");
        } catch (IOException | IllegalStateException e) {
            if (savedFile != null) UploadUtil.deleteVideo(savedFile);
            getServletContext().log("Không tải được video lên.", e);
            req.setAttribute("alert", e.getMessage() == null
                    ? "Không thể tải video. Kiểm tra dung lượng file." : e.getMessage());
            req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
        }
    }
}
