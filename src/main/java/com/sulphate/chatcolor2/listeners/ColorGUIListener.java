package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ColorGUIListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory.getTitle().equals(CC2Utils.colourise("&bColor Picker GUI"))) {
            event.getWhoClicked().sendMessage(CC2Utils.colourise("&aYou clicked inside the color picker GUI!"));
        }
    }

    public static void openGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&bColor Picker GUI"));
        player.openInventory(inventory);
    }

}
