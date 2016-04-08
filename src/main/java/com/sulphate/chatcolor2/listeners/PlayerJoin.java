package com.sulphate.chatcolor2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.utils.FileUtils;

public class PlayerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(PlayerJoinEvent e) {

        Player p = e.getPlayer();

        FileUtils.updatePlayer(p);

    }

}
