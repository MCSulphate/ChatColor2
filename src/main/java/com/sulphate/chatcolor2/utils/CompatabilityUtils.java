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

    public CompatabilityUtils() {
        // Parse minor version to check for hex compatability.
        String version = Bukkit.getVersion();
        int dotIndex = version.indexOf('.');
        int minorVersion = Integer.parseInt(version.substring(dotIndex + 1, version.indexOf('.', dotIndex + 1)));

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

        CommandSender console = Bukkit.getConsoleSender();

        boolean isLightColour = materialName.startsWith("LIGHT");
        int underscoreIndex = materialName.indexOf('_');

        String colourName = materialName.substring(0, isLightColour ? materialName.indexOf('_', underscoreIndex + 1) : underscoreIndex);
        Material legacyMaterial = getLegacyMaterial(materialName);
        Short legacyColourData = materialName.contains("DYE") ? dyeColourToDataMap.get(colourName) : blockColourToDataMap.get(colourName);

        if (legacyMaterial == null) {
            console.sendMessage(GeneralUtils.colourise("&cError: Failed to resolve legacy material: " + materialName));
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
        else if (materialName.contains("GLASS_PANE")) {
            return Material.getMaterial("THIN_GLASS");
        }
        else if (materialName.equals("INK_SAC")) {
            return Material.getMaterial("INK_SACK");
        }
        else {
            return null;
        }
    }

    public static boolean isMaterialLegacy() {
        return isMaterialLegacy;
    }

    public static boolean isHexLegacy() {
        return isHexLegacy;
    }

}
