package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.UploadUtil;

@WebServlet("/admin/category/edit")
@MultipartConfig
public class CategoryEditController extends HttpServlet {
    private final CategoryService service = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseId(req.getParameter("id"));
        if (id < 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Category category = service.get(id);
        if (category == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher("/views/admin/edit-category.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = parseId(req.getParameter("cateId"));
        if (id < 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Category category = service.get(id);
        if (category == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String name = req.getParameter("cateName");
        name = name == null ? "" : name.trim();
        if (name.isBlank() || name.length() > 255) {
            req.setAttribute("alert", "Tên danh mục không được để trống và tối đa 255 ký tự.");
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views/admin/edit-category.jsp")
               .forward(req, resp);
            return;
        }

        category.setCateName(name);
        try {
            String newImage = UploadUtil.saveImage(req.getPart("icon"));
            if (newImage != null) category.setIcon(newImage);
        } catch (IOException e) {
            req.setAttribute("alert", e.getMessage());
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views/admin/edit-category.jsp")
               .forward(req, resp);
            return;
        }

        service.edit(category);
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    private int parseId(String value) {
        try {
            return Integer.parseInt(value);
        } catch (RuntimeException e) {
            return -1;
        }
    }
}
