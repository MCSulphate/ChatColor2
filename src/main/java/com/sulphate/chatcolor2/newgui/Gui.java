package com.sulphate.chatcolor2.newgui;

import com.sulphate.chatcolor2.data.PlayerData;
import com.sulphate.chatcolor2.exception.InvalidGuiException;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.newgui.item.*;
import com.sulphate.chatcolor2.newgui.item.impl.ColourItem;
import com.sulphate.chatcolor2.newgui.item.impl.InventoryItem;
import com.sulphate.chatcolor2.newgui.item.impl.ModifierItem;
import com.sulphate.chatcolor2.newgui.item.impl.SimpleGuiItem;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.stream.Collectors;

public class Gui {

    private static final List<String> REQUIRED_GUI_KEYS = Arrays.asList(
            "title",
            "size",
            "items"
    );

    private static ItemStackTemplate fillerItemTemplate = new ItemStackTemplate(
            Material.GRAY_STAINED_GLASS_PANE,
            "",
            null
    );
    public static ItemStackTemplate noPermissionItemTemplate = null;

    private final GuiManager guiManager;
    private final GeneralUtils generalUtils;
    private final CustomColoursManager customColoursManager;

    private final Map<Integer, GuiItem> items;
    private final String name;
    private final String title;
    private final int size;
    private final boolean fillEmptySlots;
    private final Player owner;

    public Gui(String name, ConfigurationSection section, Player owner, PlayerData playerData, GuiManager guiManager, GeneralUtils generalUtils, CustomColoursManager customColoursManager) {
        if (section == null) {
            throw new InvalidGuiException(String.format("Invalid GUI %s.", name));
        }

        for (String requiredKey : REQUIRED_GUI_KEYS) {
            if (!section.contains(requiredKey)) {
                throw new InvalidGuiException(String.format("Missing required key %s in GUI %s.", requiredKey, name));
            }
        }

        this.guiManager = guiManager;
        this.generalUtils = generalUtils;
        this.customColoursManager = customColoursManager;
        this.name = name;
        this.owner = owner;

        title = GeneralUtils.colourise(section.getString("title"));
        size = section.getInt("size");
        items = parseItems(section.getConfigurationSection("items"), owner, playerData);

        if (section.contains("fill-empty-slots")) {
            fillEmptySlots = section.getBoolean("fill-empty-slots");
        }
        else {
            fillEmptySlots = false;
        }
    }

    private Map<Integer, GuiItem> parseItems(ConfigurationSection section, Player player, PlayerData playerData) {
        if (section == null) {
            return new HashMap<>();
        }

        Map<Integer, GuiItem> items = new HashMap<>();
        Set<String> itemKeys = section.getKeys(false);

        for (String itemKey : itemKeys) {
            int slot;

            try {
                slot = Integer.parseInt(itemKey);
            }
            catch (NumberFormatException ex) {
                throw new InvalidGuiException(String.format("Invalid item key %s in GUI %s, must be a number.", itemKey, name));
            }

            if (slot < 0 || slot > size - 1) {
                throw new InvalidGuiException(String.format("Invalid item key %s in GUI %s, must be between 0 and %d.", itemKey, name, size));
            }

            ConfigurationSection itemSection = section.getConfigurationSection(itemKey);

            if (itemSection == null) {
                throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, should be a config section.", itemKey, name));
            }

            if (!itemSection.contains("type")) {
                throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, missing 'type' config value.", itemKey, name));
            }

            String typeString = itemSection.getString("type");
            ItemType type;

            try {
                type = ItemType.valueOf(typeString);
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, invalid item type %s.", itemKey, name, typeString));
            }

            GuiItem item;

            if (type.equals(ItemType.FILLER)) {
                item = new SimpleGuiItem(fillerItemTemplate);
            }
            else {
                if (!itemSection.contains("data")) {
                    throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, missing 'data' config value.", itemKey, name));
                }

                String data = itemSection.getString("data");

                if (type.equals(ItemType.INVENTORY)) {
                    if (!itemSection.contains("name")) {
                        throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, missing 'name' config value.", itemKey, name));
                    }

                    if (!itemSection.contains("material")) {
                        throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, missing 'material' config value.", itemKey, name));
                    }

                    ItemStackTemplate itemTemplate = ItemStackTemplate.fromConfigSection(itemSection);

                    if (!guiManager.guiExists(data)) {
                        throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, targeting non-existent GUI %s", itemKey, name, data));
                    }

                    item = new InventoryItem(data, itemTemplate, owner, guiManager);
                }
                else if (type.equals(ItemType.COLOUR)) {
                    if (!itemSection.contains("material")) {
                        throw new InvalidGuiException(String.format("Invalid item %s in GUI %s, missing 'material' config value.", itemKey, name));
                    }

                    ItemStackTemplate itemTemplate = ItemStackTemplate.fromConfigSection(itemSection);

                    // Default display name is auto-generated, but allow them to override it if they want.
                    if (itemTemplate.getDisplayName() == null) {
                        itemTemplate.setDisplayName(getColourName(data));
                    }

                    item = new ColourItem(data, itemTemplate, playerData);
                }
                else {
                    item = new ModifierItem(data, String.format("&%s%s", data, generalUtils.getModifierName(data)), playerData);
                }
            }

            if (item instanceof PermissibleItem) {
                ((PermissibleItem) item).checkPermission(player);
            }

            items.put(slot, item);
        }

        if (fillEmptySlots) {
            for (int i = 0; i < size; i++) {
                if (!items.containsKey(i)) {
                    items.put(i, new SimpleGuiItem(fillerItemTemplate));
                }
            }
        }

        return items;
    }

    private String getColourName(String colour) {
        if (colour.startsWith("%")) {
            String customColourName = Arrays.stream(colour.substring(1).split("[^0-9a-zA-Z]"))
                    .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));

            return customColoursManager.getCustomColour(colour) + customColourName;
        }
        else {
            colour = '&' + colour;
            return String.format("%s%s", colour, generalUtils.getColorName(colour, true));
        }
    }

    // Note: Creating and opening a new inventory every time is not very efficient. Should be caching an inventory and
    // updating items where needed, ideally.
    public void open() {
        Inventory inventory = Bukkit.createInventory(owner, size, title);

        for (Map.Entry<Integer, GuiItem> item : items.entrySet()) {
            inventory.setItem(item.getKey(), item.getValue().buildItem());
        }

        owner.openInventory(inventory);
    }

    public GuiItem onInteract(int slot) {
        // This means they clicked outside of the actual GUI.
        if (slot >= size) {
            return null;
        }

        GuiItem clicked = items.get(slot);

        if (clicked instanceof SelectableItem) {
            ((SelectableItem) clicked).select();
        }
        else if (clicked instanceof ClickableItem) {
            ((ClickableItem) clicked).click();
        }

        return clicked;
    }

}
