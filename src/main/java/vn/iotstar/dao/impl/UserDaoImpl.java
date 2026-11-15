package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.UserDao;
import vn.iotstar.model.User;

public class UserDaoImpl implements UserDao {

    @Override
    public void insert(User user) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public User findByUsername(String username) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            var results = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();

            return results.isEmpty() ? null : results.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsEmail(String email) {
        return exists("u.email", email);
    }

    @Override
    public boolean existsUsername(String username) {
        return exists("u.username", username);
    }

    @Override
    public boolean existsPhone(String phone) {
        return phone != null && !phone.isBlank()
                && exists("u.phone", phone);
    }

    private boolean exists(String property, String value) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE " +
                    property + " = :value",
                    Long.class)
                .setParameter("value", value)
                .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }
}