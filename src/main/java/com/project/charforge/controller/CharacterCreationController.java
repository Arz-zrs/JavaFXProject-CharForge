package com.project.charforge.controller;

import com.project.charforge.model.entity.character.*;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.utils.IMessageService;
import com.project.charforge.service.interfaces.utils.INavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CharacterCreationController {

    @FXML private TextField txtName;
    @FXML private ComboBox<Race> cmbRace;
    @FXML private ComboBox<CharClass> cmbCharClass;
    @FXML private TextArea txtDescription;
    @FXML private RadioButton rbMale;
    @FXML private RadioButton rbFemale;

    private ToggleGroup genderGroup;

    private ICharacterService creationService;
    private INavigationService navigationService;
    private IMessageService message;

    public void injectDependencies(
            ICharacterService creationService,
            INavigationService navigationService
            ,IMessageService messageService
    ) {
        this.creationService = creationService;
        this.navigationService = navigationService;
        this.message = messageService;

        loadMasterData();
    }

    @FXML
    public void initialize() {
        setupGenderToggle();
        setupComboBoxes();
    }

    private void setupGenderToggle() {
        genderGroup = new ToggleGroup();
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);
    }

    private void setupComboBoxes() {
        setupComboCellFactory(cmbRace);
        setupComboCellFactory(cmbCharClass);

        cmbRace.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> updateDescription());
        cmbCharClass.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> updateDescription());
    }


    private void loadMasterData() {
        cmbRace.getItems().setAll(creationService.getAllRaces());
        cmbCharClass.getItems().setAll(creationService.getAllClasses());
    }


    private void updateDescription() {
        StringBuilder desc = new StringBuilder();
        Race r = cmbRace.getValue();
        if (r != null) desc.append(r.describe()).append("\n");

        CharClass c = cmbCharClass.getValue();
        if (c != null) desc.append(c.describe());

        txtDescription.setText(desc.toString());
    }

    private <T> void setupComboCellFactory(ComboBox<T> comboBox) {
        comboBox.setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    if (item instanceof Race) setText(((Race) item).getName());
                    else if (item instanceof CharClass) setText(((CharClass) item).getName());
                }
            }
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));
    }

    private boolean isInputValid() {
        return !txtName.getText().isBlank()
                && cmbRace.getValue() != null
                && cmbCharClass.getValue() != null
                && genderGroup.getSelectedToggle() != null;
    }

    @FXML
    public void handleCreateCharacter() {
        if (!isInputValid()) {
            message.error("Incomplete", "Please fill all fields!");
            return;
        }

        try {
            PlayerCharacter pc = creationService.createCharacter(
                    txtName.getText(),
                    getSelectedGender(),
                    cmbRace.getValue(),
                    cmbCharClass.getValue()
            );

            navigationService.goToItemLoadout(pc);
        } catch (Exception e) {
            message.error("Error", e.getMessage());
        }
    }

    private Gender getSelectedGender() {
        return rbMale.isSelected() ? Gender.MALE : Gender.FEMALE;
    }

    @FXML
    public void handleReturnToMenu() {
        navigationService.goToMainMenu();
    }
}