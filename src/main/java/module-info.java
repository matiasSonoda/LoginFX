module com.sonoda.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires javafx.base;

    opens com.sonoda.login.controller to javafx.fxml;
    exports com.sonoda.login;
    opens com.sonoda.login.service to javafx.fxml;
}