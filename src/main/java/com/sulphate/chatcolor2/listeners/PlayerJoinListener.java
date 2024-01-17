package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
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
    private final CustomColoursManager customColoursManager;
    private final PlayerDataStore dataStore;

    public PlayerJoinListener(Messages M, ConfigUtils configUtils, GeneralUtils generalUtils, CustomColoursManager customColoursManager, PlayerDataStore dataStore) {
        this.M = M;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
        this.customColoursManager = customColoursManager;
        this.dataStore = dataStore;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!dataStore.loadPlayerData(uuid)) {
            // TODO: Display error message.
        }

        if (!player.hasPlayedBefore()) {
            setInitialColour(uuid);
        }

        checkCustomColour(uuid);

        // Update the player list and check their default colour.
        configUtils.updatePlayerListEntry(player.getName(), uuid);
        generalUtils.checkDefault(uuid);

        sendJoinMessage(player);

        if (GeneralUtils.check(player)) {
            player.sendMessage(M.PREFIX + M.PLUGIN_NOTIFICATION.replace("[version]", ChatColor.getPlugin().getDescription().getVersion()));
        }
    }

    private void sendJoinMessage(Player player) {
        if ((boolean) configUtils.getSetting("join-message")) {
            // Check if they have a group colour, and if it should be enforced (copied code from chat listener, may abstract it at some point).
            String groupColour = configUtils.getGroupColour(player);
            String colour = dataStore.getColour(player.getUniqueId());

            if (groupColour != null) {
                // If it should be forced, set it so.
                if ((boolean) configUtils.getSetting("force-group-colors")) {
                    colour = groupColour;
                }
            }

            player.sendMessage(M.PREFIX + generalUtils.colourSetMessage(M.CURRENT_COLOR, colour));
        }
    }

    private void checkCustomColour(UUID uuid) {
        String colour = dataStore.getColour(uuid);

        if (colour.startsWith("%")) {
            if (customColoursManager.getCustomColour(colour) == null) {
                setInitialColour(uuid);
            }
        }
    }

    private void setInitialColour(UUID uuid) {
        String colour = dataStore.getColour(uuid);

        if (colour == null) {
            if ((boolean) configUtils.getSetting("default-color-enabled")) {
                dataStore.setColour(uuid, configUtils.getCurrentDefaultColour());
            }
            else {
                dataStore.setColour(uuid, "");
            }
        }
    }

}
