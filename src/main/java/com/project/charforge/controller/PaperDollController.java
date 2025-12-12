package com.project.charforge.controller;

import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.Objects;


// TODO: UPDATE DAN OLAH CONTROLLER
public class PaperDollController {

    @FXML
    private ImageView imgSilhouette; // Inject Image View Siluet
    @FXML
    private Label lblName;
    @FXML
    private Label lblRaceClass;

    // ... field slot lain ...

    private PlayerCharacter character;

    // Method ini dipanggil dari CharacterCreationController
    public void setProfile(PlayerCharacter character) {
        this.character = character;

        // 1. Update Teks Info
        lblName.setText(character.getName().toUpperCase());
        lblRaceClass.setText(character.getRace().getName() + " " + character.getCharClass().getName());

        // 2. Update Siluet Berdasarkan Gender
        updateSilhouetteImage(character.getGender());
    }

    private void updateSilhouetteImage(Gender gender) {
        String imagePath;

        if (gender == Gender.MALE) {
            imagePath = "/com/project/charforge/assets/silhouette_male.png";
        } else {
            imagePath = "/com/project/charforge/assets/silhouette_female.png";
        }

        try {
            // Load gambar dari resources
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imgSilhouette.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Gagal memuat gambar siluet: " + imagePath);
        }
    }
}
