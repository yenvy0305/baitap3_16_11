package vn.iotstar.controller;

import java.io.IOException;

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

@WebServlet("/video/edit")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 200L * 1024 * 1024,
        maxRequestSize = 210L * 1024 * 1024)
public class VideoEditController extends VideoControllerSupport {
    private final VideoService service = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = requireUser(req, resp);
        if (user == null) return;
        Video video = service.getOwned(parameterId(req, "id"), user.getId());
        if (video == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("video", video);
        req.setAttribute("editing", true);
        req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = requireUser(req, resp);
        if (user == null) return;
        req.setCharacterEncoding("UTF-8");
        int id = parameterId(req, "id");
        Video video = service.getOwned(id, user.getId());
        if (video == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String title = parameter(req, "title").trim();
        if (title.isBlank() || title.length() > 255) {
            req.setAttribute("alert", "Tiêu đề video không được để trống và tối đa 255 ký tự.");
            video.setTitle(title);
            req.setAttribute("video", video);
            req.setAttribute("editing", true);
            req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
            return;
        }

        String oldFile = video.getFileName();
        String newFile = null;
        try {
            String description = parameter(req, "description").trim();
            if (description.length() > 4000) {
                req.setAttribute("alert", "Mô tả tối đa 4000 ký tự.");
                video.setTitle(title);
                video.setDescription(description);
                req.setAttribute("video", video);
                req.setAttribute("editing", true);
                req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
                return;
            }
            Part file = req.getPart("videoFile");
            if (file != null && file.getSize() > 0) {
                newFile = UploadUtil.saveVideo(file);
                video.setFileName(newFile);
                video.setMimeType(UploadUtil.videoMimeType(newFile));
            }
            video.setTitle(title);
            video.setDescription(description);
            service.edit(video);
            if (newFile != null) {
                try {
                    UploadUtil.deleteVideo(oldFile);
                } catch (IOException e) {
                    getServletContext().log("Không xóa được file video cũ: " + oldFile, e);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/video?updated=1");
        } catch (IOException | IllegalStateException e) {
            if (newFile != null) UploadUtil.deleteVideo(newFile);
            getServletContext().log("Không cập nhật được video.", e);
            req.setAttribute("alert", e.getMessage() == null
                    ? "Không thể cập nhật video. Kiểm tra dung lượng file." : e.getMessage());
            video.setTitle(title);
            req.setAttribute("video", video);
            req.setAttribute("editing", true);
            req.getRequestDispatcher("/views/video/form.jsp").forward(req, resp);
        }
    }
}
