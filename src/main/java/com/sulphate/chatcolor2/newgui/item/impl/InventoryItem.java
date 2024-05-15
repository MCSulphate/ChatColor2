package com.sulphate.chatcolor2.newgui.item.impl;

import com.sulphate.chatcolor2.newgui.Gui;
import com.sulphate.chatcolor2.newgui.GuiManager;
import com.sulphate.chatcolor2.newgui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.newgui.item.ClickableItem;
import com.sulphate.chatcolor2.newgui.item.ComplexGuiItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryItem extends ComplexGuiItem implements ClickableItem {

    private final Player player;
    private final GuiManager guiManager;

    public InventoryItem(String data, ItemStackTemplate itemTemplate, Player player, GuiManager guiManager) {
        super(data, itemTemplate);

        this.player = player;
        this.guiManager = guiManager;
    }

    @Override
    public ItemStack buildItem() {
        return itemTemplate.build(1);
    }

    @Override
    public void click() {
        // It is necessary to create the GUI here as otherwise it will result in a stack overflow if two inventories
        // refer to each other (as opposed to when the item is created).
        Gui targetGui = guiManager.createGui(data, player);
        guiManager.openGui(targetGui, player);
    }

}
