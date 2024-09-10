package com.sulphate.chatcolor2.gui.item.impl;

import com.sulphate.chatcolor2.data.PlayerData;
import com.sulphate.chatcolor2.gui.Gui;
import com.sulphate.chatcolor2.gui.GuiManager;
import com.sulphate.chatcolor2.gui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.gui.item.ComplexGuiItem;
import com.sulphate.chatcolor2.gui.item.PermissibleItem;
import com.sulphate.chatcolor2.gui.item.SelectableItem;
import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ModifierItem extends ComplexGuiItem implements PermissibleItem, SelectableItem {

    private static String selectedText = "Selected";
    private static Material selectedMaterial = Material.LIME_DYE;
    private static String unselectedText = "Unselected";
    private static Material unselectedMaterial = Material.GRAY_DYE;

    private final String name;
    private final PlayerData playerData;
    private final String permission;
    private final List<String> noPermissionLore;
    private boolean hasPermission = false;
    private boolean selected;

    public ModifierItem(String modifier, String name, PlayerData playerData, List<String> noPermissionLore) {
        // Modifiers have two underlying items - selected and unselected. This doesn't really conform to the
        // design pattern I've gone for, so I may rework this in the future. The template passed in here
        super(modifier, new ItemStackTemplate(selectedMaterial, name, null, null));

        permission = "chatcolor.modifier." + modifier;
        selected = playerData.getModifiers().contains(modifier.charAt(0));

        this.name = name;
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
            ItemStackTemplate template = new ItemStackTemplate(selected ? selectedMaterial : unselectedMaterial, name, null, null);
            item = template.build(1);
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

            playerData.addModifier(data.charAt(0));
            selected = true;
        }

        return true;
    }

    @Override
    public void unselect() {
        if (selected) {
            playerData.removeModifier(data.charAt(0));
            selected = false;
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public static void setSelectedText(String selectedText) {
        ModifierItem.selectedText = selectedText;
    }

    public static void setUnselectedText(String unselectedText) {
        ModifierItem.unselectedText = unselectedText;
    }

    public static void setSelectedMaterial(Material selectedMaterial) {
        ModifierItem.selectedMaterial = selectedMaterial;
    }

    public static void setUnselectedMaterial(Material unselectedMaterial) {
        ModifierItem.unselectedMaterial = unselectedMaterial;
    }

}
