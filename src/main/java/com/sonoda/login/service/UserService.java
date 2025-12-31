package com.sonoda.login.service;

import com.sonoda.login.model.Rol;
import com.sonoda.login.model.UserEntity;
import com.sonoda.login.model.UserNotFoundException;
import com.sonoda.login.model.util.ConnectionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public Pair<ObservableList<UserEntity>,String> getUsers(){
        String sql = "SELECT * FROM users";
        ObservableList<UserEntity> userList = FXCollections.observableArrayList();
        String status;
        try(Connection conn = ConnectionManager.getConnection();){
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()){
                UserEntity user = new UserEntity(
                        resultSet.getInt("idusers"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        Rol.valueOf(resultSet.getString("rol").toUpperCase())
                );
                userList.add(user);
            }
            if(userList.isEmpty()){
                LOGGER.log(Level.INFO,"Get users: User list is empty");
                status = "No users found";
            }
            else {
                LOGGER.log(Level.INFO,"Get users: success");
                status = "Success";
            }
            return new Pair<>(userList,status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Error to connect Database: " + e.getMessage());
            return new Pair<>(null,"Error to connect Database: " + e.getMessage());
        }
    }

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
