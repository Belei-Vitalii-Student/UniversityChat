module com.university.chat.universitychat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.desktop;

    opens com.university.chat.universitychat to javafx.fxml;
    exports com.university.chat.universitychat;
}