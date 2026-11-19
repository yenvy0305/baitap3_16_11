package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

abstract class ProfileControllerSupport extends HttpServlet {
    protected final UserService userService = new UserServiceImpl();

    protected User requireUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        Object value = session == null ? null : session.getAttribute("account");
        if (value instanceof User user) return user;
        resp.sendRedirect(req.getContextPath() + "/login");
        return null;
    }

    protected User loadProfile(HttpServletRequest req, HttpServletResponse resp,
                               int userId)
            throws ServletException, IOException {
        User profile = userService.findById(userId);
        if (profile == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        req.setAttribute("profile", profile);
        return profile;
    }

    protected void forwardProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }

    protected String value(HttpServletRequest req, String name) {
        String result = req.getParameter(name);
        return result == null ? "" : result;
    }
}
