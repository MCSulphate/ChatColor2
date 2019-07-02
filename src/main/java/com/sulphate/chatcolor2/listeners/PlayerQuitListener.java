package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private ConfigsManager configsManager;

    public PlayerQuitListener(ConfigsManager configsManager) {
        this.configsManager = configsManager;
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        configsManager.unloadPlayerConfig(player.getUniqueId());
    }

}
