package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;

abstract class VideoControllerSupport extends HttpServlet {
    protected User requireUser(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        Object account = session == null ? null : session.getAttribute("account");
        if (account instanceof User user) return user;
        resp.sendRedirect(req.getContextPath() + "/login");
        return null;
    }

    protected int parameterId(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(req.getParameter(name));
        } catch (RuntimeException e) {
            return -1;
        }
    }

    protected String parameter(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value;
    }
}
