package vn.iotstar.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import jakarta.servlet.http.Part;

public class UploadUtil {
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
}