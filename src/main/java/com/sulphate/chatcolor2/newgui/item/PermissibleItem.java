package com.sulphate.chatcolor2.newgui.item;

import org.bukkit.entity.Player;

public interface PermissibleItem {

    void checkPermission(Player player);

    boolean hasPermission();

}
