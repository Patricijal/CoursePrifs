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
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    //<editor-fold desc="Admin Chat Elements">
    @FXML
    public ListView<Chat> allChats;
    @FXML
    public ListView<Review> chatMessages;
    //</editor-fold>

    //<editor-fold desc="Review Elements">
    @FXML
    public ListView<Review> reviewListField;
    @FXML
    public TextField reviewRatingField;
    @FXML
    public TextArea reviewCommentField;
    @FXML
    public ComboBox<Restaurant> reviewRestaurantList;
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
            initializeFilterComponents();
            List<FoodOrder> foodOrders = getFoodOrders();
            orderListField.getItems().addAll(foodOrders);

            // exclude restaurant users from basic user list
            List<BasicUser> basicUsersOnly = customHibernate.getAllRecords(BasicUser.class).stream()
                    .filter(user -> !(user instanceof Restaurant))
                    .collect(Collectors.toList());
//            orderClientList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            orderClientList.getItems().addAll(basicUsersOnly);
            // su listview
//            basicUserList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            basicUserList.getItems().addAll(basicUsersOnly);

            orderRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            orderStatusField.getItems().addAll(OrderStatus.values());
        } else if (cuisineTab.isSelected()) {
            clearAllCuisineFields();
            cuisineRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
            cuisineListField.getItems().addAll(cuisineList);
            cuisineAllergensField.getItems().addAll(Allergens.values());
        } else if (chatTab.isSelected()) {
            allChats.getItems().clear();
            allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
        } else if (reviewTab.isSelected()) {
            clearAllReviewFields();
            reviewListField.getItems().addAll(customHibernate.getAllRecords(Review.class));
            reviewRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
        } else if (altTab.isSelected()) {
            userListField.getItems().clear();
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        }
    }

    private void clearAllReviewFields() {
        reviewListField.getItems().clear();
        reviewRatingField.clear();
        reviewCommentField.clear();
        reviewRestaurantList.getItems().clear();
    }

    private void initializeFilterComponents() {
        if (filterStatus.getItems().isEmpty()) {
            filterStatus.getItems().addAll(OrderStatus.values());
            filterStatus.getItems().add(0, null);
        }
        if (filterClients.getItems().isEmpty()) {
            List<BasicUser> basicUsersOnly = customHibernate.getAllRecords(BasicUser.class).stream()
                    .filter(user -> !(user instanceof Restaurant))
                    .collect(Collectors.toList());
            filterClients.getItems().addAll(basicUsersOnly);
            filterClients.getItems().add(0, null);
        }
        filterFrom.setValue(null);
        filterTo.setValue(null);
    }

    private void clearAllOrderFields() {
        cuisineList.getItems().clear();
        basicUserList.getItems().clear();
        orderClientList.getItems().clear();
        orderRestaurantList.getItems().clear();
        orderNameField.clear();
        orderPriceField.clear();
        orderListField.getItems().clear();
        orderStatusField.getItems().clear();
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
        fillUserList();
    }

    public void loadUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        User selectedUser = userListField.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "User load", "No user selected");
            return;
        }

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, selectedUser, true);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User form");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        fillUserList();
    }

    public void deleteUser() {
        User selectedUser = userListField.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "User delete", "No user selected");
            return;
        }

        customHibernate.delete(User.class, selectedUser.getId());
        fillUserList();
    }

    private void fillUserList() {
        userListField.getItems().clear();
        userListField.getItems().addAll(customHibernate.getAllRecords(User.class));
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
        order.setFood(cuisineList.getSelectionModel().getSelectedItems());
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

        orderStatusField.getItems().stream()
                 .filter(s -> s == selectedOrder.getOrderStatus())
                 .findFirst()
                 .ifPresent(s -> orderStatusField.getSelectionModel().select(s));

        // not sure about multiple selection
//        cuisineList.getItems().stream()
//                .filter(c -> c.getId() == selectedOrder.getFood().getId())
//                .findFirst()
//                .ifPresent(u -> cuisineList.getSelectionModel().select(u));
        disableFoodOrderFields();
    }

    private void disableFoodOrderFields() {
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        boolean shouldDisable = (selectedOrder.getOrderStatus() == OrderStatus.COMPLETED);

        orderNameField.setDisable(shouldDisable);
        orderPriceField.setDisable(shouldDisable);
        orderClientList.setDisable(shouldDisable);
        orderRestaurantList.setDisable(shouldDisable);
        basicUserList.setDisable(shouldDisable);
        cuisineList.setDisable(shouldDisable);
    }

    public void filterOrders() {
        try {
            OrderStatus selectedStatus = filterStatus.getSelectionModel().getSelectedItem();
            BasicUser selectedClient = filterClients.getSelectionModel().getSelectedItem();
            LocalDate startDate = filterFrom.getValue();
            LocalDate endDate = filterTo.getValue();

            // Get current restaurant (if user is a restaurant)
            Restaurant currentRestaurant = null;
            if (currentUser instanceof Restaurant) {
                currentRestaurant = (Restaurant) currentUser;
            }

            List<FoodOrder> filteredOrders = customHibernate.getFilteredRestaurantOrders(
                    selectedStatus, selectedClient, startDate, endDate, currentRestaurant
            );

            // Update the order list
            orderListField.getItems().clear();
            orderListField.getItems().addAll(filteredOrders);

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Filter Error", "Failed to filter orders", e.getMessage());
        }
    }

    public void loadRestaurantMenuForOrder() {
        cuisineList.getItems().clear();
        cuisineList.getItems().addAll(customHibernate.getRestaurantCuisine(orderRestaurantList.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Management Tab Functions">
    public void createNewMenuItem() {
        Cuisine cuisine = new Cuisine(cuisineTitleField.getText(),
                cuisineDescriptionField.getText(),
                Double.parseDouble(cuisinePriceField.getText()),
                isSpicy.isSelected(),
                isVegan.isSelected(),
                cuisineRestaurantList.getSelectionModel().getSelectedItem(),
                cuisineAllergensField.getSelectionModel().getSelectedItem());
        customHibernate.create(cuisine);
        fillMenuList();
    }

    public void updateMenuItem() {
        Cuisine cuisine = cuisineListField.getSelectionModel().getSelectedItem();
        cuisine.setTitle(cuisineTitleField.getText());
        cuisine.setDescription(cuisineDescriptionField.getText());
        try {
            cuisine.setPrice(Double.parseDouble(cuisinePriceField.getText()));
        } catch (NumberFormatException e) {
            cuisine.setPrice(0.0); // default if invalid input
        }
        cuisine.setSpicy(isSpicy.isSelected());
        cuisine.setVegan(isVegan.isSelected());
        cuisine.setRestaurant(cuisineRestaurantList.getSelectionModel().getSelectedItem());
        cuisine.setAllergens(cuisineAllergensField.getSelectionModel().getSelectedItem());
        customHibernate.update(cuisine);
        fillMenuList();
    }

    public void deleteCuisine() {
        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(Cuisine.class, selectedCuisine.getId());
        fillMenuList();
    }

    private void fillMenuList() {
        cuisineListField.getItems().clear();
        cuisineListField.getItems().addAll(customHibernate.getAllRecords(Cuisine.class));
    }

    public void loadRestaurantMenu() {
        cuisineListField.getItems().clear();
        cuisineListField.getItems().addAll(customHibernate.getRestaurantCuisine(cuisineRestaurantList.getSelectionModel().getSelectedItem()));
    }

    public void loadMenuItemInfo() {
        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
        cuisineTitleField.setText(selectedCuisine.getTitle());
        cuisineDescriptionField.setText(selectedCuisine.getDescription());
        cuisinePriceField.setText(selectedCuisine.getPrice().toString());
        isSpicy.setSelected(selectedCuisine.getSpicy());
        isVegan.setSelected(selectedCuisine.getVegan());

        cuisineRestaurantList.getItems().stream()
                .filter(r -> r.getId() == selectedCuisine.getRestaurant().getId())
                .findFirst()
                .ifPresent(r -> cuisineRestaurantList.getSelectionModel().select(r));

        cuisineAllergensField.getItems().stream()
                .filter(a -> a == selectedCuisine.getAllergens())
                .findFirst()
                .ifPresent(a -> cuisineAllergensField.getSelectionModel().select(a));
    }
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    //<editor-fold desc="Admin Chat Functions">
    public void loadChatMessages() {
        chatMessages.getItems().clear();
        chatMessages.getItems().addAll(customHibernate.getChatMessages(allChats.getSelectionModel().getSelectedItem()));
    }

    public void deleteChat() {
        Chat selectedChat = allChats.getSelectionModel().getSelectedItem();
        customHibernate.delete(Chat.class, selectedChat.getId());

        allChats.getItems().clear();
        allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
    }

    public void deleteMessage() {
    }

    public void loadChatForm() throws IOException {
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Chat load", "No order selected");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));
        Parent parent = fxmlLoader.load();

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

    //<editor-fold desc="Review Tab Functions">
    public void addReview() {
        if (isIntegerNumeric(reviewRatingField.getText())) {
            Review review = new Review(Integer.parseInt(reviewRatingField.getText()),
                    reviewCommentField.getText(),
                    (BasicUser) currentUser,
                    reviewRestaurantList.getValue());
            customHibernate.create(review);
            fillReviewList();
        }
    }

    public void updateReview() {
        Review review = reviewListField.getSelectionModel().getSelectedItem();
        review.setRating(Integer.parseInt(reviewRatingField.getText()));
        review.setText(reviewCommentField.getText());
        review.setRestaurant(reviewRestaurantList.getSelectionModel().getSelectedItem());
        customHibernate.update(review);
        fillReviewList();
    }

    public void deleteReview() {
        Review selectedReview = reviewListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(Review.class, selectedReview.getId());
        fillReviewList();
    }

    private void fillReviewList() {
        reviewListField.getItems().clear();
        reviewListField.getItems().addAll(customHibernate.getAllRecords(Review.class));
    }

    private boolean isIntegerNumeric(String text) {
        try {
            Integer.parseInt(text);
            return true;
        }  catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadReviewInfo() {
        Review selectedReview = reviewListField.getSelectionModel().getSelectedItem();
        reviewRatingField.setText(String.valueOf(selectedReview.getRating()));
        reviewCommentField.setText(selectedReview.getText());

        reviewRestaurantList.getItems().stream()
                .filter(r -> r.getId() == selectedReview.getRestaurant().getId())
                .findFirst()
                .ifPresent(r -> reviewRestaurantList.getSelectionModel().select(r));
    }
    //</editor-fold>
}
