module com.project.charforge {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.management;

    opens com.project.charforge.controller to javafx.fxml;
    exports com.project.charforge.controller;

    exports com.project.charforge.app;
    opens com.project.charforge.app to javafx.graphics;

    exports com.project.charforge.model.entity.character;
    exports com.project.charforge.model.entity.inventory;
    exports com.project.charforge.model.entity.item;
    exports com.project.charforge.model.dto;
    exports com.project.charforge.dao.interfaces;
    exports com.project.charforge.config.interfaces;
    exports com.project.charforge.service.interfaces.characters;
    exports com.project.charforge.service.interfaces.stats;
    exports com.project.charforge.service.interfaces.items;
    exports com.project.charforge.service.interfaces.process;
}
