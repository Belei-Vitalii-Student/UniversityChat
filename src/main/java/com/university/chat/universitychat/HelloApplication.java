package com.university.chat.universitychat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        scene.getStylesheets().add("bootstrap3.css");

        stage.setTitle("University hub");
        stage.setScene(scene);

        stage.setResizable(false);

        stage.show();
    }

    // USERS:
    // Student pass 0
    // Teacher pass 1

    public static void main(String[] args) {
        launch();
    }
}