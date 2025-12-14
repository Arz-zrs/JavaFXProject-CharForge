package com.project.charforge.controller;

import com.project.charforge.console.Logs;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.model.entity.item.Item;
import com.project.charforge.service.interfaces.IEquipmentService;
import com.project.charforge.service.interfaces.INavigationService;
import com.project.charforge.service.interfaces.IStatCalculator;
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

public class PaperDollController {

    @FXML private ImageView imgSilhouette;
    @FXML private Label lblName, lblRaceClass ;
    @FXML private Label lblTotalStr, lblTotalDex, lblTotalInt, lblWeightVal ;
    @FXML private ProgressBar progressWeight;
    @FXML private GridPane inventoryGrid;

    @FXML private StackPane slotHead, slotAccessory, slotBody, slotMainHand, slotOffHand, slotLegs;
    @FXML private ImageView imgHead, imgAccessory, imgBody, imgMainHand, imgOffHand, imgLegs;

    private PlayerCharacter character;

    private IEquipmentService equipmentService;
    private IStatCalculator statCalculator;
    private INavigationService navigationService;

    public void injectServices(IEquipmentService equipmentService, IStatCalculator statCalculator, INavigationService navigationService) {
        this.equipmentService = equipmentService;
        this.statCalculator = statCalculator;
        this.navigationService = navigationService;
    }

    public void setCharacter(PlayerCharacter character) {
        this.character = character;
        updateHeaderInfo();
        reloadInventory();
        refreshStats();
    }

    // Refresh inventory
    private void reloadInventory() {
        List<InventoryItem> items = equipmentService.loadInventory(character);

        clearAllSlots();
        inventoryGrid.getChildren().clear();

        for (InventoryItem item : items) {
            if (item.isEquipped()) {
                renderEquippedItem(item);
            } else {
                renderInventoryItem(item);
            }
        }
    }

    // Updates header label
    private void updateHeaderInfo(){
        lblName.setText(character.getName().toUpperCase());
        lblRaceClass.setText(character.getRace().getName() + " " + character.getCharClass().getName());

        String imgMalePath = "/com/project/charforge/images/silhouette_male.png";
        String imgFemalePath = "/com/project/charforge/images/silhouette_female.png";

        String path = character.getGender() == Gender.MALE
                ? imgMalePath : imgFemalePath;

        imgSilhouette.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
    }

    private void renderEquippedItem(InventoryItem item) {
        EquipmentSlot slot = EquipmentSlot.valueOf(item.getSlotName());
        ImageView target = getImageViewForSlot(slot);

        if (target != null) {
            setImageFromPath(target, item.getItem().getIconPath());
            target.setVisible(true);
            target.getParent().setUserData(item);

            enableDragFromEquipmentSlot((StackPane) target.getParent(), item);
            installToolTips(target, item);
        }
    }

    private void renderInventoryItem(InventoryItem item) {
        StackPane pane = new StackPane();
        pane.setPrefSize(50, 50);
        pane.setStyle("-fx-background-color: #333; -fx-border-color: #555;");
        pane.setUserData(item);

        ImageView icon = new ImageView();
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        setImageFromPath(icon, item.getItem().getIconPath());

        pane.getChildren().add(icon);
        enableDragSource(pane, item);

        installToolTips(pane, item);

        inventoryGrid.add(pane, item.getGridIndex() % 10, item.getGridIndex() / 10);
    }

    // ToolTips Logic
    private void installToolTips(Node node, InventoryItem inventoryItem) {
        Item item = inventoryItem.getItem();
        StringBuilder sb = new StringBuilder();

        // Header and subheader
        sb.append(item.getName().toUpperCase()).append("\n");
        sb.append(item.getValidSlot().name().replace("_", " ")).append("\n\n");

        // Stats logic
        if (item.getStrBonus() != 0) sb.append("STR: ").append(item.getStrBonus() > 0 ? "+" : "").append(item.getStrBonus()).append("\n");
        if (item.getDexBonus() != 0) sb.append("DEX: ").append(item.getDexBonus() > 0 ? "+" : "").append(item.getDexBonus()).append("\n");
        if (item.getIntBonus() != 0) sb.append("INT: ").append(item.getIntBonus() > 0 ? "+" : "").append(item.getIntBonus()).append("\n");

        // Footer
        sb.append("\nWeight: ").append(item.getWeight()).append(" kg");

        Tooltip tooltip = new Tooltip(sb.toString());
        tooltip.setShowDelay(Duration.millis(100));
        tooltip.setShowDuration(Duration.seconds(10));
        Tooltip.install(node, tooltip);
    }

    // Drag & Drop Logic
    // Drag (Source)
    private void enableDragSource(Node node, InventoryItem item) {
        node.setOnDragDetected(event -> {
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(item.getInstanceId()));
            db.setContent(content);
            event.consume();
        });
    }

    // Drag from Equipment Slot
    private void enableDragFromEquipmentSlot(StackPane slotPane, InventoryItem item) {
        slotPane.setOnDragDetected(event -> {
            Dragboard db = slotPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(item.getInstanceId()));
            db.setContent(content);
            event.consume();
        });
    }

    // Drop (Target)
    private void setupSlotEvents(StackPane slotPane, EquipmentSlot slotType) {

        // When dragged over slot
        slotPane.setOnDragOver(event -> {
            var db = event.getDragboard();

            if (db.hasString()) {
                try {
                    int id = Integer.parseInt(db.getString());

                    boolean valid = equipmentService.canEquip(character, id, slotType);

                    if (valid) {
                        event.acceptTransferModes(TransferMode.MOVE);
                        slotPane.setStyle("-fx-border-color: #4CAF50; -fx-border-width: 2;");
                    } else {
                        slotPane.setStyle("-fx-border-color: #FF5252; -fx-border-width: 2;");
                    }

                } catch (NumberFormatException ignore) {}
            }

            event.consume();
        });

        slotPane.setOnDragExited(event -> {
            slotPane.setStyle("");
            event.consume();
        });

        // Final drop event
        slotPane.setOnDragDropped(event -> {
            var db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                int id = Integer.parseInt(db.getString());

                try {
                    equipmentService.equip(character, id, slotType);
                    reloadInventory();
                    refreshStats();
                    success = true;

                } catch (IllegalStateException e) {
                    Logs.printError(e.getMessage());
                    slotPane.setStyle("-fx-border-color: #FF0000; -fx-border-width: 2;");
                }
            }

            event.setDropCompleted(success);

            // always reset after drop
            slotPane.setStyle("");

            event.consume();
        });
    }


    // Stat Calculation
    private void refreshStats() {
        var stats = statCalculator.calculate(character);

        lblTotalStr.setText(String.valueOf(stats.totalStr()));
        lblTotalDex.setText(String.valueOf(stats.totalDex()));
        lblTotalInt.setText(String.valueOf(stats.totalInt()));

        double progress = stats.currentWeight() / stats.maxWeight();
        progressWeight.setProgress(progress);

        lblWeightVal.setText(String.format("%.2f / %.2f kg", stats.currentWeight(), stats.maxWeight()));
    }

    // Helper Methods
    private void clearAllSlots() {
        imgHead.setVisible(false); slotHead.setUserData(null);
        imgBody.setVisible(false); slotBody.setUserData(null);
        imgMainHand.setVisible(false); slotMainHand.setUserData(null);
        imgOffHand.setVisible(false); slotOffHand.setUserData(null);
        imgLegs.setVisible(false); slotLegs.setUserData(null);
        imgAccessory.setVisible(false); slotAccessory.setUserData(null);
    }

    private ImageView getImageViewForSlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> imgHead;
            case BODY -> imgBody;
            case MAIN_HAND -> imgMainHand;
            case OFFHAND -> imgOffHand;
            case LEGS -> imgLegs;
            case ACCESSORY -> imgAccessory;
            default -> null;
        };
    }

    private void setImageFromPath(ImageView view, String path) {
        try {
            String fullPath = "/com/project/charforge/images/items/" + path;
            view.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(fullPath))));
        } catch (Exception e) {
            // Placeholder Image
             view.setImage(new Image("com/project/charforge/images/items/placeholder.png"));
        }
    }

    // Inventory Setup
    @FXML
    public void initialize() {
        setupAutoHideLabel(slotHead, imgHead);
        setupAutoHideLabel(slotBody, imgBody);
        setupAutoHideLabel(slotMainHand, imgMainHand);
        setupAutoHideLabel(slotOffHand, imgOffHand);
        setupAutoHideLabel(slotLegs, imgLegs);
        setupAutoHideLabel(slotAccessory, imgAccessory);

        // Unequip Logic
        inventoryGrid.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);

                // Visual feedback
                inventoryGrid.setStyle(
                        "-fx-border-color: #00ff00;" +
                                "-fx-border-width: 2;" +
                                "-fx-background-color: rgba(0,255,0,0.1);"
                );
            }
            event.consume();
        });

        inventoryGrid.setOnDragExited(event -> {
            inventoryGrid.setStyle("");
            event.consume();
        });

        inventoryGrid.setOnDragDropped(event -> {
            boolean success = false;

            // Calls unequip method
            if (event.getDragboard().hasString()) {
                try {
                    int instanceId = Integer.parseInt(event.getDragboard().getString());

                    equipmentService.unequip(character, instanceId);

                    reloadInventory();
                    refreshStats();
                    success = true;

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            event.setDropCompleted(success);
            inventoryGrid.setStyle("");
            event.consume();
        });

        setupSlotEvents(slotHead, EquipmentSlot.HEAD);
        setupSlotEvents(slotBody, EquipmentSlot.BODY);
        setupSlotEvents(slotMainHand, EquipmentSlot.MAIN_HAND);
        setupSlotEvents(slotOffHand, EquipmentSlot.OFFHAND);
        setupSlotEvents(slotLegs, EquipmentSlot.LEGS);
        setupSlotEvents(slotAccessory, EquipmentSlot.ACCESSORY);
    }

    private void setupAutoHideLabel(StackPane slot, ImageView img) {
        for (Node node : slot.getChildren()) {
            if (node instanceof Label) {
                node.visibleProperty().bind(img.visibleProperty().not());
            }
        }
    }

    @FXML
    public void handleReturnToMenu() {
        navigationService.goToMainMenu();
    }
}
