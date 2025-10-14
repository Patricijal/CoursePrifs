/**
 * Helper klase, skirta darbui su duomenu baze t.y. perduosiu objektus, atidarysiu sesija, sesijos metu kazka atliksiu, baigsiu darba
 */

package com.example.courseprifs.hibernateControl;

import com.example.courseprifs.model.BasicUser;
import com.example.courseprifs.model.Cuisine;
import com.example.courseprifs.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

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

    public <T> void update(T entity) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(entity); // UPDATE
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            // Klaidos atveju as panaudosiu Alertus
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> void delete(Class<T> entityClass, int id) {
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            T entityFromDb = entityManager.find(entityClass, id);
            entityManager.remove(entityFromDb); // DELETE
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Klaidos atveju as panaudosiu Alertus
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public <T> T getEntityById(Class<T> entity, int id) {
        T entityFromDb = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityFromDb = entityManager.find(entity, id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            // Klaidos atveju as panaudosiu Alertus
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return entityFromDb;
    }

    public <T> List<T> getAllRecords(Class<T> entityClass) {
        List<T> list = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaQuery query = entityManager.getCriteriaBuilder().createQuery();
            query.select(query.from(entityClass));
            Query q = entityManager.createQuery(query);
            list = q.getResultList();
        } catch (Exception e) {
            // Klaidos atveju as panaudosiu Alertus
        } finally {
            if (entityManager != null) entityManager.close();
        }
        return list;
    }
}
