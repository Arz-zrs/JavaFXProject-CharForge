package com.project.charforge.config.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.config.interfaces.ControllerInjector;
import com.project.charforge.controller.*;
import com.project.charforge.service.interfaces.characters.ICharacterInventoryService;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.*;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.process.IMessageService;
import com.project.charforge.service.interfaces.process.INavigationService;

import java.util.HashMap;
import java.util.Map;

public class AppControllerInitializer implements ControllerInitializer {

    private final Map<Class<?>, ControllerInjector<?>> injector = new HashMap<>();
    private INavigationService navigationService;

    public AppControllerInitializer(
            IEquipmentService equipmentService,
            IItemService itemService,
            ICharacterInventoryService characterInventoryService,
            IStatCalculator statCalculator,
            ICharacterService characterService,
            IMessageService messageService
    ) {

        injector.put(MainMenuController.class,
                (ControllerInjector<MainMenuController>) c ->
                        c.injectDependencies(
                                navigationService,
                                characterService,
                                messageService
                        )
        );

        injector.put(CharacterCreationController.class,
                (ControllerInjector<CharacterCreationController>) c ->
                        c.injectDependencies(
                                characterService,
                                navigationService,
                                messageService
                        )
        );

        injector.put(PaperDollController.class,
                (ControllerInjector<PaperDollController>) c ->
                        c.injectServices(
                                equipmentService,
                                statCalculator,
                                navigationService,
                                messageService
                        )
        );

        injector.put(ItemLoadoutController.class,
                (ControllerInjector<ItemLoadoutController>) c ->
                        c.injectDependencies(
                                navigationService,
                                itemService,
                                characterInventoryService,
                                statCalculator,
                                characterService,
                                messageService
                        )
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Object controller) {
        validateNavigation();

        ControllerInjector<Object> injector =
                (ControllerInjector<Object>) this.injector.get(controller.getClass());

        if (injector == null) {
            throw new IllegalStateException(
                    "No injector registered for controller: "
                            + controller.getClass().getName()
            );
        }

        injector.inject(controller);
    }

    public void setNavigationService(INavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void validateNavigation() {
        if (navigationService == null) {
            throw new IllegalStateException("NavigationService not set");
        }
    }
}
