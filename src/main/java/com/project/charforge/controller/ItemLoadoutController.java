package com.project.charforge.controller;

import com.project.charforge.console.Logs;
import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.utils.INavigationService;
import com.project.charforge.ui.AlertUtils;
import com.project.charforge.ui.ItemToolTipFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class ItemLoadoutController {
    @FXML private GridPane gridArmory;
    @FXML private GridPane gridBackpack;
    @FXML private ProgressBar progressWeight;
    @FXML private Label lblWeightVal;

    private PlayerCharacter character;
    private INavigationService navigationService;
    private IItemService itemService;
    private IInventoryService inventoryService;
    private IStatCalculator statCalculator;
    private ICharacterService creationService;

    public void injectDependencies(
            INavigationService navigationService,
            IItemService itemService,
            IInventoryService inventoryService,
            IStatCalculator statCalculator,
            ICharacterService creationService
    ) {
        this.navigationService = navigationService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.statCalculator = statCalculator;
        this.creationService = creationService;
    }

    public void setCharacter(PlayerCharacter character) {
        this.character = character;
        loadArmory();
        reloadBackpack();
    }

    private void loadArmory() {
        List<Item> allItems = itemService.getAllItems();

        gridArmory.getChildren().clear();
        int col = 0, row = 0;

        for (Item item : allItems) {
            StackPane node = createItemNode(item);
            enableDragFromArmory(node, item);

            gridArmory.add(node, col++, row);
            if (col > 4) { col = 0; row++; }
        }
    }

    private void enableDragFromArmory(StackPane node, Item item) {
        node.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.COPY);

            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(item.getId()));

            db.setContent(content);
            event.consume();
        });
    }

    private void reloadBackpack() {
        List<InventoryItem> items = inventoryService.getInventory(character);

        gridBackpack.getChildren().clear();
        int col = 0, row = 0;

        for (InventoryItem inv : items) {
            StackPane node = createItemNode(inv.getItem());
            gridBackpack.add(node, col++, row);

            if (col > 4) { col = 0; row++; }
        }

        updateWeightUI();
    }

    private StackPane createItemNode(Item item) {
        StackPane pane = new StackPane();
        pane.setPrefSize(60, 60);
        pane.setStyle("-fx-background-color: #333; -fx-border-color: #555; -fx-border-radius: 5;");

        ImageView img = new ImageView();
        img.setFitWidth(40);
        img.setFitHeight(40);

        try {
            String path = "/com/project/charforge/images/items/" + item.getIconPath();
            img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
        } catch (Exception e) { e.getStackTrace(); }

        pane.getChildren().add(img);

        ItemToolTipFactory.install(pane, item);

        return pane;
    }

    private void updateWeightUI() {
        StatSnapshot stats = statCalculator.calculate(character);

        double current = stats.currentWeight();
        double max = stats.maxWeight();

        progressWeight.setProgress(current / max);
        lblWeightVal.setText(String.format("%.1f / %.1f kg", current, max));

        progressWeight.setStyle(
                current > max ? "-fx-accent: red;" : "-fx-accent: green;"
        );
    }

    @FXML
    public void initialize() {
        gridBackpack.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });

        gridBackpack.setOnDragDropped(e -> {
            if (!e.getDragboard().hasString()) return;

            int itemId = Integer.parseInt(e.getDragboard().getString());
            if (character.getId() == 0) {
                inventoryService.addTempItem(character, itemId);
            }
            else {
                inventoryService.addItem(character, itemId);
            }

            reloadBackpack();
            e.setDropCompleted(true);
            e.consume();
        });
    }

    @FXML
    private void handleFinish() {
        try {
            if (character.getId() == 0) creationService.saveCharacterToDB(character);
            navigationService.goToPaperDoll(character);

        } catch (Exception e) {
            Logs.printError(e.getMessage());
            e.getStackTrace();
            AlertUtils.showError("Save Error", "Gagal menyimpan karakter.");
        }
    }

    public void handlePrevious() {
        navigationService.goToCharacterCreation();
    }
}
