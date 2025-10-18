package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.BasicUser;
import com.example.courseprifs.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserForm implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField pswField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneNumField;
    @FXML
    public ToggleGroup userType;
    @FXML
    public RadioButton userRadio;
    @FXML
    public RadioButton restaurantRadio;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton driverRadio;
    @FXML
    public TextField addressField;
    public AnchorPane commonPane;
    public AnchorPane basicPane;
    public AnchorPane driverPane;
    public AnchorPane restaurantPane;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    public void setData(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void disableFields() {
        boolean isUser = userRadio.isSelected();
        boolean isClient = clientRadio.isSelected();
        boolean isDriver = driverRadio.isSelected();
        boolean isRestaurant = restaurantRadio.isSelected();

        // Common pane always visible
        commonPane.setVisible(true);
        commonPane.setDisable(false);

        // Basic (client) pane visible for client and driver (since driver extends basic user)
        basicPane.setVisible(isClient || isDriver);
        basicPane.setDisable(!(isClient || isDriver));

        // Driver pane only for driver
        driverPane.setVisible(isDriver);
        driverPane.setDisable(!isDriver);

        // Restaurant pane only for restaurant
        restaurantPane.setVisible(isRestaurant);
        restaurantPane.setDisable(!isRestaurant);
    }

    public void createNewUser() {
        if (userRadio.isSelected()) {
            User user = new User(usernameField.getText(),
                    pswField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText());
            genericHibernate.create(user);
        } else if (clientRadio.isSelected()) {
            BasicUser basicUser = new BasicUser(usernameField.getText(),
                    pswField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText());
            genericHibernate.create(basicUser);
        }

        // Laikinas kodas
        User user = genericHibernate.getEntityById(User.class, 1);
//        genericHibernate.delete(User.class, 1);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources){
        disableFields();
    }
}
