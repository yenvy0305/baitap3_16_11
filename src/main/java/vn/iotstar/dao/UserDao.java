package vn.iotstar.dao;

import vn.iotstar.model.User;

public interface UserDao {
    void insert(User user);
    User findByUsername(String username);
    boolean existsEmail(String email);
    boolean existsUsername(String username);
    boolean existsPhone(String phone);
}