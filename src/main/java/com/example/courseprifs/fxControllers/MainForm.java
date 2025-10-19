package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.model.FoodOrder;
import com.example.courseprifs.model.Restaurant;
import com.example.courseprifs.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainForm {
    @FXML
    public Tab userTab;
    @FXML
    public Tab managementTab;
    @FXML
    public Tab foodTab;
    @FXML // laikinas
    public Tab altTab;
    @FXML
    public ListView<User> userListField;
    @FXML
    public TabPane tabsPane;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
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

        } else if (managementTab.isSelected()) {
            List<FoodOrder> foodOrders = getFoodOrders();
        } else if (foodTab.isSelected()) {

        } else if (altTab.isSelected()) {
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        }
    }

    private List<FoodOrder> getFoodOrders() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantOrders((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(FoodOrder.class);
        }
    }

    //<editor-fold desc="Alternative Tab Functions">
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
        stage.initModality(Modality.APPLICATION_MODAL); // modaliskumas - neleidzia atidaryti pasikartojanciu langu
        stage.showAndWait();
    }

    public void deleteUser() {
        User selectedUser = userListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(User.class, selectedUser.getId());
    }
    //</editor-fold>
}
