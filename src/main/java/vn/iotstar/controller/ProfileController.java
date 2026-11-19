package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.model.User;
import vn.iotstar.util.CloudinaryService;
import vn.iotstar.util.FormValidation;

@WebServlet("/profile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5L * 1024 * 1024,
        maxRequestSize = 6L * 1024 * 1024)
public class ProfileController extends ProfileControllerSupport {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User account = requireUser(req, resp);
        if (account == null) return;
        User profile = loadProfile(req, resp, account.getId());
        if (profile == null) return;
        req.setAttribute("newEmail", "");
        forwardProfile(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User account = requireUser(req, resp);
        if (account == null) return;
        req.setCharacterEncoding("UTF-8");

        User profile = userService.findById(account.getId());
        if (profile == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fullname = value(req, "fullname").trim();
        String phone = value(req, "phone").trim();
        String newEmail = value(req, "newEmail").trim().toLowerCase();
        req.setAttribute("newEmail", newEmail);

        String validationMessage = null;
        if (fullname.isBlank() || fullname.length() > 255) {
            validationMessage = "Họ tên không được để trống và tối đa 255 ký tự.";
        } else if (!FormValidation.validPhone(phone)) {
            validationMessage = "Số điện thoại không hợp lệ.";
        } else if (!newEmail.isBlank() && !FormValidation.validEmail(newEmail)) {
            validationMessage = "Email mới không hợp lệ.";
        } else if (!newEmail.isBlank()
                && !newEmail.equalsIgnoreCase(profile.getEmail())
                && !userService.isEmailAvailable(newEmail, profile.getId())) {
            validationMessage = "Email này đã được tài khoản khác sử dụng.";
        }

        if (validationMessage != null) {
            profile.setFullname(fullname);
            profile.setPhone(phone);
            req.setAttribute("profile", profile);
            req.setAttribute("alert", validationMessage);
            forwardProfile(req, resp);
            return;
        }

        try {
            Part avatar = req.getPart("avatar");
            String avatarUrl = CloudinaryService.uploadProfileImage(avatar);
            profile.setFullname(fullname);
            profile.setPhone(phone);
            if (avatarUrl != null) profile.setAvatar(avatarUrl);
            userService.updateProfile(profile);
            req.getSession(false).setAttribute("account", profile);

            boolean changingEmail = !newEmail.isBlank()
                    && !newEmail.equalsIgnoreCase(profile.getEmail());
            if (changingEmail) {
                userService.requestProfileEmailChange(profile.getId(), newEmail);
                User refreshed = userService.findById(profile.getId());
                req.getSession(false).setAttribute("account", refreshed);
                resp.sendRedirect(req.getContextPath() + "/profile?otpSent=1");
            } else {
                resp.sendRedirect(req.getContextPath() + "/profile?updated=1");
            }
        } catch (IOException | IllegalStateException e) {
            getServletContext().log("Không cập nhật được hồ sơ hoặc tải ảnh lên Cloudinary.", e);
            User refreshed = userService.findById(profile.getId());
            if (refreshed != null) {
                profile = refreshed;
                req.getSession(false).setAttribute("account", refreshed);
            }
            req.setAttribute("profile", profile);
            req.setAttribute("alert", e.getMessage() == null
                    ? "Không thể cập nhật hồ sơ. Kiểm tra Cloudinary và thử lại." : e.getMessage());
            forwardProfile(req, resp);
        } catch (RuntimeException e) {
            getServletContext().log("Không xử lý được yêu cầu cập nhật hồ sơ.", e);
            User refreshed = userService.findById(profile.getId());
            if (refreshed != null) {
                profile = refreshed;
                req.getSession(false).setAttribute("account", refreshed);
            }
            req.setAttribute("profile", profile);
            req.setAttribute("alert", "Không thể hoàn tất cập nhật hồ sơ. Kiểm tra cấu hình Cloudinary/SMTP và thử lại.");
            forwardProfile(req, resp);
        }
    }
}
