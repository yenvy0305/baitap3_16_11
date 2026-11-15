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
        int id = Integer.parseInt(req.getParameter("id"));
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
        int id = Integer.parseInt(req.getParameter("cateId"));

        Category category = service.get(id);
        if (category == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        category.setCateName(req.getParameter("cateName").trim());
        String newImage = UploadUtil.saveImage(req.getPart("icon"));
        if (newImage != null) category.setIcon(newImage);

        service.edit(category);
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}