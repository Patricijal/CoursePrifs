package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
    public ListView<User> userListField;
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


    public void createUser(ActionEvent actionEvent) {
        User user = new User(usernameField.getText(), pswField.getText(), nameField.getText(), surnameField.getText(), phoneNumField.getText());
//        System.out.println(user);
        userListField.getItems().add(user);
    }

    public void disableFields() {
        if (userRadio.isSelected()) {
            addressField.setDisable(true);
            addressField.setVisible(false);
        } else if (restaurantRadio.isSelected()) {


        } else if (clientRadio.isSelected()) {

        } else {

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        disableFields();
    }
}
