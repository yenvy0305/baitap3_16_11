package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.FormValidation;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        User user = new User();
        user.setEmail(value(req, "email").trim().toLowerCase());
        user.setUsername(value(req, "username").trim());
        user.setFullname(value(req, "fullname").trim());
        user.setPassword(value(req, "password"));
        user.setPhone(value(req, "phone").trim());

        String validationMessage = null;
        if (!FormValidation.validEmail(user.getEmail())) {
            validationMessage = "Email không hợp lệ.";
        } else if (!FormValidation.validUsername(user.getUsername())) {
            validationMessage = "Tài khoản dài 3–100 ký tự, chỉ gồm chữ, số, dấu chấm, gạch dưới hoặc gạch ngang.";
        } else if (user.getFullname().isBlank() || user.getFullname().length() > 255) {
            validationMessage = "Họ tên không được trống và tối đa 255 ký tự.";
        } else if (!FormValidation.validPhone(user.getPhone())) {
            validationMessage = "Số điện thoại không hợp lệ.";
        } else if (user.getPassword().length() < 8 || user.getPassword().length() > 128) {
            validationMessage = "Mật khẩu phải dài từ 8 đến 128 ký tự.";
        }
        if (validationMessage != null) {
            req.setAttribute("alert", validationMessage);
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            if (!service.register(user)) {
                req.setAttribute("alert", "Email, tài khoản hoặc điện thoại đã được dùng.");
                req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
                return;
            }
            req.getSession(true).setAttribute("activationEmail", user.getEmail());
            resp.sendRedirect(req.getContextPath() + "/activate");
        } catch (RuntimeException e) {
            getServletContext().log("Không hoàn tất gửi OTP đăng ký.", e);
            req.getSession(true).setAttribute("activationEmail", user.getEmail());
            resp.sendRedirect(req.getContextPath() + "/activate?sendError=1");
        }
    }

    private String value(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }
}
