package com.sonoda.login.controller;

import com.sonoda.login.model.UserEntity;
import com.sonoda.login.model.util.ConnectionManager;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.*;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    public void switchAdminPage(ActionEvent event) throws IOException {
        Stage adminStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminController.class.getResource("/com/sonoda/login/admin.fxml"));
        Scene adminScene = fxmlLoader.load();
        adminStage.setScene(adminScene);
    }

    @FXML
    public void deleteUser(ActionEvent event){

    }


}
