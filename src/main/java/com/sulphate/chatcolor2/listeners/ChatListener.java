package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
        GeneralUtils.checkDefault(uuid, configUtils);

        // If their message contains &, check they have permissions for it, or strip the colour.
        if (GeneralUtils.isDifferentWhenColourised(message)) {
            if (!player.hasPermission("chatcolor.use-color-codes")) {
                // Strip the colour from the message.
                message = org.bukkit.ChatColor.stripColor(GeneralUtils.colourise(message));
            }
        }

        // Check if they have a custom colour, and if it should be enforced.
        String customColour = configUtils.getCustomColour(player);
        String colour = configUtils.getColour(uuid);

        if (customColour != null) {
            // If it should be forced, set it so.
            if ((boolean) configUtils.getSetting("force-custom-colors")) {
                colour = customColour;
            }
        }

        e.setMessage(GeneralUtils.colouriseMessage(colour, message, true, configUtils));
    }

}
