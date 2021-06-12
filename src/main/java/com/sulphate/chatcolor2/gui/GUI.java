package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GUI {

    private final String title;
    private final int size;
    private final Map<Integer, GUIItem> items;

    private ItemStack colourUnavailable;
    private ItemStack modifierUnavailable;
    private List<String> colourActive;
    private List<String> colourInactive;
    private ItemStack modifierActive;
    private ItemStack modifierInactive;

    public GUI(ConfigurationSection config) {
        items = new HashMap<>();

        title = config.getString("title");
        size = config.getInt("size");

        try {
            if (config.contains("color-unavailable")) {
                colourUnavailable = new ItemStack(Material.getMaterial(config.getString("color-unavailable.material")));
                InventoryUtils.setLore(colourUnavailable, config.getStringList("color-unavailable.lore"));
            }
            else {
                colourUnavailable = GUIUtils.DEFAULT_COLOUR_UNAVAILABLE;
            }

            if (config.contains("modifier-unavailable")) {
                modifierUnavailable = new ItemStack(Material.getMaterial(config.getString("modifier-unavailable.material")));
                InventoryUtils.setLore(colourUnavailable, config.getStringList("modifier-unavailable.lore"));
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
                modifierInactive = new ItemStack(Material.getMaterial(config.getString("modifier-inactive.material")));
                InventoryUtils.setLore(modifierInactive, config.getStringList("modifier-inactive.lore"));
            }
            else {
                modifierInactive = GUIUtils.DEFAULT_MODIFIER_INACTIVE;
            }

            if (config.contains("modifier-active")) {
                modifierActive = new ItemStack(Material.getMaterial(config.getString("modifier-active.material")));
                InventoryUtils.setLore(modifierActive, config.getStringList("modifier-active.lore"));
            }
        }
        catch (Exception ex) {
            colourUnavailable = null;
            modifierUnavailable = null;
            colourActive = null;
            colourInactive = null;

            GeneralUtils.sendConsoleMessage("&6[ChatColor] &cError while parsing GUI " + title + " (not in the items section), please check config format & values are correct.");
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
                            ItemStack item = new ItemStack(Material.getMaterial(itemSection.getString("material")));
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
                }
            }
        }
    }

    void open(Player player) {
        // TODO
    }

    void onClick(Player player, ItemStack item, int slot) {
        // TODO
    }

}
