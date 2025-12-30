package com.sonoda.login.controller;

import com.sonoda.login.model.UserEntity;
import com.sonoda.login.model.UserNotFoundException;
import com.sonoda.login.service.UserService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AdminController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @FXML
    public TableColumn<UserEntity,Integer> idTableColumn;
    @FXML
    public TableColumn<UserEntity,String> usernameTableColumn;
    @FXML
    public TableColumn<UserEntity,String> passwordTableColumn;
    @FXML
    public TableColumn<UserEntity,String> emailTableColumn;
    @FXML
    public TableView<UserEntity> tableView;

    UserService userService = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        idTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject());
        usernameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        emailTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        loadUsers();
    }

    @FXML
    public void getUsers(ActionEvent event ){
        loadUsers();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update table");
        alert.setContentText("updated table successfully");
        alert.showAndWait();
    }

    private void loadUsers(){
        Pair<ObservableList<UserEntity>, String> result = userService.getUsers();
        if ("Error to connect Database: ...".equals(result.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database");
            alert.setContentText(result.getValue());
            alert.showAndWait();
            return;
        }
        ObservableList<UserEntity> userList = result.getKey();
        tableView.setItems(userList);
        if("No users found".equals(result.getValue())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Database");
            alert.setContentText("User list is empty");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteUser(ActionEvent event){
        Optional<UserEntity> user = Optional.ofNullable(tableView.getSelectionModel().getSelectedItem());
        if (user.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No row selected");
            alert.setContentText("Select a row, please");
            alert.showAndWait();
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation to delete");
        confirmation.setContentText("Are you sure you want to delete the user " + user.get().getUsername() + " with ID " + user.get().getIdUser());
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            try{
            userService.deleteUser(user.get().getIdUser());
            tableView.getItems().remove(user.get());
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("successfully deleted");
            success.setContentText("Deleted user");
            success.showAndWait();}
            catch (UserNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("User not found");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
            catch (RuntimeException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error deleting user");
                alert.setContentText("An error occurred: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
