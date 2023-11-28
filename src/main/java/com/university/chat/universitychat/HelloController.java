package com.university.chat.universitychat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    Button log_in_btn;
    @FXML
    Button news_btn;
    @FXML
    Button schedule_btn;
    @FXML
    Button meeting_btn;
    @FXML
    Button new_meet_btn;
    @FXML
    GridPane news_view;
    @FXML
    TableView schedule_view;
    @FXML
    VBox meeting_view;
    @FXML
    Pane new_meet_form;

    @FXML
    MenuButton menu_options;
    private Stage stage;
    @FXML
    protected void login(ActionEvent event) throws IOException {
        Parent teacherView = FXMLLoader.load(getClass().getResource("teacher-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(teacherView);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException {
        Parent teacherView = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) menu_options.getScene().getWindow();
        Scene scene = new Scene(teacherView);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void selectView(ActionEvent event) {
        Node node = (Node) event.getSource() ;
        String data = (String) node.getUserData();

        switch (data) {
            case "news":
                selectNewsView();
                break;
            case "schedule":
                selectScheduleView();
                break;
            case "meeting":
                selectMeetingView();
        }
    }

    @FXML
    protected void showMeetingForm() {
        new_meet_form.setVisible(true);
    }

    @FXML
    protected void hideMeetingForm() {
        new_meet_form.setVisible(false);
    }


    private void selectNewsView() {
        schedule_view.setVisible(false);
        meeting_view.setVisible(false);
        news_view.setVisible(true);
    }

    private void selectScheduleView() {
        meeting_view.setVisible(false);
        news_view.setVisible(false);
        schedule_view.setVisible(true);
    }

    private void selectMeetingView() {
        schedule_view.setVisible(false);
        news_view.setVisible(false);
        meeting_view.setVisible(true);
    }
}