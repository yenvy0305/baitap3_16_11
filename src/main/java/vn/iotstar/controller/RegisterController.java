package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

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
        user.setEmail(req.getParameter("email").trim());
        user.setUsername(req.getParameter("username").trim());
        user.setFullname(req.getParameter("fullname").trim());
        user.setPassword(req.getParameter("password"));
        user.setPhone(req.getParameter("phone").trim());

        if (user.getEmail().isBlank() || user.getUsername().isBlank()
                || user.getFullname().isBlank() || user.getPassword().isBlank()) {
            req.setAttribute("alert", "Vui lòng điền các thông tin bắt buộc.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        if (!service.register(user)) {
            req.setAttribute("alert", "Email, tài khoản hoặc điện thoại đã được dùng.");
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/login?registered=1");
    }
}