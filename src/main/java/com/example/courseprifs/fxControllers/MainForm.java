package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainForm implements Initializable {
    @FXML
    public Tab userTab;
    @FXML
    public Tab orderTab;
    @FXML
    public Tab cuisineTab;
    @FXML // laikinas
    public Tab altTab;
    @FXML
    public ListView<User> userListField;
    @FXML
    public TabPane tabsPane;
    @FXML
    public Tab chatTab;
    @FXML
    public Tab reviewTab;
    @FXML
    public ListView<Cuisine> cuisineListField;
    @FXML
    public TextField cuisineTitleField;
    @FXML
    public TextArea cuisineDescriptionField;
    @FXML
    public ComboBox<Allergens> cuisineAllergensField;
    @FXML
    public TextField cuisinePriceField;
    @FXML
    public ListView<Review> reviewListField;
    @FXML
    public TextField reviewRatingField;
    @FXML
    public TextArea reviewCommentField;
    @FXML
    public ListView<FoodOrder> orderListField;
    @FXML
    public TextField orderNameField;
    @FXML
    public ComboBox<Cuisine> orderCuisineField;
    @FXML
    public TextField orderPriceField;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;
    private GenericHibernate genericHibernate;

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        setUserFormVisibility();
    }

    private void setUserFormVisibility() {
        if (currentUser instanceof User) {
            // custom
        } else if (currentUser instanceof Restaurant) {
            tabsPane.getTabs().remove(altTab); // wont even generate tab
        }
    }

    public void reloadTableData() {
        if (userTab.isSelected()) {

        } else if (orderTab.isSelected()) {
            orderListField.getItems().clear();
            List<FoodOrder> foodOrders = getFoodOrders();
            orderListField.getItems().addAll(foodOrders);

            loadCuisinesForOrders();
        } else if (cuisineTab.isSelected()) {
            cuisineListField.getItems().clear();
            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
            cuisineListField.getItems().addAll(cuisineList);
        } else if (chatTab.isSelected()) {

        } else if (reviewTab.isSelected()) {

        } else if (altTab.isSelected()) {
            userListField.getItems().clear();
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        }
    }

    private void loadCuisinesForOrders() {
        if (entityManagerFactory != null && customHibernate != null) {
            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
            orderCuisineField.getItems().setAll(cuisineList);
        }
    }

    private List<FoodOrder> getFoodOrders() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantOrders((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(FoodOrder.class);
        }
    }

    //<editor-fold desc="Alternative User Management Tab Functions">
    public void addUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, null, false);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User form");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); // modaliskumas - neleidzia atidaryti pasikartojanciu langu
        stage.showAndWait();
    }

    public void loadUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        User selectedUser = userListField.getSelectionModel().getSelectedItem();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, selectedUser, true);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User form");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void deleteUser() {
        User selectedUser = userListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(User.class, selectedUser.getId());
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Management Tab Functions">
    public void addCuisine() {
        Cuisine cuisine = new Cuisine(cuisineTitleField.getText(),
                cuisineDescriptionField.getText(),
                cuisineAllergensField.getSelectionModel().getSelectedItem(),
                Double.parseDouble(cuisinePriceField.getText()));
        genericHibernate.create(cuisine);
    }

    public void updateCuisine() {
        Cuisine cuisine = cuisineListField.getSelectionModel().getSelectedItem();
        cuisine.setTitle(cuisineTitleField.getText());
        cuisine.setDescription(cuisineDescriptionField.getText());
        cuisine.setAllergens(cuisineAllergensField.getSelectionModel().getSelectedItem());
        try {
            cuisine.setPrice(Double.parseDouble(cuisinePriceField.getText()));
        } catch (NumberFormatException e) {
            cuisine.setPrice(0.0); // default if invalid input
        }
        genericHibernate.update(cuisine);
    }

    public void deleteCuisine() {
        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
        genericHibernate.delete(Cuisine.class, selectedCuisine.getId());
    }
    //</editor-fold>

    //<editor-fold desc="Review Tab Functions">
    public void addReview() {
    }

    public void updateReview() {
    }

    public void deleteReview() {
    }
    //</editor-fold>

    //<editor-fold desc="Order Management Tab Functions">
    public void addOrder() {
        FoodOrder order = new FoodOrder(orderNameField.getText(),
                Double.parseDouble(orderPriceField.getText()));
        genericHibernate.create(order);
    }

    public void updateOrder() {
        FoodOrder order = orderListField.getSelectionModel().getSelectedItem();
        order.setName(orderNameField.getText());
        try {
            order.setPrice(Double.parseDouble(orderPriceField.getText()));
        } catch (NumberFormatException e) {
            order.setPrice(0.0); // default if invalid input
        }
        genericHibernate.update(order);
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        genericHibernate.delete(FoodOrder.class, selectedOrder.getId());
    }
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cuisineAllergensField.getItems().setAll(Allergens.values());
    }
}
