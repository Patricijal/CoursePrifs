package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatForm {
    public ListView<Review> messageList;
    public TextArea messageBody;
    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;
    private FoodOrder currentFoodOrder;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, FoodOrder currentFoodOrder) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        this.currentFoodOrder = currentFoodOrder;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
    }

    public void sendMessage() {
//        FoodOrder managedFoodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
//        if (managedFoodOrder.getChat() == null) {
//            Chat chat = new Chat("Chat no " + managedFoodOrder.getName(), managedFoodOrder);
//            customHibernate.create(chat);
//        }
//        User managedUser = customHibernate.getEntityById(User.class, currentUser.getId());
//        managedFoodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
//        Review message = new Review(messageBody.getText(),
//                (BasicUser) managedUser,
//                managedFoodOrder.getChat());
//        customHibernate.create(message);
//        messageBody.clear();

        if (currentFoodOrder.getChat() == null) {
            Chat chat = new Chat("Chat no " + currentFoodOrder.getName(), currentFoodOrder);
            customHibernate.create(chat);
        }
        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
        Review message = new Review(messageBody.getText(),
                (BasicUser) currentUser,
                foodOrder.getChat());
        customHibernate.create(message);
    }
}
