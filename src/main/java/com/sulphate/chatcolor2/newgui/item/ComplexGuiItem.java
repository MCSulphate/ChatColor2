package com.sulphate.chatcolor2.newgui.item;

public abstract class ComplexGuiItem extends GuiItem {

    protected final String data;
    protected final ItemStackTemplate itemTemplate;

    public ComplexGuiItem(String data, ItemStackTemplate itemTemplate) {
        this.data = data;
        this.itemTemplate = itemTemplate;
    }

}
