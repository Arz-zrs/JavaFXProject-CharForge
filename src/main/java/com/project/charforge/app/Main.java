package com.project.charforge.app;

import com.project.charforge.config.impl.AppControllerInitializer;
import com.project.charforge.dao.impl.*;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.service.impl.*;
import com.project.charforge.service.interfaces.*;
import com.project.charforge.service.impl.NavigationService;
import com.project.charforge.service.interfaces.INavigationService;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // 1. DAOs
        RaceDao raceDao = new RaceDaoImpl();
        CharClassDao classDao = new CharClassDaoImpl();
        CharacterDao charDao = new CharacterDaoImpl(raceDao, classDao);
        InventoryDao invDao = new InventoryDaoImpl();

        // 2. Services
        IValidationService validationService = new ValidationService();
        IEquipmentService equipmentService = new EquipmentService(invDao, validationService);
        ICharacterCreationService creationService = new CharacterCreationService(charDao, equipmentService);
        ICharacterService characterService = new CharacterService(charDao);

        // 3. Controller Initialize
        AppControllerInitializer appInitializer = new AppControllerInitializer(
                charDao,
                raceDao,
                classDao,
                equipmentService,
                creationService,
                characterService
        );

        // 4. Navigation & Wiring
        INavigationService navigationService = new NavigationService(stage, appInitializer);
        appInitializer.setNavigationService(navigationService);

        // 5. Stage Setup
        stage.setTitle("CharForge - RPG Simulator");
        stage.setMaximized(true);

        // 6. Start
        navigationService.goToMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}