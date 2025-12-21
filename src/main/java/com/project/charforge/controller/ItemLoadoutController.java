package com.project.charforge.controller;

import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IInventoryService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.utils.IMessageService;
import com.project.charforge.service.interfaces.utils.INavigationService;
import com.project.charforge.ui.ItemToolTipFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Objects;

public class ItemLoadoutController {
    @FXML private GridPane gridArmory;
    @FXML private GridPane gridBackpack;
    @FXML private ProgressBar progressWeight;
    @FXML private Label lblWeightVal;

    private static final DataFormat DRAG_SOURCE_ARMORY = new DataFormat("application/x-charforge-armory-id");
    private static final DataFormat DRAG_SOURCE_BACKPACK = new DataFormat("application/x-charforge-backpack-index");

    private PlayerCharacter character;
    private INavigationService navigationService;
    private IItemService itemService;
    private IInventoryService inventoryService;
    private IStatCalculator statCalculator;
    private ICharacterService creationService;
    private IMessageService message;

    public void injectDependencies(
            INavigationService navigationService,
            IItemService itemService,
            IInventoryService inventoryService,
            IStatCalculator statCalculator,
            ICharacterService creationService,
            IMessageService message
    ) {
        this.navigationService = navigationService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.statCalculator = statCalculator;
        this.creationService = creationService;
        this.message = message;
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
            content.put(DRAG_SOURCE_ARMORY, item.getId());
            content.putString(String.valueOf(item.getId()));

            db.setContent(content);
            event.consume();
        });
    }

    private void reloadBackpack() {
        List<InventoryItem> items = inventoryService.getInventory(character);

        gridBackpack.getChildren().clear();
        int col = 0, row = 0;

        for (int i = 0; i < items.size(); i++) {
            InventoryItem inv = items.get(i);
            StackPane node = createItemNode(inv.getItem());

            enableDragFromBackpack(node, i);

            gridBackpack.add(node, col++, row);

            if (col > 4) { col = 0; row++; }
        }

        updateWeightUI();
    }

    private void enableDragFromBackpack(StackPane node, int index) {
        node.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(DRAG_SOURCE_BACKPACK, index);
            content.putString(String.valueOf(index));
            db.setContent(content);
            event.consume();
        });
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
        } catch (Exception e) { message.error("Image Load Fail", e.getMessage()); }

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

        progressWeight.getStyleClass().remove("weight-bar-encumbered");
        progressWeight.getStyleClass().add("weight-bar");

        if (current > max) {
            progressWeight.getStyleClass().remove("weight-bar");
            progressWeight.getStyleClass().add("weight-bar-encumbered");
        }
    }

    @FXML
    public void initialize() {
        // Adding items to backback
        gridBackpack.setOnDragOver(e -> {
            // Accept if it's from armory
            if (e.getDragboard().hasContent(DRAG_SOURCE_ARMORY)) {
                e.acceptTransferModes(TransferMode.COPY);
            }
            e.consume();
        });

        gridBackpack.setOnDragDropped(e -> {
            if (!e.getDragboard().hasContent(DRAG_SOURCE_ARMORY)) return;

            Object content = e.getDragboard().getContent(DRAG_SOURCE_ARMORY);
            int itemId;

            if (content instanceof Integer) {
                itemId = (Integer) content;
            }
            else {
                itemId = Integer.parseInt(content.toString());
            }


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

        // Dropping items to armory
        gridArmory.setOnDragOver(e -> {
            // Accept if it's from backpack
            if (e.getDragboard().hasContent(DRAG_SOURCE_BACKPACK)) {
                e.acceptTransferModes(TransferMode.MOVE);
                gridArmory.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
            e.consume();
        });

        gridArmory.setOnDragExited(_ -> gridArmory.setStyle(""));

        gridArmory.setOnDragDropped(e -> {
            if (!e.getDragboard().hasContent(DRAG_SOURCE_BACKPACK)) return;

            int itemIndex = (int) e.getDragboard().getContent(DRAG_SOURCE_BACKPACK);
            InventoryItem itemToRemove = character.getInventory().get(itemIndex);
            inventoryService.removeItem(character, itemToRemove);

            reloadBackpack();
            e.setDropCompleted(true);
            gridArmory.setStyle("");
            e.consume();
        });
    }
    @FXML
    private void handleFinish() {
        try {
            boolean success = message.confirm(
                    "Finalize Character",
                    "Do you want to finalize your character?",
                    "You'll no longer be able to determine your equipment");
            if (success) {
                if (character.getId() == 0) creationService.saveCharacterToDB(character);
                navigationService.goToPaperDoll(character);
            }
        } catch (Exception e) {
            message.error("Save Error", "Failed to save Character.\n" + e.getMessage());
        }
    }

    public void handlePrevious() {
        navigationService.goToCharacterCreation();
    }
}
