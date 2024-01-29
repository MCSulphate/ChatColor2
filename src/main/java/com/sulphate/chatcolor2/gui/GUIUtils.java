package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.utils.CompatabilityUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GUIUtils {

    // Default items for GUIs that are missing them in the config.
    public static final ItemStack DEFAULT_COLOUR_UNAVAILABLE;
    public static final ItemStack DEFAULT_MODIFIER_UNAVAILABLE;
    public static final List<String> DEFAULT_COLOUR_ACTIVE;
    public static final List<String> DEFAULT_COLOUR_INACTIVE;
    public static final ItemStack DEFAULT_MODIFIER_ACTIVE;
    public static final ItemStack DEFAULT_MODIFIER_INACTIVE;
    public static final ItemStack DEFAULT_HEX_COLORS_NOT_SUPPORTED;

    static {
        DEFAULT_COLOUR_UNAVAILABLE = new ItemStack(Material.BARRIER);
        DEFAULT_MODIFIER_UNAVAILABLE = CompatabilityUtils.isMaterialLegacy() ? CompatabilityUtils.getColouredItem("RED_DYE") : new ItemStack(Material.RED_DYE);

        DEFAULT_COLOUR_ACTIVE = Collections.singletonList("&aSelected");
        DEFAULT_COLOUR_INACTIVE = Collections.singletonList("&eClick to Select");

        DEFAULT_MODIFIER_ACTIVE = CompatabilityUtils.isMaterialLegacy() ? CompatabilityUtils.getColouredItem("LIME_DYE") : new ItemStack(Material.LIME_DYE);
        DEFAULT_MODIFIER_INACTIVE = CompatabilityUtils.isMaterialLegacy() ? CompatabilityUtils.getColouredItem("GRAY_DYE") : new ItemStack(Material.GRAY_DYE);

        DEFAULT_HEX_COLORS_NOT_SUPPORTED = new ItemStack(Material.BARRIER);

        InventoryUtils.setLore(DEFAULT_COLOUR_UNAVAILABLE, Collections.singletonList("&cUnavailable"));
        InventoryUtils.setLore(DEFAULT_MODIFIER_UNAVAILABLE, Collections.singletonList("&cUnavailable"));
        InventoryUtils.setLore(DEFAULT_MODIFIER_ACTIVE, Collections.singletonList("&aActive"));
        InventoryUtils.setLore(DEFAULT_MODIFIER_INACTIVE, Arrays.asList("&7Inactive", "&eClick to Toggle"));
        InventoryUtils.setLore(DEFAULT_HEX_COLORS_NOT_SUPPORTED, Collections.singletonList("&cHex colors are only supported on 1.16+"));
    }

    public static boolean checkPermission(Player player, GUIItem item) {
        switch (item.getType()) {
            case COLOR:
                if (item.getData().startsWith("#")) {
                    return GeneralUtils.checkPermission(player, "chatcolor.use-hex-codes");
                }
                else if (item.getData().startsWith("%")) {
                    return GeneralUtils.checkPermission(player, "chatcolor.custom." + item.getData().replace("%", ""));
                }
                else {
                    return GeneralUtils.checkPermission(player, "chatcolor.color." + item.getData());
                }
            case MODIFIER:
                return GeneralUtils.checkPermission(player, "chatcolor.modifier." + item.getData());
            default:
                return true;
        }
    }

    public static List<String> colouriseList(List<String> toColourise) {
        List<String> copy = new ArrayList<>();

        for (String entry : toColourise) {
            copy.add(GeneralUtils.colourise(entry));
        }

        return copy;
    }

}
