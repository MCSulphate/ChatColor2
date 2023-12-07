package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GUI {

    private final GUIManager manager;
    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;
    private final Messages M;

    private final String guiName;
    private final String title;
    private final int size;
    private final Map<Integer, GUIItem> items;

    private ItemStack colourUnavailable;
    private ItemStack modifierUnavailable;
    private List<String> colourActive;
    private List<String> colourInactive;
    private ItemStack modifierActive;
    private ItemStack modifierInactive;
    private ItemStack hexColorsNotSupported;

    public GUI(GUIManager manager, String guiName, ConfigurationSection config, ConfigUtils configUtils, GeneralUtils generalUtils, Messages M) {
        this.manager = manager;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
        this.M = M;
        this.guiName = guiName;

        items = new HashMap<>();
        title = GeneralUtils.colourise(config.getString("title"));
        size = config.getInt("size");

        try {
            if (config.contains("color-unavailable")) {
                if (CompatabilityUtils.isMaterialLegacy()) {
                    colourUnavailable = CompatabilityUtils.getColouredItem(config.getString("color-unavailable.material"));
                }
                else {
                    colourUnavailable = new ItemStack(Material.getMaterial(config.getString("color-unavailable.material")));
                }

                InventoryUtils.setLore(colourUnavailable, config.getStringList("color-unavailable.lore"));
            }
            else {
                colourUnavailable = GUIUtils.DEFAULT_COLOUR_UNAVAILABLE;
            }

            if (config.contains("modifier-unavailable")) {
                if (CompatabilityUtils.isMaterialLegacy()) {
                    modifierUnavailable = CompatabilityUtils.getColouredItem(config.getString("modifier-unavailable.material"));
                }
                else {
                    modifierUnavailable = new ItemStack(Material.getMaterial(config.getString("modifier-unavailable.material")));
                }

                InventoryUtils.setLore(modifierUnavailable, config.getStringList("modifier-unavailable.lore"));
            }
            else {
                modifierUnavailable = GUIUtils.DEFAULT_MODIFIER_UNAVAILABLE;
            }

            if (config.contains("color-active")) {
                colourActive = config.getStringList("color-active.lore");
            }
            else {
                colourActive = GUIUtils.DEFAULT_COLOUR_ACTIVE;
            }

            if (config.contains("color-inactive")) {
                colourInactive = config.getStringList("color-inactive.lore");
            }
            else {
                colourInactive = GUIUtils.DEFAULT_COLOUR_INACTIVE;
            }

            if (config.contains("modifier-inactive")) {
                if (CompatabilityUtils.isMaterialLegacy()) {
                    modifierInactive = CompatabilityUtils.getColouredItem(config.getString("modifier-inactive.material"));
                }
                else {
                    modifierInactive = new ItemStack(Material.getMaterial(config.getString("modifier-inactive.material")));
                }

                InventoryUtils.setLore(modifierInactive, config.getStringList("modifier-inactive.lore"));
            }
            else {
                modifierInactive = GUIUtils.DEFAULT_MODIFIER_INACTIVE;
            }

            if (config.contains("modifier-active")) {
                if (CompatabilityUtils.isMaterialLegacy()) {
                    modifierActive = CompatabilityUtils.getColouredItem(config.getString("modifier-active.material"));
                }
                else {
                    modifierActive = new ItemStack(Material.getMaterial(config.getString("modifier-active.material")));
                }

                InventoryUtils.setLore(modifierActive, config.getStringList("modifier-active.lore"));
            }
            else {
                modifierActive = GUIUtils.DEFAULT_MODIFIER_ACTIVE;
            }

            if (config.contains("hex-colors-not-supported")) {
                if (CompatabilityUtils.isMaterialLegacy()) {
                    hexColorsNotSupported = CompatabilityUtils.getColouredItem(config.getString("hex-colors-not-supported.material"));
                }
                else {
                    hexColorsNotSupported = new ItemStack(Material.getMaterial(config.getString("hex-colors-not-supported.material")));
                }

                InventoryUtils.setLore(hexColorsNotSupported, config.getStringList("hex-colors-not-supported.lore"));
            }
            else {
                hexColorsNotSupported = GUIUtils.DEFAULT_HEX_COLORS_NOT_SUPPORTED;
            }
        }
        catch (Exception ex) {
            colourUnavailable = null;
            modifierUnavailable = null;
            colourActive = null;
            colourInactive = null;

            GeneralUtils.sendConsoleMessage("&6[ChatColor] &cError while parsing GUI " + title + " (not in the items section), please check config format & values are correct.");
            ex.printStackTrace();
        }

        ConfigurationSection itemsSection = config.getConfigurationSection("items");

        if (itemsSection == null) {
            GeneralUtils.sendConsoleMessage("&6[ChatColor] &eWarning: GUI " + title + " does not have any items defined in the config.");
        }
        else {
            Set<String> keys = itemsSection.getKeys(false);

            for (String key : keys) {
                try {
                    int inventorySlot = Integer.parseInt(key);
                    ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                    ItemType type = ItemType.valueOf(itemSection.getString("type"));
                    ItemStack item = null;
                    String name = null;

                    List<String> extraLore = itemSection.getStringList("lore");

                    switch (type) {
                        case FILLER:
                            // Blank name.
                            name = "&r";
                            extraLore = null;

                        case COLOR:
                            if (CompatabilityUtils.isHexLegacy() && GeneralUtils.isValidHexColour(itemSection.getString("data"))) {
                                item = hexColorsNotSupported.clone();
                            }

                        case INVENTORY:
                            if (CompatabilityUtils.isMaterialLegacy()) {
                                item = CompatabilityUtils.getColouredItem(itemSection.getString("material"));
                            }
                            // null check in case it hit the hex legacy block in COLOR (don't want to override).
                            else if (item == null) {
                                item = new ItemStack(Material.getMaterial(itemSection.getString("material")));
                            }

                            if (name == null) {
                                name = itemSection.getString("name");
                            }

                            InventoryUtils.setDisplayName(item, name);

                            items.put(inventorySlot, new GUIItem(type, itemSection.getString("data"), item, extraLore));
                            continue;

                        case MODIFIER:
                            // For modifiers, just start with the inactive item.
                            item = modifierInactive.clone();
                            InventoryUtils.setDisplayName(item, itemSection.getString("name"));

                            items.put(inventorySlot, new GUIItem(type, itemSection.getString("data"), item, extraLore));
                            continue;

                        default:
                            GeneralUtils.sendConsoleMessage("&6[ChatColor] &eWarning: Invalid item type for item " + key + " in GUI " + title + ", please check the config.");
                    }
                }
                catch (Exception ex) {
                    GeneralUtils.sendConsoleMessage("&6[ChatColor] &cError parsing item " + key + " in GUI " + title + ", please check the config.");
                    ex.printStackTrace();
                }
            }
        }
    }

    public int getSize() {
        return size;
    }

    boolean loaded() {
        return colourUnavailable != null;
    }

    private void updateExtraLore(GUIItem item, ItemStack inventoryItem) {
        if (item.hasExtraLore()) {
            List<String> lore = InventoryUtils.getLore(inventoryItem);
            lore.addAll(item.getExtraLore());
            InventoryUtils.setLore(inventoryItem, lore);
        }
    }

    void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        String chatColour = configUtils.getColour(player.getUniqueId());

        if (chatColour.startsWith("&")) {
            chatColour = chatColour.substring(1);
        }

        String[] parts = chatColour.split("&");
        String colourPart = parts[0];
        List<String> modifierParts = Arrays.asList(parts).subList(1, parts.length);

        for (Map.Entry<Integer, GUIItem> entry : items.entrySet()) {
            int slot = entry.getKey();
            GUIItem item = entry.getValue();

            if (GUIUtils.colouriseList(hexColorsNotSupported.getItemMeta().getLore()).equals(item.getItem().getItemMeta().getLore())) {
                inventory.setItem(slot, item.getItem());
            }
            else if (GUIUtils.checkPermission(player, item)) {
                switch (item.getType()) {
                    case COLOR: {
                        ItemStack inventoryItem = item.getItem().clone();

                        if (colourPart.equals(item.getData())) {
                            InventoryUtils.setLore(inventoryItem, colourActive);
                        }
                        else {
                            InventoryUtils.setLore(inventoryItem, colourInactive);
                        }

                        updateExtraLore(item, inventoryItem);
                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case MODIFIER: {
                        ItemStack inventoryItem = item.getItem().clone();

                        // If it's active, clone the active item and set the display name.
                        if (modifierParts.contains(item.getData())) {
                            ItemStack original = inventoryItem;
                            inventoryItem = modifierActive.clone();

                            updateExtraLore(item, inventoryItem);
                            InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());
                        }

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case FILLER:
                    case INVENTORY:
                        ItemStack inventoryItem = item.getItem().clone();
                        updateExtraLore(item, inventoryItem);
                        inventory.setItem(slot, inventoryItem);
                        break;
                }
            }
            else {
                switch (item.getType()) {
                    case COLOR: {
                        ItemStack original = item.getItem();
                        ItemStack inventoryItem = colourUnavailable.clone();

                        updateExtraLore(item, inventoryItem);
                        InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case MODIFIER: {
                        ItemStack original = item.getItem();
                        ItemStack inventoryItem = modifierUnavailable.clone();

                        updateExtraLore(item, inventoryItem);
                        InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }
                }
            }
        }

        player.openInventory(inventory);
    }

    private void removeExtraLore(List<String> lore, GUIItem item) {
        // Workaround for the extra lore, remove the same number of entries as extra lore
        // from the item's lore list.
        if (item.hasExtraLore()) {
            List<String> extraLore = item.getExtraLore();

            for (int i = 0; i < extraLore.size(); i++) {
                lore.remove(lore.size() - 1);
            }
        }
    }

    void onClick(Player player, ItemStack item, int slot) {
        if (item == null) {
            return;
        }

        GUIItem clicked = items.get(slot);

        if (clicked != null) {
            ItemType type = clicked.getType();

            if (type.equals(ItemType.FILLER)) {
                return;
            }

            String chatColour = configUtils.getColour(player.getUniqueId());

            if (chatColour.startsWith("&")) {
                chatColour = chatColour.substring(1);
            }

            String[] parts = chatColour.split("&");
            String colourPart = parts[0];
            List<String> modifierParts = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
            UUID uuid = player.getUniqueId();

            switch (type) {
                case COLOR: {
                    Material clickedMaterial = item.getType();

                    if (clickedMaterial.equals(colourUnavailable.getType())) {
                        player.sendMessage(M.PREFIX + M.NO_COLOR_PERMS.replace("[color]", item.getItemMeta().getDisplayName()));
                    }
                    else {
                        List<String> clickedLore = InventoryUtils.getLore(item);
                        removeExtraLore(clickedLore, clicked);

                        if (clickedLore.equals(GUIUtils.colouriseList(colourActive))) {
                            player.sendMessage(M.PREFIX + M.GUI_COLOR_ALREADY_SET);
                        }
                        else {
                            String colour;

                            if (GeneralUtils.isCustomColour(clicked.getData())) {
                                colour = colourFromParts(clicked.getData(), new ArrayList<>());
                            }
                            else {
                                colour = colourFromParts(clicked.getData(), modifierParts);
                            }

                            configUtils.setColour(uuid, colour);

                            player.sendMessage(M.PREFIX + generalUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid)));
                            manager.openGUI(player, guiName); // Refresh GUI.
                        }
                    }

                    break;
                }

                case MODIFIER: {
                    List<String> clickedLore = InventoryUtils.getLore(item);
                    removeExtraLore(clickedLore, clicked);

                    // Have to check lore here for compatability with older versions (dyes are the same material in legacy).
                    if (GUIUtils.colouriseList(InventoryUtils.getLore(modifierUnavailable)).equals(clickedLore)) {
                        player.sendMessage(M.PREFIX + M.NO_MOD_PERMS.replace("[modifier]", InventoryUtils.getDisplayName(item)));
                    }
                    else {
                        if (GeneralUtils.isCustomColour(configUtils.getColour(uuid))) {
                            player.sendMessage(M.PREFIX + M.CANNOT_MODIFY_CUSTOM_COLOR);
                            return;
                        }

                        if (GUIUtils.colouriseList(InventoryUtils.getLore(modifierActive)).equals(clickedLore)) {
                            modifierParts.remove(clicked.getData());
                        }
                        else {
                            modifierParts.add(clicked.getData());
                        }

                        String colour = colourFromParts(colourPart, modifierParts);
                        configUtils.setColour(uuid, colour);

                        player.sendMessage(M.PREFIX + generalUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid)));
                        manager.openGUI(player, guiName);
                    }

                    break;
                }

                case INVENTORY: {
                    manager.openGUI(player, clicked.getData());
                    break;
                }
            }
        }
    }

    private String colourFromParts(String colourPart, List<String> modifierParts) {
        StringBuilder builder = new StringBuilder();

        if (GeneralUtils.isCustomColour(colourPart)) {
            builder.append(colourPart);
        }
        else {
            builder.append('&').append(colourPart);
        }

        for (String modPart : modifierParts) {
            builder.append('&').append(modPart);
        }

        return builder.toString();
    }

}
