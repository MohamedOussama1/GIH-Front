module com.example.frontend {

    requires com.fasterxml.jackson.jaxrs.json;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.jaxrs.base;
    requires com.fasterxml.jackson.module.jaxb;
    requires jersey.media.json.jackson;
    requires java.xml;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires jakarta.ws.rs;
    requires java.base;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.desktop;
    requires org.joml;
    requires obj;

    opens com.example.frontend to javafx.fxml;
    exports com.example.frontend;
}