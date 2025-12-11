package com.sonoda.login;

import com.sonoda.login.controller.LoginController;
import com.sonoda.login.model.util.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main extends Application {
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

        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("login.fxml"));
        /*HBox root = new HBox();
        VBox containerLeft = new VBox();
        VBox containerRight = new VBox();
        VBox containerLogin = new VBox();


        Label labelTitle = new Label("Login");
        labelTitle.setFont(new Font(30));

        Label labelUser = new Label("Ingrese su usuario");
        labelUser.setFont(new Font(15));

        Label labelPass = new Label("Ingrese su password");
        labelPass.setFont(new Font(15));

        TextField textUser = new TextField();
        textUser.setFont(new Font(18));
        textUser.setPromptText("Ingrese usuario");
        textUser.setPrefWidth(341);
        textUser.setPrefHeight(44);

        PasswordField passField = new PasswordField();
        passField.setFont(new Font(18));
        passField.setPromptText("Ingrese clave");
        passField.setPrefWidth(341);
        passField.setPrefHeight(44);

        Button btnLogin = new Button("Loguear");
        btnLogin.setFont(new Font(20));
        btnLogin.setPrefWidth(370);
        btnLogin.setPrefHeight(44);
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setCursor(Cursor.HAND);

        containerLogin.getChildren().addAll(labelUser,textUser,labelPass,passField,btnLogin);
        containerLogin.setAlignment(Pos.CENTER);

        VBox.setMargin(labelUser, new Insets(10,0,0,0));
        VBox.setMargin(labelPass, new Insets(10,0,0,0));
        VBox.setMargin(btnLogin, new Insets(20,0,0,0));

        containerLeft.getChildren().add(labelTitle);
        containerLeft.getChildren().add(containerLogin);
        containerLeft.setPrefHeight(422);
        containerLeft.setAlignment(Pos.CENTER);
        VBox.setMargin(containerLogin, new Insets(0,30,0,30));

        ImageView imageLogo;

        InputStream inputStream;
        inputStream = getClass().getResourceAsStream("/img/logo.png");
        Image image = new Image(inputStream);

        imageLogo = new ImageView(image);
        containerRight.getChildren().add(imageLogo);

        containerRight.setPrefWidth(422);
        containerRight.setAlignment(Pos.CENTER);
        containerRight.setBackground(new Background(new BackgroundFill(Color.web("30373e"), CornerRadii.EMPTY, Insets.EMPTY)));

        root.getChildren().addAll(containerLeft,containerRight);
        HBox.setHgrow(containerLeft, Priority.ALWAYS);
        HBox.setHgrow(containerRight, Priority.ALWAYS);
        root.setAlignment(Pos.CENTER);*/

        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
