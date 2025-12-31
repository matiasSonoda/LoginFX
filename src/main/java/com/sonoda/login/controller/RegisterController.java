package com.sonoda.login.controller;

import com.sonoda.login.model.Rol;
import com.sonoda.login.model.UserEntity;
import com.sonoda.login.model.util.ConnectionManager;
import com.sonoda.login.model.util.EmailValidator;
import com.sonoda.login.model.util.PasswordValidator;
import com.sonoda.login.model.util.UserValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
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
    public ChoiceBox<String> rolChoiceBox;
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
        ObservableList<String> rolList = FXCollections.observableArrayList();
        rolList.add(Rol.USER.toString().toUpperCase());
        rolList.add(Rol.ADMINISTRATOR.toString().toUpperCase());
        rolChoiceBox.setItems(rolList);
        rolChoiceBox.setValue(Rol.USER.toString());
    }

    @FXML
    public void register(ActionEvent event){

        String username = userField.getText();
        String email = emailField.getText();
        String validateEmail = validateEmailField.getText();
        String password = passwordField.getText();
        String validatePassword = validatePasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || validateEmail.isEmpty() || password.isEmpty()){
            Alert empty = new Alert(Alert.AlertType.WARNING);
            empty.setTitle("Empty text fields");
            empty.setContentText("Fill in the missing fields");
            empty.showAndWait();
            return;
        }

        if(!UserValidator.isUserValid(username)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Existing user");
            alert.setContentText("The user already exists.");
            alert.showAndWait();
            return;
        }

        if(!EmailValidator.isValidEmail(email)){
            Alert invalidEmail = new Alert(Alert.AlertType.ERROR);
            invalidEmail.setTitle("Invalid email");
            invalidEmail.setContentText("Enter a valid email address (e.g., user@domain.com)");
            invalidEmail.showAndWait();
            return;
        }

        if(!email.equals(validateEmail)){
            Alert emailsNotEquals = new Alert(Alert.AlertType.WARNING);
            emailsNotEquals.setTitle("Emails: They are not the same");
            emailsNotEquals.setContentText("The emails must be the same.");
            emailsNotEquals.showAndWait();
            return;
        }


        List<String> passwordErrors = PasswordValidator.isPasswordValid(password);
        if (!passwordErrors.isEmpty()){
            String errorsMsg = String.join("\n",passwordErrors);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid password");
            alert.setContentText("Please correct the following:\n" + errorsMsg);
            alert.showAndWait();
            return;
        }

        if(!password.equals(validatePassword)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Different passwords");
            alert.setContentText("Passwords must be the same");
            alert.showAndWait();
            return;
        }

        /*if(!PasswordValidator.isPasswordValid(password)){
            Alert invalidPassword = new Alert((Alert.AlertType.ERROR));
            invalidPassword.setTitle("Password invalido");
            invalidPassword.setContentText("Ingresa un password valido: con al menos una minuscula, una mayuscula, un numero, un digito especial");
        }*/

        String sql = "INSERT INTO `login_schema`.`users`(`username`,`password`,`email`,`rol`) VALUES( ?, ?, ?,?);";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if(Rol.ADMINISTRATOR.toString().equals(rolChoiceBox.getValue().toUpperCase())){
            user.setRol(Rol.ADMINISTRATOR);
        }else {
            user.setRol(Rol.USER);
        }

        try(Connection conn = ConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ){
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            pstmt.setString(3,user.getEmail());
            pstmt.setString(4,user.getRol().toString().toUpperCase());
            int rowsAffected = pstmt.executeUpdate();
            if ( rowsAffected > 0){
                System.out.println("Successful Registration");
                Alert succes = new Alert(Alert.AlertType.INFORMATION);
                succes.setTitle("Successful Registration");
                succes.setContentText("User created. Now log in.");
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
                error.setTitle("Registration error");
                error.setContentText("The user could not be created. Please try again.");
                error.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
