package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.utils.Messages;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private Messages M;
    private ConfigUtils configUtils;
    private ConfigsManager configsManager;

    public PlayerJoinListener(Messages M, ConfigUtils configUtils, ConfigsManager configsManager) {
        this.M = M;
        this.configUtils = configUtils;
        this.configsManager = configsManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // Load the player's config, if necessary.
        if (configsManager.getPlayerConfig(uuid) == null) {
            configsManager.loadPlayerConfig(uuid);
        }

        // Update the player list and check their default colour.
        configUtils.updatePlayerListEntry(player.getName(), uuid);
        checkDefault(uuid);

        if ((boolean) configUtils.getSetting("join-message")) {
            if (configUtils.getColour(uuid).contains("rainbow")) {
                // Make sure the sequence is valid.
                String rseq = (String) configUtils.getSetting("rainbow-sequence");
                GeneralUtils.verifyRainbowSequence(rseq, true, configUtils);
            }

            player.sendMessage(M.PREFIX + M.CURRENT_COLOR + GeneralUtils.colouriseMessage(configUtils.getColour(uuid), M.THIS, false, configUtils));
        }

        if (GeneralUtils.check(player)) {
            player.sendMessage(M.PREFIX + M.PLUGIN_NOTIFICATION.replace("[version]", ChatColor.getPlugin().getDescription().getVersion()));
        }
    }

    // Checks if a player's colour has been set to the default since a default was last set.
    private void checkDefault(UUID uuid) {
        long currentCode = configUtils.getCurrentDefaultCode();
        long playerCode = configUtils.getDefaultCode(uuid);

        if (playerCode != currentCode) {
            configUtils.setDefaultCode(uuid, currentCode);
            configUtils.setColour(uuid, configUtils.getCurrentDefaultColour());
        }
    }
}
