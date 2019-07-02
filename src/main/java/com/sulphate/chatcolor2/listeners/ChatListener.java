package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sulphate.chatcolor2.main.ChatColor;

import java.util.UUID;

public class ChatListener implements Listener {

    private ConfigUtils configUtils;

    public ChatListener(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        UUID uuid = player.getUniqueId();
        checkDefault(uuid);

        // If their message contains &, check they have permissions for it, or strip the colour.
        if (e.getMessage().contains("&")) {
            String colourised = GeneralUtils.colourise(message);
            String stripped = org.bukkit.ChatColor.stripColor(colourised);

            // If the two messages are different, there was a colour code in it.
            if (!colourised.equals(stripped) && !player.hasPermission("chatcolor.use-colour-codes")) {
                e.setMessage(stripped);
            }
        }

        e.setMessage(GeneralUtils.colouriseMessage(configUtils.getColour(uuid), e.getMessage(), true, configUtils));
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
