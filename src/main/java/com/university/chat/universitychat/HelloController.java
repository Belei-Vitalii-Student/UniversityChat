package com.university.chat.universitychat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javax.swing.Timer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
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
    Button chat_btn;
    @FXML
    Button notes_btn;

    @FXML
    TextField username_field;
    @FXML
    PasswordField password_field;
    @FXML
    Label username_alert_msg;
    @FXML
    Label password_alert_msg;
    @FXML
    Text login_alert_msg;

    @FXML
    GridPane news_view;
    @FXML
    TableView schedule_view;
    @FXML
    VBox meeting_view;
    @FXML
    Pane new_meet_form;
    @FXML
    VBox chat_view;
    @FXML
    Pane notes_view;

    @FXML
    MenuButton menu_options;
    private Stage stage;
    private PostgresqlDB postgresqlDB = new PostgresqlDB();

    public HelloController() throws Exception {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            if(news_view == null) {
                menu_options.setText("Student");
                return;
            }
            renderNews();
            menu_options.setText("Teacher");
        } catch (Exception e) {
            return;
        }
    }

    @FXML
    protected void login(ActionEvent event) throws IOException {
        String usernameText = username_field.getText();
        String passwordText = password_field.getText();

        if (usernameText.length() == 0) {
            username_alert_msg.setVisible(true);
            Timer timer = new Timer(2000, e -> {
                username_alert_msg.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();

            return;
        }

        if (passwordText.length() == 0) {
            password_alert_msg.setVisible(true);
            Timer timer = new Timer(2000, e -> {
                password_alert_msg.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();

            return;
        }

        try {
            Integer role = postgresqlDB.validateData(usernameText, passwordText);
            if (role != null && role == 0) {
                renderStudentPage(event);
            } else if (role != null && role == 1) {
                renderTeacherPage(event);
            }
        } catch (Exception e) {
            login_alert_msg.setText(e.getMessage());
            login_alert_msg.setVisible(true);
            Timer timer = new Timer(3000, err -> {
                login_alert_msg.setText("Log in");
                login_alert_msg.setVisible(false);
            });
            timer.setRepeats(false);
            timer.start();
            return;
        }
    }

    private void renderTeacherPage(ActionEvent event) throws IOException {
        Parent teacherView = FXMLLoader.load(getClass().getResource("teacher-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(teacherView);
        stage.setScene(scene);
        stage.show();
    }

    private void renderStudentPage(ActionEvent event) throws IOException {
        Parent teacherView = FXMLLoader.load(getClass().getResource("student-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(teacherView);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void logout(ActionEvent event) throws IOException, SQLException {
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
                break;
            case "chat":
                selectChatView();
                break;
            case "notes":
                selectNotesView();
                break;
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

    private void selectChatView() {
        chat_view.setVisible(true);
        notes_view.setVisible(false);
    }

    private void selectNotesView() {
        chat_view.setVisible(false);
        notes_view.setVisible(true);
    }

    protected void renderNews() throws SQLException {
        System.out.println("RENDER NEWS");

        ResultSet news = postgresqlDB.selectAllNews();

        Integer row = 0, column = 0;

        while (news.next()) {
            Integer id = news.getInt("ID");
            System.out.println(id);
            String author = news.getString("USERNAME");
            System.out.println(author);
//            Timestamp date = news.getTimestamp("DATE");
//            System.out.println(date);
            Date date = new Date();
            String description = news.getString("DESCRIPTION");
            System.out.println(description);

            System.out.println(id + " | " + author + " | " + date + " | " + description);

            VBox vBox = new VBox();
            vBox.setPrefWidth(200);
            vBox.setPrefHeight(100);

            HBox hBox = new HBox();
            hBox.setPrefHeight(40);

            HBox hBox2 = new HBox();

            Text descriptionFlow = new Text();
            descriptionFlow.setText(description);

            hBox2.getChildren().add(descriptionFlow);

            Label authorLabel = new Label();
            authorLabel.setText(author);

            Label dateLabel = new Label();
            dateLabel.setText(date.toString());

            hBox.getChildren().add(authorLabel);
            hBox.getChildren().add(dateLabel);

            vBox.getChildren().add(hBox);
            vBox.getChildren().add(hBox2);

            news_view.add(vBox, column, row);

            if(column >= 1) {
                row++;
            } else {
                column++;
            }
        }
    }
}