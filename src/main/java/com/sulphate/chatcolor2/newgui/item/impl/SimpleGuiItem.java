package com.sulphate.chatcolor2.newgui.item.impl;

import com.sulphate.chatcolor2.newgui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.newgui.item.GuiItem;
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
