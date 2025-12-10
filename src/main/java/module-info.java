module com.project.charforge {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
    requires com.project.charforge;


    opens com.project.charforge to javafx.fxml;
    exports com.project.charforge;
}