package com.project.charforge.service.interfaces.process;

import com.project.charforge.model.entity.character.PlayerCharacter;

public interface INavigationService {
    void goToMainMenu();
    void goToCharacterCreation();
    void goToPaperDoll(PlayerCharacter character);
    void goToItemLoadout(PlayerCharacter character);
    void exitProgram();
}
