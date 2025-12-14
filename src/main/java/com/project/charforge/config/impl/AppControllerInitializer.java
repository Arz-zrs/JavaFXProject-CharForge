package com.project.charforge.config.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.controller.*;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.service.interfaces.characters.*;
import com.project.charforge.service.interfaces.items.*;
import com.project.charforge.service.interfaces.stats.IEncumbranceService;
import com.project.charforge.service.interfaces.utils.INavigationService;

public class AppControllerInitializer implements ControllerInitializer {

    private final CharacterDao characterDao;
    private final RaceDao raceDao;
    private final CharClassDao classDao;

    private final IEquipmentService equipmentService;
    private final IItemService itemService;
    private final IInventoryService inventoryService;

    private final ICharacterStatService characterStatService;
    private final IEncumbranceService encumbranceService;

    private final ICharacterCreationService creationService;
    private final ICharacterService characterService;

    private INavigationService navigationService;

    public AppControllerInitializer(
            CharacterDao characterDao,
            RaceDao raceDao,
            CharClassDao classDao,
            IEquipmentService equipmentService,
            IItemService itemService,
            IInventoryService inventoryService,
            ICharacterStatService characterStatService,
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
        this.characterStatService = characterStatService;
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
                    characterStatService,
                    navigationService
            );
        }

        else if (controller instanceof ItemLoadoutController c) {
            validateNavigation();
            c.injectDependencies(
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
