package com.project.charforge.config.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.controller.CharacterCreationController;
import com.project.charforge.controller.ItemLoadoutController;
import com.project.charforge.controller.MainMenuController;
import com.project.charforge.controller.PaperDollController;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.characters.ICharacterCreationService;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IEquipmentService;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.utils.INavigationService;

public class AppControllerInitializer implements ControllerInitializer {

    private final CharacterDao characterDao;
    private final RaceDao raceDao;
    private final CharClassDao classDao;

    private final IEquipmentService equipmentService;
    private final IItemService itemService;
    private final IInventoryService inventoryService;

    private final IStatCalculator statCalculator;
    private final IEncumbranceService encumbranceService;

    private final ICharacterCreationService creationService;
    private final ICharacterService characterService;

    private INavigationService navigationService;
    private PlayerCharacter playerCharacter;

    public AppControllerInitializer(
            CharacterDao characterDao,
            RaceDao raceDao,
            CharClassDao classDao,
            IEquipmentService equipmentService,
            IItemService itemService,
            IInventoryService inventoryService,
            IStatCalculator statCalculator,
            IEncumbranceService encumbranceService,
            ICharacterCreationService creationService,
            ICharacterService characterService
    ) {
        this.characterDao = characterDao;
        this.raceDao = raceDao;
        this.classDao = classDao;
        this.equipmentService = equipmentService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.statCalculator = statCalculator;
        this.encumbranceService = encumbranceService;
        this.creationService = creationService;
        this.characterService = characterService;
    }

    @Override
    public void initialize(Object controller) {
        if (controller instanceof MainMenuController c) {
            validateNavigation();
            c.injectDependencies(
                    characterDao,
                    navigationService,
                    characterService
            );
        }

        else if (controller instanceof CharacterCreationController c) {
            validateNavigation();
            c.injectDependencies(
                    raceDao,
                    classDao,
                    creationService,
                    navigationService
            );
        }

        else if (controller instanceof PaperDollController c) {
            validateNavigation();
            c.injectServices(
                    equipmentService,
                    statCalculator,
                    encumbranceService,
                    navigationService
            );
        }

        else if (controller instanceof ItemLoadoutController c) {
            validateNavigation();
            c.injectDependencies(
                    playerCharacter,
                    navigationService,
                    itemService,
                    inventoryService,
                    encumbranceService
            );
        }    }

    public void setNavigationService(INavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void validateNavigation() {
        if (navigationService == null) throw new IllegalStateException("NavigationService not set");
    }
}
