package com.sonoda.login;

import com.sonoda.login.model.util.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = ConnectionManager.getConnection();
            if(connection != null){

                String sql = "SELECT * FROM users";
                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();

                while(resultSet.next()){
                    int id = resultSet.getInt("idusers");
                    String name = resultSet.getString("username");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            }
            else{
                System.out.println("Failed to get connection.");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
