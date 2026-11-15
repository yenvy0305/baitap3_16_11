package vn.iotstar.config;

import jakarta.persistence.EntityManager;

public class JpaSmokeTest {
    public static void main(String[] args) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            Long categoryCount = em.createQuery(
                    "SELECT COUNT(c) FROM Category c", Long.class)
                .getSingleResult();

            Long userCount = em.createQuery(
                    "SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();

            System.out.println("Kết nối JPA thành công.");
            System.out.println("Số Category: " + categoryCount);
            System.out.println("Số User: " + userCount);
        } finally {
            em.close();
            JpaConfig.close();
        }
    }
}