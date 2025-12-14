package com.project.charforge.app;

import com.project.charforge.config.impl.AppControllerInitializer;
import com.project.charforge.dao.impl.*;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.service.impl.items.EquipmentService;
import com.project.charforge.service.impl.items.InventoryService;
import com.project.charforge.service.impl.items.ItemService;
import com.project.charforge.service.impl.stats.EncumbranceService;
import com.project.charforge.service.impl.stats.StatCalculator;
import com.project.charforge.service.impl.utils.NavigationService;
import com.project.charforge.service.impl.characters.CharacterCreationService;
import com.project.charforge.service.impl.characters.CharacterService;
import com.project.charforge.service.impl.utils.ValidationService;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.utils.INavigationService;

import com.project.charforge.service.interfaces.characters.ICharacterCreationService;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IEquipmentService;
import com.project.charforge.service.interfaces.utils.IValidationService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // DAOs
        RaceDao raceDao = new RaceDaoImpl();
        CharClassDao classDao = new CharClassDaoImpl();
        CharacterDao characterDao = new CharacterDaoImpl(raceDao, classDao);
        InventoryDao inventoryDao = new InventoryDaoImpl();
        ItemDao itemDao = new ItemDaoImpl();

        // Services
        IItemService itemService = new ItemService(itemDao);
        IInventoryService inventoryService = new InventoryService(inventoryDao);
        IValidationService validationService = new ValidationService();
        IEquipmentService equipmentService = new EquipmentService(inventoryDao, validationService);
        IStatCalculator statCalculator = new StatCalculator();
        IEncumbranceService encumbranceService = new EncumbranceService(statCalculator);
        ICharacterCreationService creationService = new CharacterCreationService(characterDao);
        ICharacterService characterService = new CharacterService(characterDao);

        // Controller Initialize
        AppControllerInitializer appInitializer =
                new AppControllerInitializer(
                        characterDao,
                        raceDao,
                        classDao,
                        equipmentService,
                        itemService,
                        inventoryService,
                        statCalculator,
                        encumbranceService,
                        creationService,
                        characterService
                );

        // Navigation & Wiring
        INavigationService navigationService = new NavigationService(stage, appInitializer);
        appInitializer.setNavigationService(navigationService);

        // Stage Setup
        stage.setTitle("CharForge - RPG Simulator");
        stage.setMaximized(true);

        // Start
        navigationService.goToMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}