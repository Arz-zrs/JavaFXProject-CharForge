package com.project.charforge.config.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.controller.CharacterCreationController;
import com.project.charforge.controller.MainMenuController;
import com.project.charforge.controller.PaperDollController;
import com.project.charforge.dao.interfaces.*;
import com.project.charforge.service.impl.StatCalculator;
import com.project.charforge.service.interfaces.*;

public class AppControllerInitializer implements ControllerInitializer {

    private final CharacterDao characterDao;
    private final RaceDao raceDao;
    private final CharClassDao classDao;
    private final IEquipmentService equipmentService;
    private final ICharacterCreationService creationService;
    private final ICharacterService characterService;
    private INavigationService navigationService;


    public AppControllerInitializer(
            CharacterDao characterDao,
            RaceDao raceDao,
            CharClassDao classDao,
            IEquipmentService equipmentService,
            ICharacterCreationService creationService,
            ICharacterService characterService)
    {
        this.characterDao = characterDao;
        this.raceDao = raceDao;
        this.classDao = classDao;
        this.equipmentService = equipmentService;
        this.creationService = creationService;
        this.characterService = characterService;
    }

    @Override
    public void initialize(Object controller) {
        if (controller instanceof MainMenuController c) {
            if (navigationService == null) throw new IllegalStateException("NavigationService not set");
            c.injectDependencies(characterDao, navigationService, characterService);
        }

        else if (controller instanceof CharacterCreationController c) {
            c.injectDependencies(
                    raceDao,
                    classDao,
                    creationService,
                    navigationService
            );
        }

        else if (controller instanceof PaperDollController c) {
            c.injectServices(equipmentService, new StatCalculator());
        }
    }

    public void setNavigationService(INavigationService navigationService) {
        this.navigationService = navigationService;
    }

}
