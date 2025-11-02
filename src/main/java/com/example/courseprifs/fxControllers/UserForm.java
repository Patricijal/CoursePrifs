package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import com.example.courseprifs.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    public Button updateButton;
    @FXML
    public TextField licenseField;
    @FXML
    public ComboBox<VehicleType> vehicleTypeField;
    @FXML
    public DatePicker bDateField;
    @FXML
    public TextField workHoursField;
    @FXML
    public TextField ratingField;
    public Button saveButton;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User userForUpdate;
    private boolean isForUpdate;

    public void setData(EntityManagerFactory entityManagerFactory, User user, boolean isForUpdate) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.userForUpdate = user;
        this.isForUpdate = isForUpdate;
        fillUserDataForUpdate();
    }

    private void fillUserDataForUpdate() {
        if (userForUpdate != null && isForUpdate) {
            usernameField.setText(userForUpdate.getLogin());
            pswField.setText(userForUpdate.getPassword());
            nameField.setText(userForUpdate.getName());
            surnameField.setText(userForUpdate.getSurname());
            phoneNumField.setText(userForUpdate.getPhoneNumber());
            if (userForUpdate instanceof BasicUser basicUser) {
                addressField.setText(basicUser.getAddress());
            }
            if (userForUpdate instanceof Driver driver) {
                addressField.setText(driver.getAddress());
                licenseField.setText(driver.getLicense());
                bDateField.setValue(driver.getBDate());
                vehicleTypeField.setValue(driver.getVehicleType());
            }
            if (userForUpdate instanceof Restaurant restaurant) {
                addressField.setText(restaurant.getAddress());
                workHoursField.setText(restaurant.getWorkHours());
                ratingField.setText(String.valueOf(restaurant.getRating()));
            }
            if (userForUpdate instanceof Driver) {
                driverRadio.setSelected(true);
            } else if (userForUpdate instanceof Restaurant) {
                restaurantRadio.setSelected(true);
            } else if (userForUpdate instanceof BasicUser) {
                clientRadio.setSelected(true);
            } else {
                userRadio.setSelected(true);
            }
            disableFields();
            updateButton.setVisible(true);
            saveButton.setVisible(false);
        } else {
            updateButton.setVisible(false);
        }
    }

    public void disableFields() {
//        boolean isUser = userRadio.isSelected();
        boolean isClient = clientRadio.isSelected();
        boolean isDriver = driverRadio.isSelected();
        boolean isRestaurant = restaurantRadio.isSelected();

        // Common pane always visible
        commonPane.setVisible(true);
        commonPane.setDisable(false);

        // Basic (client) pane visible for client and driver (since driver extends basic user)
        basicPane.setVisible(isClient || isDriver || isRestaurant);
        basicPane.setDisable(!(isClient || isDriver || isRestaurant));

        // Driver pane only for driver
        driverPane.setVisible(isDriver);
        driverPane.setDisable(!isDriver);

        // Restaurant pane only for restaurant
        restaurantPane.setVisible(isRestaurant);
        restaurantPane.setDisable(!isRestaurant);
    }

    public void createNewUser() {
        if (!validateUserFields()) {
            return;
        }
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
        } else if (driverRadio.isSelected()) {
            Driver driver = new Driver(usernameField.getText(),
                    pswField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText(),
                    licenseField.getText(),
                    bDateField.getValue(),
                    vehicleTypeField.getSelectionModel().getSelectedItem());
            genericHibernate.create(driver);
        } else if (restaurantRadio.isSelected()) {
            Restaurant restaurant = new Restaurant(usernameField.getText(),
                    pswField.getText(),
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText(),
                    workHoursField.getText(),
                    Double.parseDouble(ratingField.getText()));
            genericHibernate.create(restaurant);
        }
        // Laikinas kodas
//        User user = genericHibernate.getEntityById(User.class, 1);
//        genericHibernate.delete(User.class, 1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        vehicleTypeField.getItems().setAll(VehicleType.values());
        disableFields();
    }

    public void updateUser() {
        if (!validateUserFields()) {
            return;
        }
        // Common fields (shared by all subclasses)
        userForUpdate.setLogin(usernameField.getText());
        userForUpdate.setPassword(pswField.getText());
        userForUpdate.setName(nameField.getText());
        userForUpdate.setSurname(surnameField.getText());
        userForUpdate.setPhoneNumber(phoneNumField.getText());

        // Type-specific fields
        if (userForUpdate instanceof BasicUser basicUser) {
            basicUser.setAddress(addressField.getText());
        }

        if (userForUpdate instanceof Driver driver) {
            driver.setAddress(addressField.getText());
            driver.setLicense(licenseField.getText());
            driver.setBDate(bDateField.getValue());
            driver.setVehicleType(vehicleTypeField.getSelectionModel().getSelectedItem());
        }

        if (userForUpdate instanceof Restaurant restaurant) {
            restaurant.setAddress(addressField.getText());
            restaurant.setWorkHours(workHoursField.getText());
            try {
                restaurant.setRating(Double.parseDouble(ratingField.getText()));
            } catch (NumberFormatException e) {
                restaurant.setRating(0.0); // default if invalid input
            }
        }

        // Save the updated object
        genericHibernate.update(userForUpdate);

        // Close the form
        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();
    }



    private boolean validateUserFields() {
        // Common fields validation
        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Username Missing", "Please enter a username.");
            return false;
        }

        if (pswField.getText() == null || pswField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Password Missing", "Please enter a password.");
            return false;
        }

        if (pswField.getText().length() < 4) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Password Too Short", "Password must be at least 4 characters long.");
            return false;
        }

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Name Missing", "Please enter a name.");
            return false;
        }

        if (surnameField.getText() == null || surnameField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Surname Missing", "Please enter a surname.");
            return false;
        }

        if (phoneNumField.getText() == null || phoneNumField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Phone Number Missing", "Please enter a phone number.");
            return false;
        }

//        // Phone number format validation (basic)
//        if (!phoneNumField.getText().matches("\\+?[0-9\\s-()]+")) {
//            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Phone Number", "Please enter a valid phone number.");
//            return false;
//        }

        // User type selection validation
        if (!userRadio.isSelected() && !clientRadio.isSelected() && !driverRadio.isSelected() && !restaurantRadio.isSelected()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "User Type Not Selected", "Please select a user type.");
            return false;
        }

        // Basic User (Client) validation
        if (clientRadio.isSelected() || driverRadio.isSelected() || restaurantRadio.isSelected()) {
            if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Address Missing", "Please enter an address.");
                return false;
            }
        }

        // Driver-specific validation
        if (driverRadio.isSelected()) {
            if (licenseField.getText() == null || licenseField.getText().trim().isEmpty()) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "License Missing", "Please enter a driver's license.");
                return false;
            }

            if (bDateField.getValue() == null) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Birth Date Missing", "Please select a birth date.");
                return false;
            }

            if (vehicleTypeField.getSelectionModel().getSelectedItem() == null) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Vehicle Type Not Selected", "Please select a vehicle type.");
                return false;
            }
        }

        // Restaurant-specific validation
        if (restaurantRadio.isSelected()) {
            if (workHoursField.getText() == null || workHoursField.getText().trim().isEmpty()) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Work Hours Missing", "Please enter work hours.");
                return false;
            }

            if (ratingField.getText() == null || ratingField.getText().trim().isEmpty()) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Rating Missing", "Please enter a rating.");
                return false;
            }

            if (!isNumeric(ratingField.getText())) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Please enter a valid numeric rating.");
                return false;
            }

            // Check if rating is between 0 and 5
            try {
                double rating = Double.parseDouble(ratingField.getText());
                if (rating < 0 || rating > 5) {
                    FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Rating must be between 0 and 5.");
                    return false;
                }
            } catch (NumberFormatException e) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Please enter a valid numeric rating.");
                return false;
            }
        }

        return true; // All validations passed
    }

    private boolean isNumeric(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
