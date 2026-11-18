package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") instanceof User) {
            routeByRole(req, resp, (User) session.getAttribute("account"));
            return;
        }

        String rememberedUsername = "";
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    rememberedUsername = cookie.getValue();
                    break;
                }
            }
        }

        req.setAttribute("rememberedUsername", rememberedUsername);
        req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            req.setAttribute("alert", "Vui lòng nhập tài khoản và mật khẩu.");
            req.setAttribute("rememberedUsername", username);
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        User user = service.login(username.trim(), password);

        if (user == null) {
            req.setAttribute("alert", "Thông tin đăng nhập không đúng hoặc tài khoản chưa được kích hoạt.");
            req.setAttribute("rememberedUsername", username.trim());
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
            return;
        }

        String cookiePath = req.getContextPath().isEmpty()
                ? "/" : req.getContextPath();

        if ("on".equals(req.getParameter("remember"))) {
            Cookie cookie = new Cookie("username", user.getUsername());
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
            resp.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie("username", "");
            cookie.setMaxAge(0);
            cookie.setPath(cookiePath);
            resp.addCookie(cookie);
        }

        HttpSession authenticatedSession = req.getSession(true);
        req.changeSessionId();
        authenticatedSession.setAttribute("account", user);
        routeByRole(req, resp, user);
    }

    private void routeByRole(HttpServletRequest req, HttpServletResponse resp,
                             User user)
            throws ServletException, IOException {
        if (user.getRoleid() == 1) {
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } else if (user.getRoleid() == 2) {
            resp.sendRedirect(req.getContextPath() + "/manager/home");
        } else {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }
}
