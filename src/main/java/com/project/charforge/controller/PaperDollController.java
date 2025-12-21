package com.project.charforge.controller;

import com.project.charforge.model.dto.DerivedStat;
import com.project.charforge.model.entity.character.Gender;
import com.project.charforge.model.entity.character.PlayerCharacter;
import com.project.charforge.model.entity.inventory.InventoryItem;
import com.project.charforge.model.entity.item.EquipmentSlot;
import com.project.charforge.service.interfaces.items.IEquipmentService;
import com.project.charforge.service.interfaces.stats.IStatCalculator;
import com.project.charforge.service.interfaces.process.IMessageService;
import com.project.charforge.service.interfaces.process.INavigationService;
import com.project.charforge.utils.ItemToolTipFactory;
import com.project.charforge.utils.Logs;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Objects;

public class PaperDollController {

    @FXML private ImageView imgSilhouette;
    @FXML private Label lblName, lblRaceClass ;
    @FXML private Label lblTotalStr, lblTotalDex, lblTotalInt, lblWeightVal;
    @FXML private Label lblHp, lblAp, lblAtk;
    @FXML private ProgressBar progressWeight;
    @FXML private GridPane inventoryGrid;

    @FXML private StackPane slotHead, slotAccessory, slotBody, slotMainHand, slotOffHand, slotLegs;
    @FXML private ImageView imgHead, imgAccessory, imgBody, imgMainHand, imgOffHand, imgLegs;

    private static final double INV_SLOT_SIZE = 50.0;
    private static final double ICON_SIZE = 40.0;
    private static final double PROGRESS_BAR_CAP = 1.0;
    private static final int GRID_SIZE = 10;

    private PlayerCharacter character;

    private IEquipmentService equipmentService;
    private IStatCalculator statCalculator;
    private INavigationService navigationService;
    private IMessageService message;

    public void injectServices(
            IEquipmentService equipmentService,
            IStatCalculator statCalculator,
            INavigationService navigationService,
            IMessageService message
    ) {
        this.equipmentService = equipmentService;
        this.statCalculator = statCalculator;
        this.navigationService = navigationService;
        this.message = message;
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
            ItemToolTipFactory.install(target, item.getItem());
        }
    }

    private void renderInventoryItem(InventoryItem inventoryItem) {
        StackPane pane = new StackPane();
        pane.setPrefSize(INV_SLOT_SIZE, INV_SLOT_SIZE);
        pane.getStyleClass().add("item-slot");
        pane.setUserData(inventoryItem);

        ImageView icon = new ImageView();
        icon.setFitWidth(ICON_SIZE);
        icon.setFitHeight(ICON_SIZE);
        setImageFromPath(icon, inventoryItem.getItem().getIconPath());

        pane.getChildren().add(icon);
        enableDragSource(pane, inventoryItem);

        ItemToolTipFactory.install(pane, inventoryItem.getItem());

        inventoryGrid.add(pane, inventoryItem.getGridIndex() % GRID_SIZE, inventoryItem.getGridIndex() / GRID_SIZE);
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
                        if (!slotPane.getStyleClass().contains("equipment-slot-valid")) {
                            slotPane.getStyleClass().add("equipment-slot-valid");
                        }
                        slotPane.getStyleClass().remove("equipment-slot-invalid");
                    } else {
                        if (!slotPane.getStyleClass().contains("equipment-slot-invalid")) {
                            slotPane.getStyleClass().add("equipment-slot-invalid");
                        }
                        slotPane.getStyleClass().remove("equipment-slot-valid");
                    }
                } catch (NumberFormatException ignore) {}
            }
            event.consume();
        });

        // Style removal during drag exit
        slotPane.setOnDragExited(event -> {
            slotPane.getStyleClass().removeAll("equipment-slot-valid", "equipment-slot-invalid");
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
                    message.error("Error", e.getMessage());
                    Logs.printError("PaperDollController Error", e);
                }
            }
            event.setDropCompleted(success);

            // Clean up styles
            slotPane.getStyleClass().removeAll("equipment-slot-valid", "equipment-slot-invalid");
            event.consume();
        });
    }


    // Stat Calculation
    private void refreshStats() {
        var snap = statCalculator.calculate(character);

        lblHp.setText(formatStat(snap.hp()));
        lblAtk.setText(formatStat(snap.atk()));
        lblAp.setText(formatStat(snap.ap()));

        lblTotalStr.setText(formatStat(snap.str()));
        lblTotalDex.setText(formatStat(snap.dex()));
        lblTotalInt.setText(formatStat(snap.ints()));

        double ratio = snap.currentWeight() / snap.maxWeight();
        progressWeight.setProgress(Math.min(PROGRESS_BAR_CAP, ratio));

        lblWeightVal.setText(
                String.format("%.2f / %.2f kg",
                        snap.currentWeight(),
                        snap.maxWeight())
        );

        progressWeight.getStyleClass().remove("weight-bar-encumbered");

        if (snap.isEncumbered()) {
            progressWeight.getStyleClass().add("weight-bar-encumbered");
        }
    }

    private String formatStat(DerivedStat stat) {
        return String.format(
                "%d (%d%s)",
                stat.total(),
                stat.base(),
                stat.bonus() != 0 ? ", +" + stat.bonus() : ""
        );
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
                if (!inventoryGrid.getStyleClass().contains("equipment-slot-valid")) {
                    inventoryGrid.getStyleClass().add("equipment-slot-valid");
                }
            }
            event.consume();
        });

        // Reset style
        inventoryGrid.setOnDragExited(event -> {
            inventoryGrid.getStyleClass().remove("equipment-slot-valid");
            event.consume();
        });

        // Successful drop
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
                    message.error("Error", e.getMessage());
                    Logs.printError("PaperDollController", e);
                }
            }

            event.setDropCompleted(success);
            inventoryGrid.getStyleClass().remove("equipment-slot-valid");
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
