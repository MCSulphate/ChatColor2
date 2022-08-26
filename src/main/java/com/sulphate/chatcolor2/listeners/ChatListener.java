package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;
    private final Messages M;

    public ChatListener(ConfigUtils configUtils, GeneralUtils generalUtils, Messages M) {
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
        this.M = M;
    }

    public void onEvent(AsyncPlayerChatEvent e) {
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

        if (!player.hasPermission("chatcolor.use-hex-codes") && GeneralUtils.containsHexColour(message)) {
            while (GeneralUtils.isDifferentWhenColourised(message)) {
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
