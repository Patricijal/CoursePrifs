package com.example.courseprifs.hibernateControl;

import com.example.courseprifs.model.*;
import com.example.courseprifs.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomHibernate extends GenericHibernate {
    public CustomHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public User getUserByCredentials(String username, String password) {
        User user = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);

            query.select(root).where(cb.and(
                    cb.equal(root.get("login"), username),
                    cb.equal(root.get("password"), password)
            ));
            Query q = entityManager.createQuery(query);
            user = (User) q.getSingleResult();
        } catch (Exception e) {
            // Handle exception (e.g., user not found)
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting User by credentials");
        }
        return user;
    }

    public List<FoodOrder> getRestaurantOrders(Restaurant restaurant) {
        List<FoodOrder> orders = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> query = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            query.select(root).where(
                    cb.equal(root.get("restaurant"), restaurant)
            );
            Query q = entityManager.createQuery(query);
            orders = q.getResultList();
        } catch (Exception e) {
            // Handle exception (e.g., user not found)
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting Restaurant Orders");
        }
        return orders;
    }

    public List<FoodOrder> getFilteredRestaurantOrders(OrderStatus orderStatus, BasicUser client, LocalDate start, LocalDate end, Restaurant restaurant) {
        List<FoodOrder> orders = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<FoodOrder> query = cb.createQuery(FoodOrder.class);
            Root<FoodOrder> root = query.from(FoodOrder.class);

            // Build dynamic predicates based on provided filters
            List<Predicate> predicates = new ArrayList<>();
            // Restaurant filter (mandatory for restaurant users, optional for others)
            if (restaurant != null) {
                predicates.add(cb.equal(root.get("restaurant"), restaurant));
            }
            // Order status filter
            if (orderStatus != null) {
                predicates.add(cb.equal(root.get("orderStatus"), orderStatus));
            }
            // Client filter
            if (client != null) {
                predicates.add(cb.equal(root.get("buyer"), client));
            }
            // Date range filter
            if (start != null && end != null) {
                predicates.add(cb.between(root.get("dateCreated"), start, end));
            } else if (start != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateCreated"), start));
            } else if (end != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateCreated"), end));
            }
            // Apply all predicates
            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }
            // Order by date (most recent first)
            query.orderBy(cb.desc(root.get("dateCreated")));
            TypedQuery<FoodOrder> q = entityManager.createQuery(query);
//            // cia bus Predicates
//            if (restaurant != null) {
//                query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
//            } else {
//                query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
//            }
//            Query q = entityManager.createQuery(query);
            orders = q.getResultList();
        } catch (Exception e) {
            // Handle exception (e.g., user not found)
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting Filtered Restaurant Orders: " + e.getMessage());
        }
        return orders;
    }

    public List<Cuisine> getRestaurantCuisine(Restaurant restaurant) {
        List<Cuisine> menu = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Cuisine> query = cb.createQuery(Cuisine.class);
            Root<Cuisine> root = query.from(Cuisine.class);

            query.select(root).where(cb.equal(root.get("restaurant"), restaurant));
            Query q = entityManager.createQuery(query);
            menu = q.getResultList();
        } catch (Exception e) {
            // Handle exception (e.g., user not found)
            FxUtils.generateExceptionAlert(Alert.AlertType.ERROR, "Something went wrong getting Restaurant Cuisine", e);
        }
        return menu;
    }

//    public List<Review> getChatMessages(Chat chat) {
//        List<Review> messages = new ArrayList<>();
//        try {
//            entityManager = entityManagerFactory.createEntityManager();
//            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//            CriteriaQuery<Review> query = cb.createQuery(Review.class);
//            Root<Review> root = query.from(Review.class);
//
//            query.select(root).where(cb.equal(root.get("chat"), chat));
//            Query q = entityManager.createQuery(query);
//            messages = q.getResultList();
//        } catch (Exception e) {
//            // Handle exception (e.g., user not found)
//            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting Chat Messages");
//        }
//        return messages;
//    }

    public List<Review> getChatMessages(Chat chat) {
        List<Review> messages = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Review> query = cb.createQuery(Review.class);
            Root<Review> root = query.from(Review.class);

            query.select(root).where(cb.equal(root.get("chat"), chat));
            Query q = entityManager.createQuery(query);
            messages = q.getResultList();
        } catch (Exception e) {
            // Handle exception (e.g., user not found)
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting Chat Messages");
        }
        return messages;
    }
}
