package com.sonoda.login.service;

import com.sonoda.login.model.UserNotFoundException;
import com.sonoda.login.model.util.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public void deleteUser(int id){
        if(id == 0){
            LOGGER.log(Level.WARNING, "ID invalid");
            throw new IllegalArgumentException("ID invalid");
        }
        String sql = "DELETE FROM users WHERE idusers = ?";
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);)
        {
            preparedStatement.setInt(1,id);
            int rowAffected = preparedStatement.executeUpdate();
            if ( rowAffected == 0) {
                LOGGER.log(Level.WARNING, "No user was found with this ID.");
                throw new UserNotFoundException("No user was found with this ID.");
            }
            LOGGER.log(Level.INFO,"User " + id + " deleted successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL error deleting user ID: " + id + ": " + e.getMessage());
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
