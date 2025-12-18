package com.sonoda.login.controller;

import com.sonoda.login.model.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserController {

    @FXML
    public void deleteUser(int id){
        String sql = "DELETE FROM users WHERE idusers = ?";
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);)
        {
            preparedStatement.setInt(1,id);
            int rowAffected = preparedStatement.executeUpdate();
            if ( rowAffected > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("User deleted");
                alert.setContentText("The user has been deleted");
                alert.showAndWait();
                return;
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("User not found");
                alert.setContentText("The user was not found. Please try again.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
