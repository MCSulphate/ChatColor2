package com.sulphate.chatcolor2.gui.item.impl;

import com.sulphate.chatcolor2.data.PlayerData;
import com.sulphate.chatcolor2.gui.Gui;
import com.sulphate.chatcolor2.gui.GuiManager;
import com.sulphate.chatcolor2.gui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.gui.item.ComplexGuiItem;
import com.sulphate.chatcolor2.gui.item.PermissibleItem;
import com.sulphate.chatcolor2.gui.item.SelectableItem;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import com.sulphate.chatcolor2.utils.StaticMaps;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ColourItem extends ComplexGuiItem implements PermissibleItem, SelectableItem {

    private static String selectedText = "Selected";
    private static String unselectedText = "Unselected";

    private final GeneralUtils generalUtils;

    private final PlayerData playerData;
    private final String permission;
    private final List<String> noPermissionLore;
    private boolean hasPermission = false;
    private boolean selected;

    public ColourItem(String colour, ItemStackTemplate itemTemplate, PlayerData playerData, List<String> noPermissionLore, GeneralUtils generalUtils) {
        super(colour, itemTemplate);
        this.generalUtils = generalUtils;

        if (colour.equals("default")) {
            permission = "chatcolor.use";
        }
        else if (colour.startsWith("%")) {
            permission = "chatcolor.custom." + colour.substring(1);
        }
        else if (colour.startsWith("#")) {
            permission = "chatcolor.color." + colour.substring(1).toLowerCase();
        }
        else if (colour.startsWith("u") || colour.startsWith("g")) {
            permission = "chatcolor.special";
        }
        else {
            permission = "chatcolor.color." + colour;
        }

        selected = playerData.getColourName().equals(colour);
        this.playerData = playerData;
        this.noPermissionLore = noPermissionLore;
    }

    @Override
    public ItemStack buildItem() {
        ItemStack item;

        if (!hasPermission) {
            if (Gui.getNoPermissionItemTemplate() != null) {
                item = Gui.getNoPermissionItemTemplate().build(1);

                if (GuiManager.shouldCopyNoPermissionItemMaterial()) {
                    List<String> lore = InventoryUtils.getLore(item);

                    item = itemTemplate.build(1);
                    InventoryUtils.setLore(item, lore);
                }

                if (!noPermissionLore.isEmpty()) {
                    InventoryUtils.setLore(item, noPermissionLore);
                }
            }
            else {
                return null;
            }
        }
        else {
            item = itemTemplate.build(1);
            List<String> lore = InventoryUtils.getLore(item);

            lore.add("");

            if (selected) {
                InventoryUtils.addFakeEnchantment(item);
                lore.add(selectedText);
            }
            else {
                lore.add(unselectedText);
            }

            InventoryUtils.setLore(item, lore);
        }

        InventoryUtils.setDisplayName(item, itemTemplate.getDisplayName());

        return item;
    }

    @Override
    public void checkPermission(Player player) {
        if (data.startsWith("#")) {
            hasPermission = player.hasPermission(permission) || player.hasPermission("chatcolor.use-hex-codes");
        }
        else {
            hasPermission = player.hasPermission(permission) || player.hasPermission(StaticMaps.getVerbosePermission(permission));
        }
    }

    @Override
    public boolean hasPermission() {
        return hasPermission;
    }

    @Override
    public boolean select() {
        if (!selected) {
            if (!hasPermission) {
                return false;
            }

            selected = true;

            // Unless it's a custom colour or the default, just set the name.
            // e.g., a, b, c, #123456
            if (data.startsWith("%")) {
                playerData.setColour(data);
            }
            else if (data.equals("default")) {
                playerData.setColour(generalUtils.getDefaultColourForPlayer(playerData.getUuid()));
            }
            else {
                playerData.setColourName(data);
            }
        }

        return true;
    }

    @Override
    public void unselect() {
        selected = false;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public static void setSelectedText(String selectedText) {
        ColourItem.selectedText = selectedText;
    }

    public static void setUnselectedText(String unselectedText) {
        ColourItem.unselectedText = unselectedText;
    }

}
