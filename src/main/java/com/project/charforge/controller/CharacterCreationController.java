package com.project.charforge.controller;

import com.project.charforge.dao.interfaces.CharClassDao;
import com.project.charforge.dao.interfaces.RaceDao;
import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.Race;
import com.project.charforge.service.impl.StatCalculator;
import com.project.charforge.service.interfaces.ICharacterCreationService;
import com.project.charforge.service.interfaces.IEquipmentService;
import com.project.charforge.service.interfaces.INavigationService;
import com.project.charforge.ui.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CharacterCreationController {

    @FXML private TextField txtName;
    @FXML private ComboBox<Race> cmbRace;
    @FXML private ComboBox<CharClass> cmbCharClass;
    @FXML private TextArea txtDescription;
    @FXML private Button btnCreate;
    @FXML private RadioButton rbMale;
    @FXML private RadioButton rbFemale;

    private ToggleGroup genderGroup;

    private RaceDao raceDao;
    private CharClassDao classDao;
    private ICharacterCreationService creationService;
    private INavigationService navigationService;

    public void injectDependencies(RaceDao raceDao, CharClassDao classDao, ICharacterCreationService creationService, INavigationService navigationService) {
        this.raceDao = raceDao;
        this.classDao = classDao;
        this.creationService = creationService;
        this.navigationService = navigationService;

        loadMasterData();
    }

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);

        cmbRace.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDescription());
        cmbCharClass.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDescription());

        btnCreate.setOnAction(event -> handleCreate());

        setupComboCellFactory(cmbRace);
        setupComboCellFactory(cmbCharClass);
    }

    private void handleCreate() {
        if (!isInputValid()) {
            AlertUtils.showWarning("Incomplete", "Please fill all fields!");
            return;
        }

        try {
            PlayerCharacter pc = creationService.createCharacter(
                    txtName.getText(),
                    rbMale.isSelected() ? Gender.MALE : Gender.FEMALE,
                    cmbRace.getValue(),
                    cmbCharClass.getValue()
            );

            navigationService.goToPaperDoll(pc);

        } catch (Exception e) {
            AlertUtils.showError("Error", e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void loadMasterData() {
        List<Race> races = raceDao.findAll();
        cmbRace.getItems().addAll(races);

        List<CharClass> classes = classDao.findAll();
        cmbCharClass.getItems().addAll(classes);
    }

    private void updateDescription() {
        StringBuilder desc = new StringBuilder();

        Race r = cmbRace.getValue();
        if (r != null) {
            desc.append("RACE: ").append(r.getName()).append("\n")
                    .append("Str: +").append(r.getStrBonus())
                    .append(" | Dex: +").append(r.getDexBonus())
                    .append(" | Int: +").append(r.getIntBonus()).append("\n");
        }

        CharClass c = cmbCharClass.getValue();
        if (c != null) {
            desc.append("\nCLASS: ").append(c.getName()).append("\n")
                    .append("Str: +").append(c.getStrBonus())
                    .append(" | Dex: +").append(c.getDexBonus())
                    .append(" | Int: +").append(c.getIntBonus());
        }

        txtDescription.setText(desc.toString());
    }

    private <T> void setupComboCellFactory(ComboBox<T> comboBox) {
        comboBox.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(T item, boolean empty) {
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
}