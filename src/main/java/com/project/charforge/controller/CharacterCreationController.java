package com.project.charforge.controller;

import com.project.charforge.dao.impl.CharClassDaoImpl;
import com.project.charforge.dao.impl.RaceDaoImpl;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.character.CharClass;
import com.project.charforge.model.entity.character.Race;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

// THIS IS JUST A TEMPLATE
// TODO: WORK ON CONTROLLER
public class CharacterCreationController {

    @FXML private TextField txtName;
    @FXML private ComboBox<Race> cmbRace;
    @FXML private ComboBox<CharClass> cmbCharClass;
    @FXML private TextArea txtDescription;
    @FXML private Button btnCreate;
    @FXML private RadioButton rbMale;
    @FXML private RadioButton rbFemale;

    private ToggleGroup genderGroup;
    private final RaceDaoImpl raceDao = new RaceDaoImpl();
    private final CharClassDaoImpl classDao = new CharClassDaoImpl();

    @FXML
    public void initialize() {
        // 1. Load Data Master ke ComboBox
        loadMasterData();

        // 2. Setup Listeners (Update deskripsi saat pilihan berubah)
        cmbRace.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDescription());
        cmbCharClass.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateDescription());

        // 3. Setup Button Action
        btnCreate.setOnAction(event -> handleCreate());

        // Agar tampilan ComboBox menampilkan nama, bukan object address
        setupComboCellFactory(cmbRace);
        setupComboCellFactory(cmbCharClass);

        // Setup ToggleGroup agar hanya bisa pilih satu
        genderGroup = new ToggleGroup();
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);
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
                    .append(" | Int: +").append(r.getIntBonus()).append("\n")
                    .append("Weight Mod: x").append(r.getWeightModifier()).append("\n\n");
        }

        //TODO: PERBARUI
//        CharClass c = CharClass.getValue();
//        if (c != null) {
//            desc.append("CLASS: ").append(c.getName()).append("\n")
//                    .append("Str: +").append(c.getStrBonus())
//                    .append(" | Dex: +").append(c.getDexBonus())
//                    .append(" | Int: +").append(c.getIntBonus());
//        }

        txtDescription.setText(desc.toString());
    }

    private void handleCreate() {
        String name = txtName.getText();
        Race race = cmbRace.getValue();
        CharClass cClass = cmbCharClass.getValue();

        if (name.isEmpty() || race == null || cClass == null) {
            showAlert("Incomplete", "Please fill all fields!");
            return;
        }

        // 1. Buat Object Profile Sementara
        PlayerCharacter newChar = new PlayerCharacter();
        // newChar.setId(...) -> Nanti didapat setelah save ke DB
        newChar.setName(name);
        newChar.setRace(race);
        newChar.setCharClass(cClass);

        // Set Gender berdasarkan RadioButton
        if (rbMale.isSelected()) {
            newChar.setGender(Gender.MALE);
        } else {
            newChar.setGender(Gender.FEMALE);
        }

        // 2. TODO: Simpan ke Database (Insert ke tabel characters)
        // int newId = characterDao.save(newChar);
        // newChar.setId(newId);

        // 3. Pindah ke Scene Paper Doll
        navigateToPaperDoll(newChar);
    }

    private void navigateToPaperDoll(PlayerCharacter character) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/charforge/view/paper_doll.fxml"));
            Parent root = loader.load();

            // KIRIM DATA KARAKTER KE CONTROLLER BERIKUTNYA
            PaperDollController controller = loader.getController();
            //controller.setPlayerCharacter(character); // <-- Method ini harus dibuat di PaperDollController

            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 700)); // Perbesar window utk layout baru
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T> void setupComboCellFactory(ComboBox<T> comboBox) {
        // Helper agar ComboBox menampilkan .getName() dari object
        comboBox.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    // Asumsi T adalah BaseEntity atau punya method getName
                    if (item instanceof Race) setText(((Race) item).getName());
                    else if (item instanceof CharClass) setText(((CharClass) item).getName());
                }
            }
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}