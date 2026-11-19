package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.FormValidation;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = value(req, "email").trim().toLowerCase();
        req.setAttribute("email", email);

        if ("request".equals(req.getParameter("action"))) {
            if (!FormValidation.validEmail(email)) {
                req.setAttribute("alert", "Email không hợp lệ.");
                req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
                return;
            }
            try {
                service.requestPasswordReset(email);
                req.setAttribute("step", "verify");
                req.setAttribute("message", "Nếu email thuộc tài khoản đã kích hoạt, mã đặt lại mật khẩu đã được gửi.");
            } catch (RuntimeException e) {
                getServletContext().log("Không gửi được OTP đặt lại mật khẩu.", e);
                req.setAttribute("step", "verify");
                req.setAttribute("alert", "Chưa gửi được email. Kiểm tra cấu hình SMTP và thử yêu cầu mã mới.");
            }
        } else {
            String password = value(req, "newPassword");
            String confirm = value(req, "confirmPassword");
            req.setAttribute("step", "verify");
            if (!FormValidation.validEmail(email)) {
                req.setAttribute("alert", "Email không hợp lệ.");
            } else if (!value(req, "otp").trim().matches("[0-9]{6}")) {
                req.setAttribute("alert", "OTP phải gồm đúng 6 chữ số.");
            } else if (password.length() < 8 || password.length() > 128) {
                req.setAttribute("alert", "Mật khẩu mới phải dài từ 8 đến 128 ký tự.");
            } else if (!password.equals(confirm)) {
                req.setAttribute("alert", "Hai mật khẩu không trùng nhau.");
            } else if (service.resetPassword(email, value(req, "otp").trim(), password)) {
                resp.sendRedirect(req.getContextPath() + "/login?passwordReset=1");
                return;
            } else {
                req.setAttribute("alert", "OTP không đúng, đã hết hạn hoặc email không hợp lệ.");
            }
        }
        req.getRequestDispatcher("/views/forgot-password.jsp").forward(req, resp);
    }

    private String value(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }
}
