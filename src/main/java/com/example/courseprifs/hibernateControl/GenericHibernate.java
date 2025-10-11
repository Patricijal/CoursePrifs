/**
 * Helper klase, skirta darbui su duomenu baze t.y. perduosiu objektus, atidarysiu sesija, sesijos metu kazka atliksiu, baigsiu darba
 */

package com.example.courseprifs.hibernateControl;

import com.example.courseprifs.model.BasicUser;
import com.example.courseprifs.model.Cuisine;
import com.example.courseprifs.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class GenericHibernate {
    EntityManagerFactory entityManagerFactory;
    EntityManager entityManager;

    public GenericHibernate(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    // generic method
    public <T> void create(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(entity); // INSERT
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            // Klaidos atveju as panaudosiu Alertus
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
