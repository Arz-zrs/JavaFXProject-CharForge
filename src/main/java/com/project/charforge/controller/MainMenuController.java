package com.project.charforge.controller;

import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.utils.IMessageService;
import com.project.charforge.service.interfaces.utils.INavigationService;
import com.project.charforge.utils.Logs;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class MainMenuController {
    @FXML private TableView<PlayerCharacter> tableCharacters;
    @FXML private TableColumn<PlayerCharacter, String> colName;
    @FXML private TableColumn<PlayerCharacter, String> colRace;
    @FXML private TableColumn<PlayerCharacter, String> colClass;
    @FXML private TableColumn<PlayerCharacter, String> colGender;

    private INavigationService navigationService;
    private ICharacterService characterService;
    private IMessageService message;

    public void injectDependencies(
            INavigationService navigationService,
            ICharacterService characterService,
            IMessageService message
    ) {
        this.navigationService = navigationService;
        this.characterService = characterService;
        this.message = message;

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
            message.warning("Player Not Selected", "Please select a character to load.");
            return;
        }
        boolean success = message.confirm(
                "Load " + selected.getName() + "?",
                "Do you want to play as " + selected.getName() + "?"
        );
        if (success) navigationService.goToPaperDoll(selected);
    }

    @FXML
    public void handleDeleteCharacter() {
        PlayerCharacter selected = tableCharacters.getSelectionModel().getSelectedItem();
        if (selected == null) {
            message.warning("No Selection", "Please select a character to delete.");
            return;
        }

        boolean result = message.confirm(
                "Delete Character",
                "Are you sure?",
                "Permanently delete " + selected.getName() + "?\nThis cannot be undone."
        );

        if (result) {
            boolean success = characterService.deleteCharacter(selected.getId());
            if (success) {
                refreshTable();
            } else {
                message.error("Deletion Error", "Failed to delete character.");
                Logs.printError("MainMenuController Error");
            }
        }
    }

    public void handleExitProgram() {
        boolean result = message.confirm("Exit", "Exit App?");
        if (result) navigationService.exitProgram();
    }
}
