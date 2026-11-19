package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;

@WebServlet("/profile/resend-email-otp")
public class ProfileEmailResendController extends ProfileControllerSupport {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User account = requireUser(req, resp);
        if (account == null) return;
        try {
            userService.resendProfileEmailOtp(account.getId());
            resp.sendRedirect(req.getContextPath() + "/profile?otpSent=1");
        } catch (RuntimeException e) {
            getServletContext().log("Không gửi lại được OTP thay đổi email.", e);
            req.setAttribute("alert", "Không gửi lại được OTP. Kiểm tra cấu hình email.");
            loadProfile(req, resp, account.getId());
            forwardProfile(req, resp);
        }
    }
}
