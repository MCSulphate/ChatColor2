package com.sulphate.chatcolor2.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

// Utils for managing cross-api-version compatability.
public class CompatabilityUtils {

    private static boolean isMaterialLegacy;
    private static boolean isHexLegacy;
    private static HashMap<String, Short> blockColourToDataMap;
    private static HashMap<String, Short> dyeColourToDataMap;

    private CompatabilityUtils() {
        // Empty private constructor.
    }

    public static void init() {
        // Parse minor version to check for hex compatability.
        String version = Bukkit.getBukkitVersion();
        version = version.substring(0, version.indexOf('-'));

        int dotIndex = version.indexOf('.');
        float minorVersion = Float.parseFloat(version.substring(dotIndex + 1));

        isHexLegacy = minorVersion < 16;
        isMaterialLegacy = Material.getMaterial("INK_SAC") == null;

        blockColourToDataMap = new HashMap<>();
        dyeColourToDataMap = new HashMap<>();

        String[] blockColourNames = { "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE", "YELLOW", "LIME", "PINK", "GRAY", "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE", "BROWN", "GREEN", "RED", "BLACK" };
        for (int i = 0; i < blockColourNames.length; i++) {
            blockColourToDataMap.put(blockColourNames[i], (short) i);
        }

        String[] dyeColourNames = { "INK", "RED", "GREEN", "COCOA", "LAPIS", "PURPLE", "CYAN", "LIGHT_GRAY", "GRAY", "PINK", "LIME", "YELLOW", "LIGHT_BLUE", "MAGENTA", "ORANGE", "BONE", "BLACK", "BROWN", "BLUE", "WHITE" };
        for (int i = 0; i < dyeColourNames.length; i++) {
            dyeColourToDataMap.put(dyeColourNames[i], (short) i);
        }
    }

    public static ItemStack getColouredItem(String materialName) {
        if (!isMaterialLegacy) {
            return new ItemStack(Material.getMaterial(materialName), 1);
        }

        boolean isLightColour = materialName.startsWith("LIGHT");
        int underscoreIndex = materialName.indexOf('_');

        Material legacyMaterial = getLegacyMaterial(materialName);
        String colourName = underscoreIndex == -1 ? null : materialName.substring(0, isLightColour ? materialName.indexOf('_', underscoreIndex + 1) : underscoreIndex);
        Short legacyColourData = colourName == null ? null : materialName.contains("DYE") ? dyeColourToDataMap.get(colourName) : blockColourToDataMap.get(colourName);

        if (legacyMaterial == null) {
            GeneralUtils.sendConsoleMessage("&6[ChatColor] &cError: Failed to resolve legacy material: " + materialName);
            return new ItemStack(Material.AIR);
        }

        // If null, then it's not a coloured item.
        if (legacyColourData == null) {
            return new ItemStack(legacyMaterial, 1);
        }
        // Use legacy colour constructor.
        else {
            return new ItemStack(legacyMaterial, 1, legacyColourData);
        }
    }

    private static Material getLegacyMaterial(String materialName) {
        if (materialName.contains("DYE")) {
            return Material.getMaterial("INK_SACK");
        }
        else if (materialName.contains("STAINED_GLASS_PANE")) {
            return Material.getMaterial("STAINED_GLASS_PANE");
        }
        else if (materialName.contains("STAINED_GLASS")) {
            return Material.getMaterial("GLASS");
        }
        else if (materialName.contains("GLASS_PANE")) {
            return Material.getMaterial("THIN_GLASS");
        }
        else if (materialName.equals("INK_SAC")) {
            return Material.getMaterial("INK_SACK");
        }
        else {
            try {
                // Just return the material if there is no legacy (if possible).
                return Material.getMaterial(materialName);
            }
            catch (Exception ex) {
                return null;
            }
        }
    }

    public static boolean isMaterialLegacy() {
        return isMaterialLegacy;
    }

    public static boolean isHexLegacy() {
        return isHexLegacy;
    }

}
