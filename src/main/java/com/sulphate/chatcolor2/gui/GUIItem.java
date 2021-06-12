package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUIItem {

    private final ItemType type;
    private final String data;
    private final ItemStack item;

    public GUIItem(ItemType type, String data, String materialName, String displayName, List<String> lore) {
        if (data == null) {
            throw new IllegalArgumentException("GUIItem data cannot be null.");
        }

        this.type = type;
        this.data = data;

        Material material = Material.getMaterial(materialName);
        ItemStack item = new ItemStack(material);

        InventoryUtils.setDisplayName(item, displayName);
        InventoryUtils.setLore(item, lore);

        this.item = item;
    }

    public GUIItem(ItemType type, String data, ItemStack item) {
        if (data == null) {
            throw new IllegalArgumentException("GUIItem data cannot be null.");
        }

        this.type = type;
        this.data = data;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

}
