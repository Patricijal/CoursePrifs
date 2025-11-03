package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import com.example.courseprifs.utils.FxUtils;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
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
//    @FXML
//    public ListView<BasicUser> basicUserList;
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
    @FXML
    public ComboBox<Allergens> filterAllergens;
    @FXML
    public CheckBox filterVegan;
    @FXML
    public CheckBox filterSpicy;
    //</editor-fold>

    //<editor-fold desc="Admin Chat Elements">
    @FXML
    public ListView<Chat> allChats;
    @FXML
    public ListView<Review> chatMessages;
    @FXML
    public TextArea messageBodyField;
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
    @FXML
    public Button reviewAddButton;
    @FXML
    public Button reviewUpdateButton;
    //</editor-fold>

    //<editor-fold desc="User Management TableView Elements">
    @FXML
    public TableView<UserTableParameters> userTable;
    @FXML
    public TableColumn<UserTableParameters, Integer> idCol;
    @FXML
    public TableColumn<UserTableParameters, String> userTypeCol;
    @FXML
    public TableColumn<UserTableParameters, String> loginCol;
    @FXML
    public TableColumn<UserTableParameters, String> passCol;
    @FXML
    public TableColumn<UserTableParameters, String> nameCol;
    @FXML
    public TableColumn<UserTableParameters, String> surnameCol;
    @FXML
    public TableColumn<UserTableParameters, String> addrCol;
    @FXML
    public TableColumn<UserTableParameters, Void> dummyCol;

    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();
    //</editor-fold>

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;
    private GenericHibernate genericHibernate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cuisineList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        userTable.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPassword(event.getNewValue());
            customHibernate.update(user);
        });
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setName(event.getNewValue());
            customHibernate.update(user);
        });
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setSurname(event.getNewValue());
            customHibernate.update(user);
        });
        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        setUserFormVisibility();
    }

    private void setUserFormVisibility() {
        tabsPane.getTabs().clear();
        if (currentUser instanceof Restaurant) {
            tabsPane.getTabs().addAll(orderTab, cuisineTab, reviewTab);
            reviewRestaurantList.setDisable(true);
            reviewRatingField.setDisable(true);
            reviewCommentField.setDisable(true);
            reviewAddButton.setDisable(true);
            reviewUpdateButton.setDisable(true);
            // add logic for restaurant
//            orderRestaurantList.setDisable(true);
//            cuisineRestaurantList.setVisible(false);
        } else if (currentUser instanceof Driver) {
            tabsPane.getTabs().addAll(orderTab);
        } else if (currentUser instanceof BasicUser) {
            tabsPane.getTabs().addAll(reviewTab);
        } else {
            tabsPane.getTabs().addAll(userTab, altTab, orderTab, cuisineTab, chatTab, reviewTab);
        }
        // Select first tab if available
        if (!tabsPane.getTabs().isEmpty()) {
            tabsPane.getSelectionModel().select(0);
        }
    }

//    private void setUserFormVisibility() {
//        if (currentUser instanceof User) {
//            // custom
//        } else if (currentUser instanceof Restaurant) {
//            tabsPane.getTabs().remove(altTab); // wont even generate tab
//        } else if (currentUser instanceof Driver) {
//
//        }
//    }

    //<editor-fold desc="User Tab functionality">
    public void reloadTableData() {
        if (userTab.isSelected()) {
            userTable.getItems().clear();
            List<User> users = customHibernate.getAllRecords(User.class);
            for (User u:users) {
                UserTableParameters userTableParameters = new UserTableParameters();
                userTableParameters.setId(u.getId());
                userTableParameters.setUserType(u.getClass().getSimpleName());
                userTableParameters.setLogin(u.getLogin());
                userTableParameters.setPassword(u.getPassword());
                userTableParameters.setName(u.getName());
                userTableParameters.setSurname(u.getSurname());
                //baigti bendrus laukus
                if (u instanceof BasicUser) {
                    userTableParameters.setAddress(((BasicUser) u).getAddress());
                }
                if (u instanceof Restaurant) {

                }
                if (u instanceof Driver) {

                }
                data.add(userTableParameters);
            }
            userTable.getItems().addAll(data);
            data.clear();
        } else if (orderTab.isSelected()) {
            clearAllOrderFields();
            initializeOrderFilterComponents();
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
//            basicUserList.getItems().addAll(basicUsersOnly);

            orderRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            orderStatusField.getItems().addAll(OrderStatus.values());
        } else if (cuisineTab.isSelected()) {
            clearAllCuisineFields();
            initializeCuisineFilterComponents();
            cuisineRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
//            List<Cuisine> cuisineList = customHibernate.getAllRecords(Cuisine.class);
            List<Cuisine> cuisineList = getCuisine();
            cuisineListField.getItems().addAll(cuisineList);
            cuisineAllergensField.getItems().addAll(Allergens.values());
        } else if (chatTab.isSelected()) {
            allChats.getItems().clear();
            allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
        } else if (reviewTab.isSelected()) {
            clearAllReviewFields();
            // Load only reviews that are not associated with chats
//            List<Review> allReviews = customHibernate.getAllRecords(Review.class);
            List<Review> allReviews = getReviews();
            List<Review> filteredReviews = allReviews.stream()
                    .filter(review -> review.getChat() == null) // Assuming Review has a 'chat' field
                    .collect(Collectors.toList());
            reviewListField.getItems().addAll(filteredReviews);
//            reviewListField.getItems().addAll(customHibernate.getAllRecords(Review.class));
            reviewRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
        } else if (altTab.isSelected()) {
            userListField.getItems().clear();
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        }
    }

    private void initializeCuisineFilterComponents() {
        if (filterAllergens.getItems().isEmpty()) {
            filterAllergens.getItems().addAll(Allergens.values());
            filterAllergens.getItems().add(0, null);
        }
        filterVegan.setSelected(false);
        filterSpicy.setSelected(false);
        filterAllergens.getSelectionModel().clearSelection();
    }

    private void clearAllReviewFields() {
        reviewListField.getItems().clear();
        reviewRatingField.clear();
        reviewCommentField.clear();
        reviewRestaurantList.getItems().clear();
    }

    private void initializeOrderFilterComponents() {
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
//        basicUserList.getItems().clear();
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
        if (!validateOrderFields()) {
            return; // Stop execution if validation fails
        }
        ObservableList<Cuisine> selectedCuisines = cuisineList.getSelectionModel().getSelectedItems();
        List<Cuisine> cuisineList = new ArrayList<>(selectedCuisines);
        if (isNumeric(orderPriceField.getText())) {
            FoodOrder order = new FoodOrder(orderNameField.getText(),
                    Double.parseDouble(orderPriceField.getText()),
                    orderClientList.getValue(),
//                    cuisineList.getSelectionModel().getSelectedItems(),
                    cuisineList,
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

    private boolean validateOrderFields() {
        if (orderNameField.getText() == null || orderNameField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Order Name Missing", "Please enter an order name.");
            return false;
        }

        if (orderPriceField.getText() == null || orderPriceField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Price Missing", "Please enter an order price.");
            return false;
        }

        if (!isNumeric(orderPriceField.getText())) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid numeric price.");
            return false;
        }

        // Check if price is positive
        try {
            double price = Double.parseDouble(orderPriceField.getText());
            if (price <= 0) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Price must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid numeric price.");
            return false;
        }

        if (orderClientList.getSelectionModel().getSelectedItem() == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Client Not Selected", "Please select a client for the order.");
            return false;
        }

        if (orderRestaurantList.getSelectionModel().getSelectedItem() == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Restaurant Not Selected", "Please select a restaurant for the order.");
            return false;
        }

        if (cuisineList.getSelectionModel().getSelectedItems().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "No Cuisine Selected", "Please select at least one cuisine item for the order.");
            return false;
        }

        return true; // All validations passed
    }

    public void updateOrder() {
        if (!validateOrderFields()) {
            return; // Stop execution if validation fails
        }
        FoodOrder order = orderListField.getSelectionModel().getSelectedItem();
        if (order == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Update Order", "No order selected");
            return;
        }
        ObservableList<Cuisine> selectedCuisines = cuisineList.getSelectionModel().getSelectedItems();
        List<Cuisine> cuisineList = new ArrayList<>(selectedCuisines);
        order.setName(orderNameField.getText());
        try {
            order.setPrice(Double.parseDouble(orderPriceField.getText()));
        } catch (NumberFormatException e) {
            order.setPrice(0.0); // default if invalid input
        }
        order.setBuyer(orderClientList.getSelectionModel().getSelectedItem());
//        order.setFood(cuisineList.getSelectionModel().getSelectedItems());
        order.setFood(cuisineList);
        order.setRestaurant(orderRestaurantList.getSelectionModel().getSelectedItem());
        order.setOrderStatus(orderStatusField.getValue());
        customHibernate.update(order);
        fillOrderList();
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Order", "No order selected");
            return;
        }
        selectedOrder = customHibernate.getEntityById(FoodOrder.class, selectedOrder.getId());
        Chat chat = selectedOrder.getChat();
        if (chat != null) {
            chat.setOrder(null);
            selectedOrder.setChat(null);
            customHibernate.update(chat);
            customHibernate.update(selectedOrder);
            customHibernate.delete(Chat.class, chat.getId());
        }
        customHibernate.delete(FoodOrder.class, selectedOrder.getId());
        fillOrderList();
    }

    private void fillOrderList() {
        orderListField.getItems().clear();
//        orderListField.getItems().addAll(customHibernate.getAllRecords(FoodOrder.class));

        if (currentUser instanceof Restaurant) {
            // Restaurant user - only show orders from their own restaurant
            List<FoodOrder> restaurantOrders = customHibernate.getRestaurantOrders((Restaurant) currentUser);
            orderListField.getItems().addAll(restaurantOrders);
            // Auto-select their restaurant in the combo box
            orderRestaurantList.getItems().clear();
            orderRestaurantList.getItems().add((Restaurant) currentUser);
            orderRestaurantList.getSelectionModel().select((Restaurant) currentUser);
        } else {
            // Other users - show all orders
            orderListField.getItems().addAll(customHibernate.getAllRecords(FoodOrder.class));
            // Populate restaurant list for selection
            orderRestaurantList.getItems().clear();
            orderRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
        }
    }

    public void loadOrderInfo() {
        // not optimal, code duplication
        FoodOrder selectedOrder = orderListField.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) return;

        orderNameField.setText(selectedOrder.getName());
        orderPriceField.setText(selectedOrder.getPrice().toString());

        orderClientList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> orderClientList.getSelectionModel().select(u));

//        basicUserList.getItems().stream()
//                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
//                .findFirst()
//                .ifPresent(u -> basicUserList.getSelectionModel().select(u));

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
//        basicUserList.setDisable(shouldDisable);
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
        if (orderRestaurantList.getSelectionModel().getSelectedItem() == null) return;
        cuisineList.getItems().clear();
        cuisineList.getItems().addAll(customHibernate.getRestaurantCuisine(orderRestaurantList.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Management Tab Functions">
    private List<Cuisine> getCuisine() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantCuisine((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(Cuisine.class);
        }
    }

    public void createNewMenuItem() {
        if (!validateCuisineFields()) {
            return;
        }
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
        if (!validateCuisineFields()) {
            return;
        }
        Cuisine cuisine = cuisineListField.getSelectionModel().getSelectedItem();
        if (cuisine == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Update Cuisine", "No cuisine selected");
            return;
        }
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

    private boolean validateCuisineFields() {
        if (cuisineTitleField.getText() == null || cuisineTitleField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Title Missing", "Please enter a cuisine title.");
            return false;
        }
        if (cuisinePriceField.getText() == null || cuisinePriceField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Price Missing", "Please enter a cuisine price.");
            return false;
        }
        if (!isNumeric(cuisinePriceField.getText())) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid numeric price.");
            return false;
        }
        // Check if price is positive
        try {
            double price = Double.parseDouble(cuisinePriceField.getText());
            if (price <= 0) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Price must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Price", "Please enter a valid numeric price.");
            return false;
        }
        if (cuisineDescriptionField.getText() == null || cuisineDescriptionField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Description Missing", "Please enter a cuisine description.");
            return false;
        }
        if (cuisineRestaurantList.getSelectionModel().getSelectedItem() == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Restaurant Not Selected", "Please select a restaurant for the cuisine item.");
            return false;
        }
        return true; // All validations passed
    }

    public void deleteCuisine() {
        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
        if (selectedCuisine == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Cuisine", "No cuisine selected");
            return;
        }
        customHibernate.delete(Cuisine.class, selectedCuisine.getId());
        fillMenuList();
    }

    private void fillMenuList() {
        cuisineListField.getItems().clear();
//        cuisineListField.getItems().addAll(customHibernate.getAllRecords(Cuisine.class));
        if (currentUser instanceof Restaurant) {
            // Restaurant user - only show menu items from their own restaurant
            List<Cuisine> restaurantCuisine = customHibernate.getRestaurantCuisine((Restaurant) currentUser);
            cuisineListField.getItems().addAll(restaurantCuisine);

            // Auto-select their restaurant in the combo box
            cuisineRestaurantList.getItems().clear();
            cuisineRestaurantList.getItems().add((Restaurant) currentUser);
            cuisineRestaurantList.getSelectionModel().select((Restaurant) currentUser);
        } else {
            // Other users - show all cuisine items
            cuisineListField.getItems().addAll(customHibernate.getAllRecords(Cuisine.class));

            // Populate restaurant list for selection
            cuisineRestaurantList.getItems().clear();
            cuisineRestaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
        }
    }

    public void loadRestaurantMenu() {
        cuisineListField.getItems().clear();
        cuisineListField.getItems().addAll(customHibernate.getRestaurantCuisine(cuisineRestaurantList.getSelectionModel().getSelectedItem()));
    }

    public void loadMenuItemInfo() {
        Cuisine selectedCuisine = cuisineListField.getSelectionModel().getSelectedItem();
        if (selectedCuisine == null) return;
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

    public void filterCuisine () {
        try {
            Allergens selectedAllergen = filterAllergens.getSelectionModel().getSelectedItem();
            boolean isVeganSelected = filterVegan.isSelected();
            boolean isSpicySelected = filterSpicy.isSelected();

            Restaurant currentRestaurant = null;
            if (currentUser instanceof Restaurant) {
                currentRestaurant = (Restaurant) currentUser;
            }

            List<Cuisine> filteredCuisine = customHibernate.getFilteredRestaurantCuisine(
                    selectedAllergen, isVeganSelected, isSpicySelected, currentRestaurant
            );

            // Update the cuisine list
            cuisineListField.getItems().clear();
            cuisineListField.getItems().addAll(filteredCuisine);

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Filter Error", "Failed to filter cuisine", e.getMessage());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Admin Chat Functions">
    public void loadChatMessages() {
        Chat selectedChat = allChats.getSelectionModel().getSelectedItem();
        chatMessages.getItems().clear();
        if (selectedChat == null) return;
        chatMessages.getItems().addAll(customHibernate.getChatMessages(selectedChat));
    }

    public void deleteChat() {
        Chat selectedChat = allChats.getSelectionModel().getSelectedItem();
        if (selectedChat == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Chat", "No chat selected");
            return;
        }
        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, selectedChat.getOrder().getId());
        if (foodOrder != null) {
            foodOrder.setChat(null);
            customHibernate.update(foodOrder);
        }
        customHibernate.delete(Chat.class, selectedChat.getId());
        allChats.getItems().clear();
        chatMessages.getItems().clear();
        allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
    }

    public void deleteMessage() {
        Review selectedMessage = chatMessages.getSelectionModel().getSelectedItem();
        if (selectedMessage == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Message", "No message selected");
            return;
        }
        customHibernate.delete(Review.class, selectedMessage.getId());
        loadChatMessages();
    }

    public void updateMessage() {
        Review selectedMessage = chatMessages.getSelectionModel().getSelectedItem();
        if (selectedMessage== null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Update Message", "No message selected");
            return;
        }
        selectedMessage.setText(messageBodyField.getText());
        customHibernate.update(selectedMessage);
        loadChatMessages();
    }


    public void loadMessageBody() {
        Review selectedMessage = chatMessages.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            messageBodyField.setText(selectedMessage.getText());
        }
    }

    // working on
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
    private List<Review> getReviews() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getFilteredReviews((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(Review.class);
        }
    }

    public void addReview() {
        if (!validateReviewFields()) {
            return;
        }
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
        if (!validateReviewFields()) {
            return;
        }
        Review review = reviewListField.getSelectionModel().getSelectedItem();
        if (review == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Update Review", "No review selected");
            return;
        }
        review.setRating(Integer.parseInt(reviewRatingField.getText()));
        review.setText(reviewCommentField.getText());
        review.setRestaurant(reviewRestaurantList.getSelectionModel().getSelectedItem());
        customHibernate.update(review);
        fillReviewList();
    }

    private boolean validateReviewFields() {
        if (reviewRatingField.getText() == null || reviewRatingField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Rating Missing", "Please enter a rating.");
            return false;
        }
        if (!isIntegerNumeric(reviewRatingField.getText())) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Please enter a valid numeric rating (1-5).");
            return false;
        }
        // Check if rating is between 1 and 5
        try {
            int rating = Integer.parseInt(reviewRatingField.getText());
            if (rating < 1 || rating > 5) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Rating must be between 1 and 5.");
                return false;
            }
        } catch (NumberFormatException e) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Invalid Rating", "Please enter a valid numeric rating.");
            return false;
        }
        if (reviewCommentField.getText() == null || reviewCommentField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Comment Missing", "Please enter a review comment.");
            return false;
        }
        if (reviewRestaurantList.getSelectionModel().getSelectedItem() == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Validation Error", "Restaurant Not Selected", "Please select a restaurant for the review.");
            return false;
        }
        return true; // All validations passed
    }

    public void deleteReview() {
        Review selectedReview = reviewListField.getSelectionModel().getSelectedItem();
        if (selectedReview == null) {
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Oh no", "Delete Review", "No review selected");
            return;
        }
        customHibernate.delete(Review.class, selectedReview.getId());
        fillReviewList();
    }

    private void fillReviewList() {
        reviewListField.getItems().clear();
        List<Review> allReviews = customHibernate.getAllRecords(Review.class);
        List<Review> filteredReviews = allReviews.stream()
                .filter(review -> review.getChat() == null) // Assuming Review has a 'chat' field
                .collect(Collectors.toList());
        reviewListField.getItems().addAll(filteredReviews);
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
        if (selectedReview == null) return;
        reviewRatingField.setText(String.valueOf(selectedReview.getRating()));
        reviewCommentField.setText(selectedReview.getText());

        reviewRestaurantList.getItems().stream()
                .filter(r -> r.getId() == selectedReview.getRestaurant().getId())
                .findFirst()
                .ifPresent(r -> reviewRestaurantList.getSelectionModel().select(r));
    }

    public void filterReviews() {
        if (reviewRestaurantList.getValue() == null && reviewListField.getItems().isEmpty()) {
            return;
        }
        try {
            Restaurant selectedRestaurant = reviewRestaurantList.getSelectionModel().getSelectedItem();

            List<Review> filteredReviews = customHibernate.getFilteredReviews(selectedRestaurant);

            // Update the review list
            reviewListField.getItems().clear();
            reviewListField.getItems().addAll(filteredReviews);

        } catch (Exception e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Filter Error", "Failed to filter reviews", e.getMessage());
        }
    }
    //</editor-fold>
}
