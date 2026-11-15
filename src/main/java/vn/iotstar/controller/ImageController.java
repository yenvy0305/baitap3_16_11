package vn.iotstar.controller;

import java.io.IOException;
import java.nio.file.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.util.Constant;

@WebServlet("/image")
public class ImageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String filename = req.getParameter("fname");
        if (filename == null || filename.isBlank()
                || !Paths.get(filename).getFileName().toString().equals(filename)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path file = Paths.get(Constant.UPLOAD_DIR).resolve(filename).normalize();
        if (!Files.exists(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String type = Files.probeContentType(file);
        resp.setContentType(type == null ? "application/octet-stream" : type);
        Files.copy(file, resp.getOutputStream());
    }
}