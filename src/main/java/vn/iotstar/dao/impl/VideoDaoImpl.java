package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.VideoDao;
import vn.iotstar.model.User;
import vn.iotstar.model.Video;

public class VideoDaoImpl implements VideoDao {
    @Override
    public void insert(Video video, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            video.setUser(em.getReference(User.class, userId));
            em.persist(video);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void edit(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(video);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteOwned(int videoId, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int deleted = em.createQuery(
                    "DELETE FROM Video v WHERE v.videoId = :videoId AND v.user.id = :userId")
                .setParameter("videoId", videoId)
                .setParameter("userId", userId)
                .executeUpdate();
            tx.commit();
            return deleted > 0;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Video getOwned(int videoId, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Video> result = em.createQuery(
                    "SELECT v FROM Video v WHERE v.videoId = :videoId AND v.user.id = :userId",
                    Video.class)
                .setParameter("videoId", videoId)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public Video get(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Video.class, videoId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> getByUser(int userId, int offset, int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Video v WHERE v.user.id = :userId " +
                    "ORDER BY v.createdAt DESC, v.videoId DESC",
                    Video.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByUser(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId",
                    Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> getLatest(int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Video v ORDER BY v.createdAt DESC, v.videoId DESC",
                    Video.class)
                .setMaxResults(limit)
                .getResultList();
        } finally {
            em.close();
        }
    }
}
