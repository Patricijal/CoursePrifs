package com.example.courseprifs.hibernateControl;

import com.example.courseprifs.model.FoodOrder;
import com.example.courseprifs.model.Restaurant;
import com.example.courseprifs.model.User;
import com.example.courseprifs.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

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
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Oh no", "DB error", "Something went wrong getting Restaurant orders");
        }
        return orders;
    }
}
