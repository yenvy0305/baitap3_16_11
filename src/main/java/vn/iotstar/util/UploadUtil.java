package vn.iotstar.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.UUID;

import jakarta.servlet.http.Part;

public class UploadUtil {
    private static final long MAX_VIDEO_SIZE = 200L * 1024 * 1024;

    public static String saveImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0) return null;

        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File tải lên phải là ảnh.");
        }

        String original = Paths.get(part.getSubmittedFileName())
                               .getFileName().toString();
        String extension = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) extension = original.substring(dot).toLowerCase();

        String filename = UUID.randomUUID() + extension;
        Path directory = Paths.get(Constant.UPLOAD_DIR);
        Files.createDirectories(directory);

        part.write(directory.resolve(filename).toString());
        return filename;
    }

    public static String saveVideo(Part part) throws IOException {
        if (part == null || part.getSize() == 0) return null;
        if (part.getSize() > MAX_VIDEO_SIZE) {
            throw new IOException("Video vượt quá dung lượng cho phép 200 MB.");
        }

        String originalName = part.getSubmittedFileName();
        if (originalName == null || originalName.isBlank()) {
            throw new IOException("Vui lòng chọn file video.");
        }
        String safeName = Paths.get(originalName).getFileName().toString();
        int dot = safeName.lastIndexOf('.');
        if (dot < 0) throw new IOException("Video cần có phần mở rộng mp4, webm hoặc ogg.");

        String extension = safeName.substring(dot).toLowerCase(Locale.ROOT);
        if (!extension.equals(".mp4") && !extension.equals(".webm")
                && !extension.equals(".ogg")) {
            throw new IOException("Chỉ hỗ trợ video mp4, webm hoặc ogg.");
        }

        String filename = UUID.randomUUID() + extension;
        Path directory = Paths.get(Constant.VIDEO_UPLOAD_DIR);
        Files.createDirectories(directory);
        part.write(directory.resolve(filename).toString());
        return filename;
    }

    public static String videoMimeType(String filename) {
        String lower = filename == null ? "" : filename.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".webm")) return "video/webm";
        if (lower.endsWith(".ogg")) return "video/ogg";
        return "video/mp4";
    }

    public static void deleteVideo(String filename) throws IOException {
        if (filename == null || filename.isBlank()
                || !Paths.get(filename).getFileName().toString().equals(filename)) {
            return;
        }
        Files.deleteIfExists(Paths.get(Constant.VIDEO_UPLOAD_DIR).resolve(filename).normalize());
    }
}
