module com.example.frontend {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jakarta.ws.rs;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens com.example.frontend to javafx.fxml;
    exports com.example.frontend;
}