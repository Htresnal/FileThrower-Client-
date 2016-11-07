package com.SiegeNetworks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public MainApp() {}

    public void start(Stage primaryStage) throws Exception {
        Parent root = (Parent)FXMLLoader.load(this.getClass().getResource("/FTCJavaFX.fxml"));
        primaryStage.setTitle("File Thrower(Client)");
        primaryStage.setScene(new Scene(root, 355.0D, 245.0D));
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
