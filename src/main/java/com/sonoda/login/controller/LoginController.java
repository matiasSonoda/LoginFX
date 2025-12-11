package com.sonoda.login.controller;

import com.sonoda.login.main;
import com.sonoda.login.model.util.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Logger;

public class LoginController implements Initializable{

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private Boolean loginUser(ActionEvent event){
        String user = txtUser.getText();
        String password  = txtPassword.getText();
        System.out.println("user: " + user + "password: " + password );
        if(user.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: casilleros vacios");
            alert.setContentText("Error: Casilleros vacios, rellenelos.");
            alert.showAndWait();
            return false;
        }
        String sql = "SELECT idusers FROM users WHERE BINARY username= ? AND password = ?;";
        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);){
                pstmt.setString(1,user);
                pstmt.setString(2, password);

                ResultSet res = pstmt.executeQuery();
                if(res.next()){
                    try{
                        HomeController.switchHome(event);
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

        }catch (SQLException e){
            System.err.println("Error inicio de sesion: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String sql = "SELECT * FROM users;";
        try(Connection conn = ConnectionManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);)
        {
            System.out.println("Resultados de la consulta");
            while (resultSet.next()){
                int id = resultSet.getInt("idusers");
                String name = resultSet.getString("username");
                String pass = resultSet.getString("password");

                System.out.println("id: " + id + "name: " + name + "password: " + pass);

            }

        }catch (SQLException e){
            System.err.println("Error en la consulta: " + e.getMessage());
            e.printStackTrace();
        }


        //Filtro para bloquear espacios en txtUser
        UnaryOperator<TextFormatter.Change> filterUser = change -> {
            String newText = change.getControlNewText();
            if ( newText.contains(" ")){ // Si el nuevo texto contiene espacio
                return null; // Bloquea el cambio
            }
            return change; // Permite el cambio
        };
        txtUser.setTextFormatter(new TextFormatter<>(filterUser));

        //Filtro para bloquear espacios en txtPassword (mismo para PasswordField)
        UnaryOperator<TextFormatter.Change> filterPassword = change -> {
            String nexText = change.getControlNewText();
            if (nexText.contains(" ")){
                return null;
            }
            return change;
        };
        txtPassword.setTextFormatter(new TextFormatter<>(filterPassword));

        txtUser.requestFocus();
}

@FXML
private void switchPage(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(LoginController.class.getResource("/com/sonoda/login/register.fxml"));
    Scene registerScene = new Scene(fxmlLoader.load());
    Stage registerStage = new Stage();
    registerStage.setScene(registerScene);
    registerStage.initModality(Modality.APPLICATION_MODAL);
    registerStage.show();
}

}
