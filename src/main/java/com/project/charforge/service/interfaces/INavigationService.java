package com.project.charforge.service.interfaces;

import com.project.charforge.model.entity.character.PlayerCharacter;

public interface INavigationService {
    void goToMainMenu();
    void goToCharacterCreation();
    void goToPaperDoll(PlayerCharacter character);
}
