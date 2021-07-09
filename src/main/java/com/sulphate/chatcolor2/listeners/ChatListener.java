package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    private final ConfigUtils configUtils;

    public ChatListener(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();
        UUID uuid = player.getUniqueId();

        // Check default colour.
        if ((boolean) configUtils.getSetting("default-color-enabled")) {
            GeneralUtils.checkDefault(uuid, configUtils);
        }

        // If their message contains &, check they have permissions for it, or strip the colour.
        if (!player.hasPermission("chatcolor.use-color-codes")) {
            // A player reported using '&&a' for example, would bypass this. So, loop until it's not different.
            while (GeneralUtils.isDifferentWhenColourised(message)) {
                // Strip the colour from the message.
                message = org.bukkit.ChatColor.stripColor(GeneralUtils.colourise(message));
            }
        }

        // Check if they have a group colour, and if it should be enforced.
        String groupColour = configUtils.getGroupColour(player);
        String colour = configUtils.getColour(uuid);

        if (groupColour != null) {
            // If it should be forced, set it so.
            if ((boolean) configUtils.getSetting("force-group-colors")) {
                colour = groupColour;
            }
        }

        e.setMessage(GeneralUtils.colouriseMessage(colour, message, true, configUtils));
    }

}
