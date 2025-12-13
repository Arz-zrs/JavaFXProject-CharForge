package com.project.charforge.service.impl;

import com.project.charforge.config.interfaces.ControllerInitializer;
import com.project.charforge.controller.PaperDollController;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.service.interfaces.INavigationService;
import com.project.charforge.ui.AlertUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationService implements INavigationService {
    private final Stage stage;
    private final ControllerInitializer initializer;

    public NavigationService(Stage stage, ControllerInitializer initializer) {
        this.stage = stage;
        this.initializer = initializer;
    }

    @Override
    public void goToMainMenu() {
        navigate("/com/project/charforge/view/main_menu.fxml");
    }

    @Override
    public void goToCharacterCreation() {
        navigate("/com/project/charforge/view/character_creation.fxml");
    }

    @Override
    public void goToPaperDoll(PlayerCharacter character) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/project/charforge/view/paper_doll.fxml")
            );
            Parent root = loader.load();

            PaperDollController controller = loader.getController();
            initializer.initialize(controller);
            controller.setCharacter(character);

            setScene(root);
        } catch (IOException e) {
            AlertUtils.showError("Error", "Navigation failed");
        }
    }

    private void navigate(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            initializer.initialize(loader.getController());

            setScene(root);
        } catch (IOException e) {
            AlertUtils.showError("Error", "Navigation failed");
        }
    }

    private void setScene(Parent root) {
        Scene current = stage.getScene();
        Scene next = (current == null)
                ? new Scene(root)
                : new Scene(root, current.getWidth(), current.getHeight());

        stage.setScene(next);
        stage.centerOnScreen();
        stage.show();
    }
}
