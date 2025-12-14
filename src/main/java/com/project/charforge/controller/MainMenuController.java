package com.project.charforge.controller;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.utils.INavigationService;
import com.project.charforge.ui.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Optional;

public class MainMenuController {
    @FXML private TableView<PlayerCharacter> tableCharacters;
    @FXML private TableColumn<PlayerCharacter, String> colName;
    @FXML private TableColumn<PlayerCharacter, String> colRace;
    @FXML private TableColumn<PlayerCharacter, String> colClass;
    @FXML private TableColumn<PlayerCharacter, String> colGender;

    private INavigationService navigationService;
    private ICharacterService characterService;

    public void injectDependencies(INavigationService navigationService, ICharacterService characterService) {
        this.navigationService = navigationService;
        this.characterService = characterService;

        refreshTable();
    }

    @FXML
    private void initialize() {
        setupTable();
    }

    private void setupTable(){
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colRace.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getRace().getName()));
        colClass.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCharClass().getName()));
        colGender.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getGender().name()));

    }

    private void refreshTable() {
        if (characterService == null) throw new IllegalStateException("characterService not injected");
        List<PlayerCharacter> characters = characterService.findAllCharacters();
        tableCharacters.getItems().setAll(characters);
    }

    @FXML
    private void handleNewCharacter() {
        navigationService.goToCharacterCreation();
    }

    @FXML
    private void handleLoadCharacter() {
        PlayerCharacter selected = tableCharacters.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("No Selection", "Please select a character to load.");
            return;
        }
        navigationService.goToPaperDoll(selected);
    }

    @FXML
    public void handleDeleteCharacter() {
        PlayerCharacter selected = tableCharacters.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("No Selection", "Please select a character to delete.");
            return;
        }

        Optional<ButtonType> result = AlertUtils.showConfirmation(
                "Delete Character",
                "Are you sure?",
                "Permanently delete " + selected.getName() + "?\nThis cannot be undone."
        );

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = characterService.deleteCharacter(selected.getId());
            if (success) {
                refreshTable();
            } else {
                AlertUtils.showError("Error", "Failed to delete character.");
            }
        }
    }

    public void handleExitProgram() {
        navigationService.exitProgram();
    }
}
