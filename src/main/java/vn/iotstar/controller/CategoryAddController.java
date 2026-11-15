package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.UploadUtil;

@WebServlet("/admin/category/add")
@MultipartConfig
public class CategoryAddController extends HttpServlet {
    private final CategoryService service = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/views/admin/add-category.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Category category = new Category();
        category.setCateName(req.getParameter("cateName").trim());
        category.setIcon(UploadUtil.saveImage(req.getPart("icon")));

        if (category.getCateName().isBlank()) {
            req.setAttribute("alert", "Tên danh mục không được để trống.");
            req.getRequestDispatcher("/views/admin/add-category.jsp")
               .forward(req, resp);
            return;
        }

        service.insert(category);
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}