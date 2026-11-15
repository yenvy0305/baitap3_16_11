package vn.iotstar.service.impl;

import java.sql.Timestamp;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;

public class UserServiceImpl implements UserService {
    private final UserDao dao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        User user = dao.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean register(User user) {
        if (dao.existsEmail(user.getEmail())
                || dao.existsUsername(user.getUsername())
                || dao.existsPhone(user.getPhone())) {
            return false;
        }

        user.setRoleid(5);
        user.setAvatar(null);
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        dao.insert(user);
        return true;
    }
}