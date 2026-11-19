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

@WebServlet("/activate")
public class ActivationController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Object email = req.getSession().getAttribute("activationEmail");
        req.setAttribute("email", email == null ? "" : email.toString());
        req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = value(req, "email").trim().toLowerCase();
        req.setAttribute("email", email);

        try {
            if ("resend".equals(req.getParameter("action"))) {
                if (!FormValidation.validEmail(email)) {
                    req.setAttribute("alert", "Email không hợp lệ.");
                    req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
                    return;
                }
                service.sendActivationOtp(email);
                req.setAttribute("message", "Nếu tài khoản chưa kích hoạt, OTP mới đã được gửi.");
            } else if (!FormValidation.validEmail(email)
                    || !value(req, "otp").trim().matches("[0-9]{6}")) {
                req.setAttribute("alert", "Email hoặc OTP 6 chữ số không hợp lệ.");
            } else if (service.activate(email, value(req, "otp").trim())) {
                req.getSession().removeAttribute("activationEmail");
                resp.sendRedirect(req.getContextPath() + "/login?activated=1");
                return;
            } else {
                req.setAttribute("alert", "OTP không đúng, đã hết hạn hoặc tài khoản không hợp lệ.");
            }
        } catch (RuntimeException e) {
            getServletContext().log("Lỗi xử lý kích hoạt tài khoản.", e);
            req.setAttribute("alert", "Không thể gửi OTP. Kiểm tra cấu hình email rồi thử gửi lại.");
        }
        req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
    }

    private String value(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }
}
