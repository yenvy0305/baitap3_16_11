package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;
import vn.iotstar.model.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.UploadUtil;

@WebServlet("/video/delete")
public class VideoDeleteController extends VideoControllerSupport {
    private final VideoService service = new VideoServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = requireUser(req, resp);
        if (user == null) return;
        int id = parameterId(req, "id");
        Video video = service.getOwned(id, user.getId());
        if (video == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (service.deleteOwned(id, user.getId())) {
            try {
                UploadUtil.deleteVideo(video.getFileName());
            } catch (IOException e) {
                getServletContext().log("Không xóa được file video: " + video.getFileName(), e);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/video?deleted=1");
    }
}
