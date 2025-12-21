package com.project.charforge.config.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.controller.*;
import com.project.charforge.service.interfaces.characters.*;
import com.project.charforge.service.interfaces.items.*;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.process.IMessageService;
import com.project.charforge.service.interfaces.process.INavigationService;

public class AppControllerInitializer implements ControllerInitializer {
    private final IEquipmentService equipmentService;
    private final IItemService itemService;
    private final IInventoryService inventoryService;
    private final IStatCalculator statCalculator;
    private final ICharacterService characterService;
    private final IMessageService messageService;
    private INavigationService navigationService;

    public AppControllerInitializer(
            IEquipmentService equipmentService,
            IItemService itemService,
            IInventoryService inventoryService,
            IStatCalculator statCalculator,
            ICharacterService characterService,
            IMessageService messageService
    ) {
        this.equipmentService = equipmentService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.statCalculator = statCalculator;
        this.characterService = characterService;
        this.messageService = messageService;
    }

    @Override
    public void initialize(Object controller) {
        if (controller instanceof MainMenuController c) {
            validateNavigation();
            c.injectDependencies(
                    navigationService,
                    characterService,
                    messageService
            );
        }

        else if (controller instanceof CharacterCreationController c) {
            validateNavigation();
            c.injectDependencies(
                    characterService,
                    navigationService,
                    messageService
            );
        }

        else if (controller instanceof PaperDollController c) {
            validateNavigation();
            c.injectServices(
                    equipmentService,
                    statCalculator,
                    navigationService,
                    messageService
            );
        }

        else if (controller instanceof ItemLoadoutController c) {
            validateNavigation();
            c.injectDependencies(
                    navigationService,
                    itemService,
                    inventoryService,
                    statCalculator,
                    characterService,
                    messageService
            );
        }    }

    public void setNavigationService(INavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void validateNavigation() {
        if (navigationService == null) throw new IllegalStateException("NavigationService not set");
    }
}
