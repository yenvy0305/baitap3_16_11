package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;

@WebServlet("/profile/verify-email")
public class ProfileEmailVerifyController extends ProfileControllerSupport {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User account = requireUser(req, resp);
        if (account == null) return;
        String otp = value(req, "otp").trim();
        if (!otp.matches("[0-9]{6}")) {
            req.setAttribute("alert", "OTP phải gồm đúng 6 chữ số.");
            loadProfile(req, resp, account.getId());
            forwardProfile(req, resp);
            return;
        }
        if (userService.verifyProfileEmailChange(account.getId(), otp)) {
            User refreshed = userService.findById(account.getId());
            HttpSession session = req.getSession(false);
            if (session != null) session.setAttribute("account", refreshed);
            resp.sendRedirect(req.getContextPath() + "/profile?emailUpdated=1");
            return;
        }
        req.setAttribute("alert", "OTP không đúng, đã hết hạn hoặc email không còn khả dụng.");
        loadProfile(req, resp, account.getId());
        forwardProfile(req, resp);
    }
}
