package vn.iotstar.controller;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;
import vn.iotstar.util.Constant;
import vn.iotstar.util.UploadUtil;

@WebServlet("/video/file")
public class VideoFileController extends VideoControllerSupport {
    private static final int BUFFER_SIZE = 16 * 1024;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = requireUser(req, resp);
        if (user == null) return;

        String filename = req.getParameter("name");
        if (!isSafeVideoName(filename)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path path = Paths.get(Constant.VIDEO_UPLOAD_DIR).resolve(filename).normalize();
        if (!path.startsWith(Paths.get(Constant.VIDEO_UPLOAD_DIR).normalize())
                || !Files.isRegularFile(path)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        long size = Files.size(path);
        long start = 0;
        long end = size - 1;
        String range = req.getHeader("Range");
        boolean partial = range != null && !range.isBlank();

        if (partial) {
            long[] bounds = parseRange(range, size);
            if (bounds == null) {
                resp.setHeader("Content-Range", "bytes */" + size);
                resp.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }
            start = bounds[0];
            end = bounds[1];
        }

        resp.setContentType(UploadUtil.videoMimeType(filename));
        resp.setHeader("Accept-Ranges", "bytes");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        resp.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        resp.setHeader("Content-Length", Long.toString(end - start + 1));
        if (partial) {
            resp.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            resp.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
        }

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r");
             ServletOutputStream output = resp.getOutputStream()) {
            file.seek(start);
            byte[] buffer = new byte[BUFFER_SIZE];
            long remaining = end - start + 1;
            while (remaining > 0) {
                int read = file.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                if (read < 0) break;
                output.write(buffer, 0, read);
                remaining -= read;
            }
        }
    }

    private boolean isSafeVideoName(String filename) {
        if (filename == null || filename.isBlank()
                || !Paths.get(filename).getFileName().toString().equals(filename)) {
            return false;
        }
        return filename.toLowerCase().endsWith(".mp4")
                || filename.toLowerCase().endsWith(".webm")
                || filename.toLowerCase().endsWith(".ogg");
    }

    private long[] parseRange(String header, long size) {
        if (!header.startsWith("bytes=") || size <= 0) return null;
        String[] values = header.substring(6).split("-", -1);
        if (values.length != 2) return null;
        try {
            long start;
            long end;
            if (values[0].isEmpty()) {
                long suffixLength = Long.parseLong(values[1]);
                if (suffixLength <= 0) return null;
                start = Math.max(0, size - suffixLength);
                end = size - 1;
            } else {
                start = Long.parseLong(values[0]);
                end = values[1].isEmpty() ? size - 1 : Long.parseLong(values[1]);
                if (start < 0 || start >= size || end < start) return null;
                end = Math.min(end, size - 1);
            }
            return new long[]{start, end};
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
