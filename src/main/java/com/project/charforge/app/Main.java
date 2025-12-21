package com.project.charforge.app;

import com.project.charforge.config.impl.AppControllerInitializer;
import com.project.charforge.dao.impl.*;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.db.ConnectionProvider;
import com.project.charforge.db.SQLiteConnectionProvider;
import com.project.charforge.service.impl.characters.CharacterInventoryService;
import com.project.charforge.service.impl.items.EquipmentService;
import com.project.charforge.service.impl.items.InventoryService;
import com.project.charforge.service.impl.items.ItemService;
import com.project.charforge.service.impl.stats.EncumbranceService;
import com.project.charforge.service.impl.stats.StatCalculator;
import com.project.charforge.service.impl.process.MessageService;
import com.project.charforge.service.impl.process.NavigationService;
import com.project.charforge.service.impl.characters.CharacterService;
import com.project.charforge.service.impl.process.ValidationService;
import com.project.charforge.service.interfaces.characters.ICharacterInventoryService;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.process.IMessageService;
import com.project.charforge.service.interfaces.process.INavigationService;

import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IEquipmentService;
import com.project.charforge.service.interfaces.process.IValidationService;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Databases & DAOs
        ConnectionProvider provider = new SQLiteConnectionProvider();

        RaceDao raceDao = new RaceDaoImpl(provider);
        CharClassDao classDao = new CharClassDaoImpl(provider);
        CharacterDao characterDao = new CharacterDaoImpl(provider);
        InventoryDao inventoryDao = new InventoryDaoImpl(provider);
        ItemDao itemDao = new ItemDaoImpl(provider);

        // Services
        IMessageService messageService = new MessageService();
        IItemService itemService = new ItemService(itemDao);
        IInventoryService inventoryService = new InventoryService(inventoryDao, itemDao);
        ICharacterInventoryService characterInventoryService = new CharacterInventoryService(inventoryService);
        IValidationService validationService = new ValidationService();
        IEquipmentService equipmentService = new EquipmentService(inventoryDao, validationService, messageService);
        IEncumbranceService encumbranceService = new EncumbranceService();
        IStatCalculator statCalculator = new StatCalculator(encumbranceService);
        ICharacterService characterService = new CharacterService(characterDao, inventoryDao, raceDao, classDao);

        // Controller Initialize
        AppControllerInitializer appInitializer =
                new AppControllerInitializer(
                        equipmentService,
                        itemService,
                        characterInventoryService,
                        statCalculator,
                        characterService,
                        messageService
                );

        // Navigation & Wiring
        INavigationService navigationService = new NavigationService(stage, appInitializer, messageService);
        appInitializer.setNavigationService(navigationService);

        // Stage Setup
        stage.setTitle("CharForge - RPG Character Maker");
        stage.setMaximized(true);

        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/project/charforge/images/app_icon.png")));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Start
        navigationService.goToMainMenu();
    }
}