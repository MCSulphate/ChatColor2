package com.sulphate.chatcolor2.newgui.item.impl;

import com.sulphate.chatcolor2.data.PlayerData;
import com.sulphate.chatcolor2.newgui.Gui;
import com.sulphate.chatcolor2.newgui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.newgui.item.ComplexGuiItem;
import com.sulphate.chatcolor2.newgui.item.PermissibleItem;
import com.sulphate.chatcolor2.newgui.item.SelectableItem;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ColourItem extends ComplexGuiItem implements PermissibleItem, SelectableItem {

    private static String selectedText = "Selected";
    private static String unselectedText = "Unselected";

    private final PlayerData playerData;
    private final String permission;
    private boolean hasPermission = false;
    private boolean selected;

    public ColourItem(String colour, ItemStackTemplate itemTemplate, PlayerData playerData) {
        super(colour, itemTemplate);

        if (colour.startsWith("%")) {
            permission = "chatcolor.custom." + colour.substring(1);
        }
        else if (colour.startsWith("#")) {
            permission = "chatcolor.use-hex-codes";
        }
        else if (colour.startsWith("u") || colour.startsWith("g")) {
            permission = "chatcolor.special";
        }
        else {
            permission = "chatcolor.color." + colour;
        }

        selected = playerData.getColourName().equals(colour);
        this.playerData = playerData;
    }

    @Override
    public ItemStack buildItem() {
        ItemStack item;

        if (!hasPermission) {
            if (Gui.getNoPermissionItemTemplate() != null) {
                item = Gui.getNoPermissionItemTemplate().build(1);
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
        hasPermission = player.hasPermission(permission);
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

            // Unless it's a custom colour, just set the 'name' of the colour.
            // e.g., a, b, c, #123456
            if (data.startsWith("%")) {
                playerData.setColour(data);
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
