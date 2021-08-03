package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.utils.Messages;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final Messages M;
    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;
    private final ConfigsManager configsManager;

    public PlayerJoinListener(Messages M, ConfigUtils configUtils, GeneralUtils generalUtils, ConfigsManager configsManager) {
        this.M = M;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
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
        generalUtils.checkDefault(uuid);

        if ((boolean) configUtils.getSetting("join-message")) {
            // Check if they have a group colour, and if it should be enforced (copied code from chat listener, may abstract it at some point).
            String groupColour = configUtils.getGroupColour(player);
            String colour = configUtils.getColour(uuid);

            if (groupColour != null) {
                // If it should be forced, set it so.
                if ((boolean) configUtils.getSetting("force-group-colors")) {
                    colour = groupColour;
                }
            }

            player.sendMessage(M.PREFIX + generalUtils.colourSetMessage(M.CURRENT_COLOR, colour));
        }

        if (GeneralUtils.check(player)) {
            player.sendMessage(M.PREFIX + M.PLUGIN_NOTIFICATION.replace("[version]", ChatColor.getPlugin().getDescription().getVersion()));
        }
    }
}
