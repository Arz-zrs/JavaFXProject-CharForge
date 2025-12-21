package com.project.charforge.utils;

import com.project.charforge.model.entity.item.Item;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public final class ItemToolTipFactory {
    private static final double TOOLTIP_SHOW_DELAY = 100.0;
    private static final double TOOLTIP_VISIBILITY_DURATION = 10.0;

    public static void install(Node node, Item item) {
        Tooltip tooltip = new Tooltip(buildText(item));
        tooltip.setShowDelay(Duration.millis(TOOLTIP_SHOW_DELAY));
        tooltip.setShowDuration(Duration.seconds(TOOLTIP_VISIBILITY_DURATION));
        Tooltip.install(node, tooltip);
    }

    private static String buildText(Item item) {
        StringBuilder sb = new StringBuilder();

        sb.append(item.getName().toUpperCase()).append("\n");
        sb.append(item.getValidSlot().name().replace("_", " ")).append("\n\n");

        if (item.getHpBonus() != 0) sb.append("HP: +").append(item.getHpBonus()).append("\n");
        if (item.getAtkBonus() != 0) sb.append("ATK: +").append(item.getAtkBonus()).append("\n");
        if (item.getApBonus() != 0) sb.append("AP: +").append(item.getApBonus()).append("\n");

        if (item.getStrBonus() != 0)
            sb.append("STR: ").append(format(item.getStrBonus())).append("\n");
        if (item.getDexBonus() != 0)
            sb.append("DEX: ").append(format(item.getDexBonus())).append("\n");
        if (item.getIntBonus() != 0)
            sb.append("INT: ").append(format(item.getIntBonus())).append("\n");

        sb.append("\nWeight: ").append(item.getWeight()).append(" kg");

        return sb.toString();
    }

    private static String format(int value) {
        return (value > 0 ? "+" : "") + value;
    }
}
