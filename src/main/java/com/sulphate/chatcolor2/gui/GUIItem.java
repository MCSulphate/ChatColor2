package com.sulphate.chatcolor2.gui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUIItem {

    private final ItemType type;
    private final String data;
    private final ItemStack item;
    private final List<String> extraLore;

    public GUIItem(ItemType type, String data, ItemStack item, List<String> extraLore) {
        if (data == null && !type.equals(ItemType.FILLER)) {
            throw new IllegalArgumentException("GUIItem data cannot be null unless it is ItemType.FILLER");
        }

        this.type = type;
        this.data = data;
        this.extraLore = extraLore;

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

    public boolean hasExtraLore() {
        return extraLore != null;
    }

    public List<String> getExtraLore() {
        return extraLore;
    }

}
