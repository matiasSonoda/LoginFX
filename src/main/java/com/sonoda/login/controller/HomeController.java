package com.sonoda.login.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HomeController {

    @FXML
    static public void switchHome(ActionEvent event) throws IOException {
        Stage homeStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxml = new FXMLLoader(HomeController.class.getResource("/com/sonoda/login/home.fxml"));
        Scene homeScene = new Scene(fxml.load());
        homeStage.setScene(homeScene);
    }

}
