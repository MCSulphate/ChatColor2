package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerQuitEvent e) {

        String playername = e.getPlayer().getName();
        MainClass.get().getSQL().removePlayerFromCaches(playername);

    }

}
