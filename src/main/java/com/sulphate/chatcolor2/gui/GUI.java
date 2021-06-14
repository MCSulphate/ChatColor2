package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.commands.ChatColorCommand;
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

    public GUI(GUIManager manager, String guiName, ConfigurationSection config, ConfigUtils configUtils, Messages M) {
        this.manager = manager;
        this.configUtils = configUtils;
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

                    switch (type) {
                        case INVENTORY:
                        case COLOR: {
                            ItemStack item;

                            if (CompatabilityUtils.isHexLegacy() && GeneralUtils.isValidHexColour(itemSection.getString("data"))) {
                                item = hexColorsNotSupported.clone();
                            }
                            else if (CompatabilityUtils.isMaterialLegacy()) {
                                item = CompatabilityUtils.getColouredItem(itemSection.getString("material"));
                            }
                            else {
                                item = new ItemStack(Material.getMaterial(itemSection.getString("material")));
                            }

                            InventoryUtils.setDisplayName(item, itemSection.getString("name"));

                            items.put(inventorySlot, new GUIItem(type, itemSection.getString("data"), item));
                            continue;
                        }

                        case MODIFIER: {
                            // For modifiers, just start with the inactive item.
                            ItemStack item = modifierInactive.clone();
                            InventoryUtils.setDisplayName(item, itemSection.getString("name"));

                            items.put(inventorySlot, new GUIItem(type, itemSection.getString("data"), item));
                            continue;
                        }

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

    boolean loaded() {
        return colourUnavailable != null;
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

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case MODIFIER: {
                        ItemStack inventoryItem = item.getItem().clone();

                        // If it's active, clone the active item and set the display name.
                        if (modifierParts.contains(item.getData())) {
                            ItemStack original = inventoryItem;
                            inventoryItem = modifierActive.clone();

                            InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());
                        }

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case INVENTORY:
                        inventory.setItem(slot, item.getItem());
                        break;
                }
            }
            else {
                switch (item.getType()) {
                    case COLOR: {
                        ItemStack original = item.getItem();
                        ItemStack inventoryItem = colourUnavailable.clone();
                        InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }

                    case MODIFIER: {
                        ItemStack original = item.getItem();
                        ItemStack inventoryItem = modifierUnavailable.clone();
                        InventoryUtils.setDisplayName(inventoryItem, original.getItemMeta().getDisplayName());

                        inventory.setItem(slot, inventoryItem);
                        break;
                    }
                }
            }
        }

        player.openInventory(inventory);
    }

    void onClick(Player player, ItemStack item, int slot) {
        GUIItem clicked = items.get(slot);

        if (clicked != null) {
            ItemType type = clicked.getType();
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
                        List<String> clickedLore = item.getItemMeta().getLore();

                        if (clickedLore.equals(GUIUtils.colouriseList(colourActive))) {
                            player.sendMessage(M.PREFIX + M.GUI_COLOR_ALREADY_SET);
                        }
                        else {
                            parts[0] = clicked.getData();
                            ChatColorCommand.setColorFromArgs(uuid, parts, configUtils);

                            player.sendMessage(M.PREFIX + GeneralUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid), configUtils, M));
                            manager.openGUI(player, guiName); // Refresh GUI.
                        }
                    }

                    break;
                }

                case MODIFIER: {
                    Material clickedMaterial = item.getType();

                    // Have to check lore here for compatability with older versions (dyes are the same material in legacy).
                    if (GUIUtils.colouriseList(modifierUnavailable.getItemMeta().getLore()).equals(item.getItemMeta().getLore())) {
                        player.sendMessage(M.PREFIX + M.NO_MOD_PERMS.replace("[modifier]", item.getItemMeta().getDisplayName()));
                    }
                    else {
                        List<String> clickedLore = item.getItemMeta().getLore();

                        if (GUIUtils.colouriseList(modifierActive.getItemMeta().getLore()).equals(clickedLore)) {
                            modifierParts.remove(clicked.getData());
                        }
                        else {
                            modifierParts.add(clicked.getData());
                        }

                        modifierParts.add(0, colourPart);
                        ChatColorCommand.setColorFromArgs(uuid, modifierParts.toArray(new String[] {}), configUtils);

                        player.sendMessage(M.PREFIX + GeneralUtils.colourSetMessage(M.SET_OWN_COLOR, configUtils.getColour(uuid), configUtils, M));
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

}
