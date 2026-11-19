package vn.iotstar.util;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

public final class CloudinaryService {
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;
    private static volatile Cloudinary cloudinary;

    private CloudinaryService() {
    }

    public static String uploadProfileImage(Part part) throws IOException {
        if (part == null || part.getSize() == 0) return null;
        if (part.getSize() > MAX_IMAGE_SIZE) {
            throw new IOException("Ảnh đại diện không được vượt quá 5 MB.");
        }

        String contentType = part.getContentType();
        String originalName = part.getSubmittedFileName();
        String lowerName = originalName == null ? "" : originalName.toLowerCase(Locale.ROOT);
        boolean allowedType = "image/jpeg".equalsIgnoreCase(contentType)
                || "image/png".equalsIgnoreCase(contentType)
                || "image/webp".equalsIgnoreCase(contentType);
        boolean allowedExtension = lowerName.endsWith(".jpg")
                || lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".png")
                || lowerName.endsWith(".webp");
        if (!allowedType || !allowedExtension) {
            throw new IOException("Chỉ chấp nhận ảnh JPG, PNG hoặc WebP.");
        }

        byte[] imageBytes;
        try (var input = part.getInputStream()) {
            imageBytes = input.readAllBytes();
        }

        Map<?, ?> result = getCloudinary().uploader().upload(
                imageBytes,
                ObjectUtils.asMap(
                        "resource_type", "image",
                        "folder", "mvc3tier/profile",
                        "public_id", UUID.randomUUID().toString(),
                        "overwrite", false));
        Object secureUrl = result.get("secure_url");
        if (!(secureUrl instanceof String url) || url.isBlank()) {
            throw new IOException("Cloudinary không trả về URL ảnh.");
        }
        return url;
    }

    private static Cloudinary getCloudinary() {
        Cloudinary instance = cloudinary;
        if (instance != null) return instance;

        synchronized (CloudinaryService.class) {
            instance = cloudinary;
            if (instance == null) {
                instance = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", required("CLOUDINARY_CLOUD_NAME"),
                        "api_key", required("CLOUDINARY_API_KEY"),
                        "api_secret", required("CLOUDINARY_API_SECRET"),
                        "secure", true));
                cloudinary = instance;
            }
        }
        return instance;
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Thiếu cấu hình môi trường " + name + ".");
        }
        return value;
    }
}
