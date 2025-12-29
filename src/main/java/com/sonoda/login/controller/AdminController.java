package com.sonoda.login.controller;

import com.sonoda.login.model.UserEntity;
import com.sonoda.login.model.UserNotFoundException;
import com.sonoda.login.model.util.ConnectionManager;
import com.sonoda.login.service.UserService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

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
        String sql = "SELECT * FROM users";
        ObservableList<UserEntity> userList = FXCollections.observableArrayList();
        try(Connection conn = ConnectionManager.getConnection();){
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()){
                UserEntity user = new UserEntity(
                        resultSet.getInt("idusers"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                );
                userList.add(user);
            }
            idTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUser()).asObject());
            usernameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            passwordTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
            emailTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            tableView.setItems(userList);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error to connect Database");
            alert.setContentText("Error to connect Database:" + e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
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
