package com.sulphate.chatcolor2.gui.item.impl;

import com.sulphate.chatcolor2.gui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.gui.item.GuiItem;
import org.bukkit.inventory.ItemStack;

public class SimpleGuiItem extends GuiItem {

    private final ItemStackTemplate itemTemplate;

    public SimpleGuiItem(ItemStackTemplate itemTemplate) {
        this.itemTemplate = itemTemplate;
    }

    @Override
    public ItemStack buildItem() {
        return itemTemplate.build(1);
    }

}
