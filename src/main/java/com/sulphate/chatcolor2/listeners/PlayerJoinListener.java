package com.sulphate.chatcolor2.listeners;

import java.util.UUID;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        GeneralUtils.checkDefault(uuid, configUtils);

        if ((boolean) configUtils.getSetting("join-message")) {
            if (configUtils.getColour(uuid).contains("rainbow")) {
                // Make sure the sequence is valid.
                String rseq = (String) configUtils.getSetting("rainbow-sequence");
                GeneralUtils.verifyRainbowSequence(rseq, true, configUtils);
            }

            // Check if they have a custom colour, and if it should be enforced (copied code from chat listener, may abstract it at some point).
            String customColour = configUtils.getCustomColour(player);
            String colour = configUtils.getColour(uuid);

            if (customColour != null) {
                // If it should be forced, set it so.
                if ((boolean) configUtils.getSetting("force-custom-colors")) {
                    colour = customColour;
                }
            }

            player.sendMessage(M.PREFIX + GeneralUtils.colourSetMessage(M.CURRENT_COLOR, colour, configUtils, M));
        }

        if (GeneralUtils.check(player)) {
            player.sendMessage(M.PREFIX + M.PLUGIN_NOTIFICATION.replace("[version]", ChatColor.getPlugin().getDescription().getVersion()));
        }
    }
}
