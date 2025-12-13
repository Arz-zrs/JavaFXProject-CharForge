package com.project.charforge.app;

import com.project.charforge.controller.CharacterCreationController;
import com.project.charforge.dao.impl.CharClassDaoImpl;
import com.project.charforge.dao.impl.CharacterDaoImpl;
import com.project.charforge.dao.impl.InventoryDaoImpl;
import com.project.charforge.dao.impl.RaceDaoImpl;
import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.dao.interfaces.CharacterDao;
import com.project.charforge.dao.interfaces.InventoryDao;
import com.project.charforge.dao.interfaces.RaceDao;
import com.project.charforge.model.service.impl.CharacterCreationService;
import com.project.charforge.model.service.impl.EquipmentService;
import com.project.charforge.model.service.impl.ValidationService;
import com.project.charforge.model.service.interfaces.ICharacterCreationService;
import com.project.charforge.model.service.interfaces.IEquipmentService;
import com.project.charforge.model.service.interfaces.IValidationService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        // --- DAOs ---
        RaceDao raceDao = new RaceDaoImpl();
        CharClassDao classDao = new CharClassDaoImpl();
        InventoryDao inventoryDao = new InventoryDaoImpl();
        CharacterDao characterDao = new CharacterDaoImpl(raceDao, classDao);

        // --- Services ---
        IValidationService validationService = new ValidationService();
        IEquipmentService equipmentService =
                new EquipmentService(inventoryDao, validationService);

        ICharacterCreationService creationService =
                new CharacterCreationService(characterDao, equipmentService);

        // --- Load UI ---
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/project/charforge/view/character_creation.fxml")
        );
        Parent root = loader.load();

        CharacterCreationController controller = loader.getController();
        controller.injectDependencies(
                raceDao,
                classDao,
                creationService,
                equipmentService
        );

        stage.setScene(new Scene(root));
        stage.show();
    }

}
