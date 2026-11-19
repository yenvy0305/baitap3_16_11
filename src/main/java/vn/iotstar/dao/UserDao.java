package vn.iotstar.dao;

import vn.iotstar.model.User;

public interface UserDao {
    void insert(User user);
    void update(User user);
    User findByUsername(String username);
    User findById(int id);
    User findByEmail(String email);
    boolean existsEmail(String email);
    boolean existsUsername(String username);
    boolean existsPhone(String phone);
}
