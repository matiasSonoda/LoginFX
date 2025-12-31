package com.sonoda.login.model.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SceneManager {
    private static SceneManager instance;
    private static Map<String,String> routes;

    private SceneManager(){
        routes = new HashMap<>();
        routes.put("login","/com/sonoda/login/login.fxml");
        routes.put("home","/com/sonoda/login/home.fxml");
        routes.put("admin","/com/sonoda/login/admin.fxml");
        routes.put("register","/com/sonoda/login/register.fxml");
        routes.put("registerAdmin","/com/sonoda/login/registerAdmin.fxml");
    }

    public static SceneManager getInstance(){
        if(instance == null){
            return new SceneManager();
        }
        return instance;
    }

    public void switchTo(String routeName, ActionEvent event) throws IOException{
        String fxmlPath = routes.get(routeName);
        if (fxmlPath == null){
            throw new IOException("Route not found: " + routeName);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage currentStage = getCurrentStage(event);
        currentStage.setScene(scene);
    }

    public void switchToNewStage(String route, Stage parentStage) throws IOException{
        String fxmlPath = routes.get(route);
        if (fxmlPath == null){
            throw new IOException("Route not found: " + route);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        Stage newStage = new Stage();
        newStage.setScene(newScene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(parentStage);
        newStage.show();
    }

    private Stage getCurrentStage(ActionEvent event){
        if (event == null){
            throw new IllegalArgumentException("Event can't be  null");
        }
        //Navega desde el boton(source) -> Node -> Scene- Windows (stage)
        return (Stage)((Node) event.getSource()).getScene().getWindow();
    }

}
