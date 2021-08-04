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
    private final GeneralUtils generalUtils;

    public ChatListener(ConfigUtils configUtils, GeneralUtils generalUtils) {
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(AsyncPlayerChatEvent e) {
        if (!(boolean) configUtils.getSetting("modify-chat")) {
            return;
        }

        Player player = e.getPlayer();
        String message = e.getMessage();
        UUID uuid = player.getUniqueId();

        // Check default colour.
        if ((boolean) configUtils.getSetting("default-color-enabled")) {
            generalUtils.checkDefault(uuid);
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

        e.setMessage(generalUtils.colouriseMessage(colour, message, true));
    }

}
