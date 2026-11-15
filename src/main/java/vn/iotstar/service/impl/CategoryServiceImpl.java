package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.model.Category;
import vn.iotstar.service.CategoryService;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao dao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) {
        dao.insert(category);
    }

    @Override
    public void edit(Category category) {
        dao.edit(category);
    }

    @Override
    public void delete(int id) {
        dao.delete(id);
    }

    @Override
    public Category get(int id) {
        return dao.get(id);
    }

    @Override
    public List<Category> getAll() {
        return dao.getAll();
    }

    @Override
    public Category get(String name) {
        return dao.get(name);
    }

    @Override
    public List<Category> search(String keyword) {
        return dao.search(keyword);
    }
}