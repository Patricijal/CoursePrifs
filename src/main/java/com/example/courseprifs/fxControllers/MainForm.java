package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.CustomHibernate;
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
import javafx.scene.input.MouseEvent;
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
    public Tab chatTab;
    @FXML
    public Tab reviewTab;
    @FXML
    public TabPane tabsPane;

    @FXML
    public ListView<User> userListField;

    //<editor-fold desc="Order Management Tab Elements">
    @FXML
    public TextField orderNameField;
    @FXML
    public TextField orderPriceField;
    @FXML
    public ListView<FoodOrder> orderListField;
    @FXML
    public ComboBox<Cuisine> orderCuisineField;
    @FXML
    public ComboBox<BasicUser> orderClientList;
    @FXML
    public ComboBox<Restaurant> orderRestaurantList;
    @FXML
    public ComboBox<OrderStatus> orderStatusField;
    @FXML
    public ListView<BasicUser> basicUserList;
    @FXML
    public ComboBox<OrderStatus> filterStatus;
    @FXML
    public ComboBox<BasicUser> filterClients;
    @FXML
    public DatePicker filterFrom;
    @FXML
    public DatePicker filterTo;
    @FXML
    public ListView<Cuisine> cuisineList;
    //</editor-fold>

    //<editor-fold desc="Cuisine Management Tab Elements">
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
    public ListView<Restaurant> cuisineRestaurantList;
    @FXML
    public CheckBox isSpicy;
    @FXML
    public CheckBox isVegan;
    //</editor-fold>

    @FXML
    public ListView<Review> reviewListField;
    @FXML
    public TextField reviewRatingField;
    @FXML
    public TextArea reviewCommentField;

    //<editor-fold desc="Admin Chat Elements">
    @FXML
    public ListView<Chat> allChats;
    @FXML
    public ListView<Review> chatMessages;
    //</editor-fold>

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
        } else if (currentUser instanceof Driver) {

        }
    }

    //<editor-fold desc="User Tab functionality">
    public void reloadTableData() {
        if (userTab.isSelected()) {
            // table view
        } else if (orderTab.isSelected()) {
            clearAllOrderFields();
            List<FoodOrder> foodOrders = getFoodOrders();
            orderListField.getItems().addAll(foodOrders);
            // double check kodel rodo per daug vartotoju
            orderClientList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            // jei dirbsit su listview
            basicUserList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            orderRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            orderStatusField.getItems().addAll(OrderStatus.values());
//            cuisineList.getItems().addAll(customHibernate.getAllRecords(Cuisine.class));

//            loadCuisinesForOrders();
        } else if (cuisineTab.isSelected()) {
            clearAllCuisineFields();
            cuisineRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));

            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
            cuisineListField.getItems().addAll(cuisineList);
        } else if (chatTab.isSelected()) {
            allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
        } else if (reviewTab.isSelected()) {

        } else if (altTab.isSelected()) {
            userListField.getItems().clear();
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        }
    }

    private void clearAllOrderFields() {
        orderListField.getItems().clear();
        basicUserList.getItems().clear();
        cuisineList.getItems().clear();
        orderClientList.getItems().clear();
        orderRestaurantList.getItems().clear();
        orderNameField.clear();
        orderPriceField.clear();
    }

    private void clearAllCuisineFields() {
        cuisineListField.getItems().clear();
        cuisineTitleField.clear();
        cuisinePriceField.clear();
        cuisineDescriptionField.clear();
        cuisineAllergensField.getItems().clear();
        cuisineRestaurantList.getItems().clear();
        isSpicy.setSelected(false);
        isVegan.setSelected(false);
    }
    //</editor-fold>

//    private void loadCuisinesForOrders() {
//        if (entityManagerFactory != null && customHibernate != null) {
//            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
//            orderCuisineField.getItems().setAll(cuisineList);
//        }
//    }


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

    //<editor-fold desc="Review Tab Functions">
    public void addReview() {
    }

    public void updateReview() {
    }

    public void deleteReview() {
    }
    //</editor-fold>

    //<editor-fold desc="Order Management Tab Functions">
    private List<FoodOrder> getFoodOrders() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantOrders((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(FoodOrder.class);
        }
    }

    private boolean isNumeric(String text) {
        try {
            Double.parseDouble(text);
            return true;
        }  catch (NumberFormatException e) {
            return false;
        }
    }

    public void createOrder() {
        if (isNumeric(orderPriceField.getText())) {
//            FoodOrder order = new FoodOrder(orderNameField.getText(),
//                    Double.parseDouble(orderPriceField.getText()),
//                    orderClientList.getValue(),
//                    orderRestaurantList.getValue());
            FoodOrder order = new FoodOrder(orderNameField.getText(),
                    Double.parseDouble(orderPriceField.getText()),
                    orderClientList.getValue(),
                    cuisineList.getSelectionModel().getSelectedItems(),
                    orderRestaurantList.getValue());
            customHibernate.create(order);

            // Alternatyvus budas:
//            FoodOrder order2 = new FoodOrder(orderNameField.getText(),
//                    Double.parseDouble(orderPriceField.getText()),
//                    basicUserList.getSelectionModel().getSelectedItem(),
//                    orderRestaurantList.getValue());
//            genericHibernate.create(order2);
            fillOrderList();
        }
    }

    public void updateOrder() {
        FoodOrder order = orderListField.getSelectionModel().getSelectedItem();
        order.setName(orderNameField.getText());
        try {
            order.setPrice(Double.parseDouble(orderPriceField.getText()));
        } catch (NumberFormatException e) {
            order.setPrice(0.0); // default if invalid input
        }
        order.setBuyer(orderClientList.getSelectionModel().getSelectedItem());
        order.setRestaurant(orderRestaurantList.getSelectionModel().getSelectedItem());
        order.setOrderStatus(orderStatusField.getValue());
        customHibernate.update(order);
        fillOrderList();
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(FoodOrder.class, selectedOrder.getId());
        fillOrderList();
    }

    private void fillOrderList() {
        orderListField.getItems().clear();
        orderListField.getItems().addAll(customHibernate.getAllRecords(FoodOrder.class));
    }

    public void loadOrderInfo() {
        // not optimal, code duplication
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        orderNameField.setText(selectedOrder.getName());
        orderPriceField.setText(selectedOrder.getPrice().toString());

        orderClientList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> orderClientList.getSelectionModel().select(u));

        basicUserList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> basicUserList.getSelectionModel().select(u));

        orderRestaurantList.getItems().stream()
                .filter(r -> r.getId() == selectedOrder.getRestaurant().getId())
                .findFirst()
                .ifPresent(u -> orderRestaurantList.getSelectionModel().select(u));

//        orderStatusField.getItems().stream()
//                .filter(s -> s.equals(orderStatusField.getValue()))
//                .anyMatch(s -> s.equals("COMPLETED"));
        disableFoodOrderFields();
    }

    private void disableFoodOrderFields() {
        if (orderStatusField.getSelectionModel().getSelectedItem() == OrderStatus.COMPLETED){
            orderNameField.setDisable(true);
            orderPriceField.setDisable(true);
            orderClientList.setDisable(true);
            orderRestaurantList.setDisable(true);
        }
    }

    public void filterOrders() {
    }

    public void loadRestaurantMenuForOrder() {
        cuisineList.getItems().clear();
        cuisineList.getItems().addAll(customHibernate.getRestaurantCuisine(orderRestaurantList.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Management Tab Functions">
    public void createNewMenuItem() {
//        Cuisine cuisine = new Cuisine(cuisineTitleField.getText(),
//                cuisineDescriptionField.getText(),
//                cuisineAllergensField.getSelectionModel().getSelectedItem(),
//                Double.parseDouble(cuisinePriceField.getText()));
        Cuisine cuisine = new Cuisine(cuisineTitleField.getText(),
                cuisineDescriptionField.getText(),
                Double.parseDouble(cuisinePriceField.getText()),
                isSpicy.isSelected(),
                isVegan.isSelected(),
                cuisineRestaurantList.getSelectionModel().getSelectedItem());
        customHibernate.create(cuisine);
    }

    public void updateMenuItem() {
//        Cuisine cuisine = cuisineListField.getSelectionModel().getSelectedItem();
//        cuisine.setTitle(cuisineTitleField.getText());
//        cuisine.setDescription(cuisineDescriptionField.getText());
//        cuisine.setAllergens(cuisineAllergensField.getSelectionModel().getSelectedItem());
//        try {
//            cuisine.setPrice(Double.parseDouble(cuisinePriceField.getText()));
//        } catch (NumberFormatException e) {
//            cuisine.setPrice(0.0); // default if invalid input
//        }
//        customHibernate.update(cuisine);
    }

    public void deleteCuisine() {
//        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
//        customHibernate.delete(Cuisine.class, selectedCuisine.getId());
    }

    public void loadRestaurantMenu() {
        cuisineListField.getItems().addAll(customHibernate.getRestaurantCuisine(cuisineRestaurantList.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cuisineAllergensField.getItems().setAll(Allergens.values());
    }

    //<editor-fold desc="Admin Chat Functions">
    public void loadChatMessages() {
        chatMessages.getItems().addAll(customHibernate.getChatMessages(allChats.getSelectionModel().getSelectedItem()));
    }

    public void deleteChat() {
    }

    public void deleteMessage() {
    }

    public void loadChatForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));
        Parent parent = fxmlLoader.load();

        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Chat load", "No order selected");
            return;
        }

        ChatForm chatForm = fxmlLoader.getController();
        chatForm.setData(entityManagerFactory, currentUser, selectedOrder);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Chat for order: " + selectedOrder.getName());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    //</editor-fold>
}
