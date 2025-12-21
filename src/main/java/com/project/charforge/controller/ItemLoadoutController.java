package com.project.charforge.controller;

import com.project.charforge.model.dto.StatSnapshot;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.characters.ICharacterInventoryService;
import com.project.charforge.service.interfaces.characters.ICharacterService;
import com.project.charforge.service.interfaces.items.IItemService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.process.IMessageService;
import com.project.charforge.service.interfaces.process.INavigationService;
import com.project.charforge.utils.ItemToolTipFactory;
import com.project.charforge.utils.Logs;
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

    private static final int GRID_COLUMNS = 5;

    private static final double ITEM_SLOT_SIZE = 60.0;
    private static final double ICON_SIZE = 40.0;


    private PlayerCharacter character;
    private INavigationService navigationService;
    private IItemService itemService;
    private ICharacterInventoryService characterInventoryService;
    private IStatCalculator statCalculator;
    private ICharacterService creationService;
    private IMessageService message;

    public void injectDependencies(
            INavigationService navigationService,
            IItemService itemService,
            ICharacterInventoryService characterInventoryService,
            IStatCalculator statCalculator,
            ICharacterService creationService,
            IMessageService message
    ) {
        this.navigationService = navigationService;
        this.itemService = itemService;
        this.characterInventoryService = characterInventoryService;
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
            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
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
        List<InventoryItem> items = characterInventoryService.getInventory(character);

        gridBackpack.getChildren().clear();
        int col = 0, row = 0;

        for (int i = 0; i < items.size(); i++) {
            InventoryItem inv = items.get(i);
            StackPane node = createItemNode(inv.getItem());

            enableDragFromBackpack(node, i);

            gridBackpack.add(node, col++, row);

            if (col >= GRID_COLUMNS) {
                col = 0;
                row++;
            }
        }

        updateWeightUI();
    }

    // Item Discarding
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
        pane.setPrefSize(ITEM_SLOT_SIZE, ITEM_SLOT_SIZE);
        pane.getStyleClass().add("item-slot");

        ImageView img = new ImageView();
        img.setFitWidth(ICON_SIZE);
        img.setFitHeight(ICON_SIZE);

        try {
            String path = "/com/project/charforge/images/items/" + item.getIconPath();
            img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
        } catch (Exception e) {
            message.error("Image Load Fail for" + item.getName(), e.getMessage());
            Logs.printError("ItemLoadoutController Error", e);
        }

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
        // Drop to Backpack / Add Items to Inv
        gridBackpack.setOnDragOver(e -> {
            if (e.getDragboard().hasContent(DRAG_SOURCE_ARMORY)) {
                e.acceptTransferModes(TransferMode.COPY);
                if (!gridBackpack.getStyleClass().contains("equipment-slot-insert")) {
                    gridBackpack.getStyleClass().add("equipment-slot-insert");
                }
            }
            e.consume();
        });

        gridBackpack.setOnDragExited(e -> {
            gridBackpack.getStyleClass().remove("equipment-slot-insert");
            e.consume();
        });

        gridBackpack.setOnDragDropped(e -> {
            boolean success = false;
            if (e.getDragboard().hasContent(DRAG_SOURCE_ARMORY)) {
                Object content = e.getDragboard().getContent(DRAG_SOURCE_ARMORY);
                int itemId = (content instanceof Integer) ? (Integer) content : Integer.parseInt(content.toString());

                characterInventoryService.addItem(character, itemId);
                reloadBackpack();
                success = true;
            }
            e.setDropCompleted(success);
            gridBackpack.getStyleClass().remove("equipment-slot-insert");
            e.consume();
        });

        // Drop to Armory / Discard Items from Inv
        gridArmory.setOnDragOver(e -> {
            if (e.getDragboard().hasContent(DRAG_SOURCE_BACKPACK)) {
                e.acceptTransferModes(TransferMode.MOVE);
                if (!gridArmory.getStyleClass().contains("equipment-slot-discard")) {
                    gridArmory.getStyleClass().add("equipment-slot-discard");
                }
            }
            e.consume();
        });

        gridArmory.setOnDragExited(e -> {
            gridArmory.getStyleClass().remove("equipment-slot-discard");
            e.consume();
        });

        gridArmory.setOnDragDropped(e -> {
            boolean success = false;
            if (e.getDragboard().hasContent(DRAG_SOURCE_BACKPACK)) {
                Object content = e.getDragboard().getContent(DRAG_SOURCE_BACKPACK);
                int itemIndex = (content instanceof Integer) ? (Integer) content : Integer.parseInt(content.toString());

                if (itemIndex >= 0 && itemIndex < character.getInventory().size()) {
                    characterInventoryService.removeItem(character, itemIndex);
                    reloadBackpack();
                    success = true;
                }
            }
            e.setDropCompleted(success);
            gridArmory.getStyleClass().remove("equipment-slot-discard");
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
            Logs.printError("ItemLoadoutController Error", e);
        }
    }

    public void handlePrevious() {
        navigationService.goToCharacterCreation();
    }
}
