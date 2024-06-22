package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.data.PlayerData;
import com.sulphate.chatcolor2.exception.InvalidGuiException;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.gui.item.*;
import com.sulphate.chatcolor2.gui.item.impl.ColourItem;
import com.sulphate.chatcolor2.gui.item.impl.InventoryItem;
import com.sulphate.chatcolor2.gui.item.impl.ModifierItem;
import com.sulphate.chatcolor2.gui.item.impl.SimpleGuiItem;
import com.sulphate.chatcolor2.utils.CompatabilityUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
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
            "&r",
            null,
            null
    );
    private static ItemStackTemplate noPermissionItemTemplate = null;
    private static Sound selectSound = null;
    private static Sound errorSound = null;

    private final GuiManager guiManager;
    private final GeneralUtils generalUtils;
    private final CustomColoursManager customColoursManager;
    private final Messages M;

    private final Map<Integer, GuiItem> items;
    private final String name;
    private final String title;
    private final int size;
    private final boolean fillEmptySlots;
    private final Player owner;
    private final PlayerData playerData;

    public Gui(String name, ConfigurationSection section, Player owner, PlayerData playerData, GuiManager guiManager, GeneralUtils generalUtils, CustomColoursManager customColoursManager, Messages M) {
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
        this.M = M;
        this.name = name;
        this.owner = owner;
        this.playerData = playerData;

        if (section.contains("fill-empty-slots")) {
            fillEmptySlots = section.getBoolean("fill-empty-slots");
        }
        else {
            fillEmptySlots = false;
        }

        title = GeneralUtils.colourise(section.getString("title"));
        size = section.getInt("size");
        items = parseItems(section.getConfigurationSection("items"), owner, playerData);
    }

    private Map<Integer, GuiItem> parseItems(ConfigurationSection section, Player player, PlayerData playerData) {
        if (section == null) {
            return new HashMap<>();
        }

        Map<Integer, GuiItem> items = new HashMap<>();
        Set<String> itemKeys = section.getKeys(false);
        boolean sendLegacyHexWarning = false;

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
                List<String> noPermissionLore = itemSection.getStringList("no-permission-lore");

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

                    if (CompatabilityUtils.isHexLegacy()) {
                        String colour = GeneralUtils.isCustomColour(data) ? customColoursManager.getCustomColour(data) : data;

                        if (GeneralUtils.containsHexColour(colour, false)) {
                            sendLegacyHexWarning = true;
                            continue;
                        }
                    }

                    ItemStackTemplate itemTemplate = ItemStackTemplate.fromConfigSection(itemSection);

                    // Default display name is auto-generated, but allow them to override it if they want.
                    if (itemTemplate.getDisplayName() == null) {
                        itemTemplate.setDisplayName(getColourName(data));
                    }

                    item = new ColourItem(data, itemTemplate, playerData, noPermissionLore);
                }
                else {
                    item = new ModifierItem(data, String.format("&%s%s", data, generalUtils.getModifierName(data)), playerData, noPermissionLore);
                }
            }

            if (item instanceof PermissibleItem) {
                PermissibleItem permissible = (PermissibleItem) item;
                permissible.checkPermission(player);

                // If permissible, the no-permissions template is null, and the player has no permission, skip the item.
                if (noPermissionItemTemplate == null && !permissible.hasPermission()) {
                    continue;
                }
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

        if (sendLegacyHexWarning) {
            GeneralUtils.sendConsoleMessage("Warning: Hex colours found in GUI; these will only show on MC versions 1.16+!");
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

    public void open() {
        Inventory inventory = Bukkit.createInventory(owner, size, title);

        for (Map.Entry<Integer, GuiItem> item : items.entrySet()) {
            inventory.setItem(item.getKey(), item.getValue().buildItem());
        }

        owner.openInventory(inventory);
    }

    // Performs an interaction within the GUI, updating the passed inventory with any effects of the interaction.
    public void onInteract(int slot, Inventory inventory) {
        // This means they clicked outside of the actual GUI.
        if (slot >= size) {
            return;
        }

        GuiItem clicked = items.get(slot);

        if (clicked instanceof SelectableItem) {
            SelectableItem selectable = (SelectableItem) clicked;

            if (doPreSelectChecks(selectable, slot, inventory)) {
                if (selectable.select()) {
                    playSound(selectSound);
                    owner.sendMessage(M.PREFIX + generalUtils.colourSetMessage(M.SET_OWN_COLOR, playerData.getColour()));
                }
                else {
                    playSound(errorSound);
                    owner.sendMessage(M.PREFIX + "An error occurred whilst selecting that color.");
                }
            }
        }
        else if (clicked instanceof ClickableItem) {
            ((ClickableItem) clicked).click();
        }

        inventory.setItem(slot, clicked.buildItem());
    }

    // Returns true if it should select, false if not.
    private boolean doPreSelectChecks(SelectableItem item, int slot, Inventory inventory) {
        String displayName = ((GuiItem) item).buildItem().getItemMeta().getDisplayName();

        if (item instanceof ColourItem) {
            if (!((PermissibleItem) item).hasPermission()) {
                owner.sendMessage(M.PREFIX + M.NO_COLOR_PERMS.replace("[color]", displayName));
                playSound(errorSound);
                return false;
            }

            unselectCurrentColour(slot, inventory);
        }
        else if (item instanceof ModifierItem) {
            if (!canModifyColour()) {
                owner.sendMessage(M.PREFIX + M.CANNOT_MODIFY_CUSTOM_COLOR);
                playSound(errorSound);
                return false;
            }
            else if (!((PermissibleItem) item).hasPermission()) {
                owner.sendMessage(M.PREFIX + M.NO_MOD_PERMS.replace("[modifier]", displayName));
                playSound(errorSound);
                return false;
            }

            if (item.isSelected()) {
                item.unselect();
                playSound(selectSound);
                return false;
            }
        }

        return true;
    }

    private void playSound(Sound sound) {
        if (sound != null) {
            owner.playSound(owner.getLocation(), sound, 0.5f, 1.0f);
        }
    }

    private void unselectCurrentColour(int interactedSlot, Inventory inventory) {
        Optional<Map.Entry<Integer, GuiItem>> selectedOptional = getSelectedColourItemEntry();

        // Deselect any currently selected colour item.
        selectedOptional.ifPresent(selectedEntry -> {
            // Only if it's not the current selection.
            if (selectedEntry.getKey() != interactedSlot) {
                ((SelectableItem) selectedEntry.getValue()).unselect();
                inventory.setItem(selectedEntry.getKey(), selectedEntry.getValue().buildItem());
            }
        });
    }

    private boolean canModifyColour() {
        return !customColoursManager.hasCustomColour(playerData.getColourName());
    }

    private Optional<Map.Entry<Integer, GuiItem>> getSelectedColourItemEntry() {
        return items.entrySet().stream()
                .filter(i -> i.getValue() instanceof ColourItem)
                .filter(i -> ((SelectableItem) i.getValue()).isSelected())
                .findFirst();
    }

    public static void setFillerItemMaterial(Material material) {
        fillerItemTemplate = new ItemStackTemplate(
                material,
                "&r",
                null,
                null
        );
    }

    public static void setSelectSound(Sound sound) {
        Gui.selectSound = sound;
    }

    public static void setErrorSound(Sound sound) {
        Gui.errorSound = sound;
    }

    public static void setNoPermissionItemTemplate(ItemStackTemplate template) {
        Gui.noPermissionItemTemplate = template;
    }

    public static ItemStackTemplate getNoPermissionItemTemplate() {
        return Gui.noPermissionItemTemplate;
    }

}
