package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import com.example.courseprifs.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.util.List;

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
        loadMessages();
    }

    private void loadMessages() {
        messageList.getItems().clear();
        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
        if (foodOrder.getChat() != null) {
            List<Review> messages = customHibernate.getChatMessages(foodOrder.getChat());
            messageList.getItems().addAll(messages);
        }
    }

    public void sendMessage() {
        if (messageBody.getText().isEmpty()) {
            return;
        }
        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());

        if (foodOrder.getChat() == null) {
            Chat chat = new Chat("Chat no " + foodOrder.getName(), foodOrder);
            customHibernate.create(chat);

            foodOrder.setChat(chat);
            customHibernate.update(foodOrder);
        }
        // Refresh foodOrder to get updated chat
        foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());

        Review message = new Review(messageBody.getText(),
                (BasicUser) currentUser,
                foodOrder.getChat());
        customHibernate.create(message);
        loadMessages();
    }

    public void editMessage() {
        Review selectedMessage = messageList.getSelectionModel().getSelectedItem();
        if (selectedMessage== null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Edit Message", "No message selected");
            return;
        }
        selectedMessage.setText(messageBody.getText());
        customHibernate.update(selectedMessage);
        loadMessages();
    }

    public void deleteMessage() {
        Review selectedMessage = messageList.getSelectionModel().getSelectedItem();
        if (selectedMessage== null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Message", "No message selected");
            return;
        }
        customHibernate.delete(Review.class, selectedMessage.getId());
        loadMessages();
    }

    public void loadMessageBody() {
        Review selectedMessage = messageList.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            messageBody.setText(selectedMessage.getText());
        }
    }
}

//        if (currentFoodOrder.getChat() == null) {
//Chat chat = new Chat("Chat no " + currentFoodOrder.getName(), currentFoodOrder);
//            customHibernate.create(chat);
//        }
//FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
//Review message = new Review(messageBody.getText(),
//        (BasicUser) currentUser,
//        foodOrder.getChat());
//        customHibernate.create(message);


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