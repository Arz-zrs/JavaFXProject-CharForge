module com.project.charforge {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;

    opens com.project.charforge.controller to javafx.fxml;
    exports com.project.charforge.controller;

    exports com.project.charforge.app;
    opens com.project.charforge.app to javafx.graphics;
}
