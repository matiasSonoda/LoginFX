package com.sonoda.login.controller;

import com.sonoda.login.model.util.ConnectionManager;
import com.sonoda.login.model.util.EmailValidator;
import com.sonoda.login.model.util.PasswordValidator;
import com.sonoda.login.model.util.UserValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private TextField userField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField validateEmailField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField validatePasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void register(ActionEvent event){


        String user = userField.getText();
        String email = emailField.getText();
        String validateEmail = validateEmailField.getText();
        String password = passwordField.getText();
        String validatePassword = validatePasswordField.getText();

        if (user.isEmpty() || email.isEmpty() || validateEmail.isEmpty() || password.isEmpty()){
            Alert empty = new Alert(Alert.AlertType.WARNING);
            empty.setTitle("Campos de texto vacios");
            empty.setContentText("Complete los campos faltantes");
            empty.showAndWait();
            return;
        }

        if(!UserValidator.isUserValid(user)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usiario: ya existente");
            alert.setContentText("El usuario ya existe");
            alert.showAndWait();
            return;
        }

        if(!email.equals(validateEmail)){
            Alert emailsNotEquals = new Alert(Alert.AlertType.WARNING);
            emailsNotEquals.setTitle("Emails: No son iguales");
            emailsNotEquals.setContentText("Los email deben ser iguales");
            emailsNotEquals.showAndWait();
            return;
        }

        if(!EmailValidator.isValidEmail(email)){
            Alert invalidEmail = new Alert(Alert.AlertType.ERROR);
            invalidEmail.setTitle("Email invalido");
            invalidEmail.setContentText("Ingresa un email valido (e.g., usuario@dominio.com)");
            invalidEmail.showAndWait();
            return;
        }

        List<String> passwordErrors = PasswordValidator.isPasswordValid(password);
        if (!passwordErrors.isEmpty()){
            String errorsMsg = String.join("\n",passwordErrors);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contraseña invalida");
            alert.setContentText("Corrige lo siguiente:\n" + errorsMsg);
            alert.showAndWait();
            return;
        }

        if(!password.equals(validatePassword)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contraseñas distintas");
            alert.setContentText("Las contraseñas deben ser iguales");
            alert.showAndWait();
            return;
        }

        /*if(!PasswordValidator.isPasswordValid(password)){
            Alert invalidPassword = new Alert((Alert.AlertType.ERROR));
            invalidPassword.setTitle("Password invalido");
            invalidPassword.setContentText("Ingresa un password valido: con al menos una minuscula, una mayuscula, un numero, un digito especial");
        }*/

        String sql = "INSERT INTO `login_schema`.`users`(`username`,`password`) VALUES( ?, ?);";

        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
            pstmt.setString(1,user);
            pstmt.setString(2,password);
            int rowsAffected = pstmt.executeUpdate();
            if ( rowsAffected > 0){
                System.out.println("Registro exitoso");
                Alert succes = new Alert(Alert.AlertType.INFORMATION);
                succes.setTitle("Registro Exitoso");
                succes.setContentText("Usuario creado. Ahora inicia sesion.");
                succes.showAndWait();
                userField.clear();
                emailField.clear();
                validateEmailField.clear();
                passwordField.clear();
                Stage registerStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                registerStage.close();
            }else {
                System.out.println("No se inserto ningun registro");
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error en registro");
                error.setContentText("No se pudo crear el usuario. Intenta de nuevo");
                error.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


}
