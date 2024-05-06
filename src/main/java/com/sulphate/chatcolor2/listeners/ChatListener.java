package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.event.ChatColorEvent;
import com.sulphate.chatcolor2.commands.Setting;
import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.Config;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class ChatListener implements Listener, Reloadable {

    private static final Pattern SYMBOLS_REGEX = Pattern.compile("^[!^\"£$%*()\\[\\]{}'#@~;:,./<>?\\\\|\\-_=+]+[^!^\"£$%&*()\\[\\]{}'#@~;:,./<>?\\\\|\\-_=+].+");

    private final ConfigsManager configsManager;
    private final GeneralUtils generalUtils;
    private final PlayerDataStore dataStore;

    private YamlConfiguration mainConfig;

    public ChatListener(ConfigsManager configsManager, GeneralUtils generalUtils, PlayerDataStore dataStore) {
        this.configsManager = configsManager;
        this.generalUtils = generalUtils;
        this.dataStore = dataStore;

        reload();
    }

    public void reload() {
        mainConfig = configsManager.getConfig(Config.MAIN_CONFIG);
    }

    public void onEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        UUID uuid = player.getUniqueId();

        if (event.isCancelled() || checkHasSymbolPrefix(message)) {
            return;
        }

        boolean defaultColourEnabled = mainConfig.getBoolean(Setting.DEFAULT_COLOR_ENABLED.getConfigPath());

        // If they chat before the data store has had a chance to load/fail their data, use the default colour, or if
        // not enabled, do nothing at all.
        if (dataStore.getColour(uuid) == null) {
            if (defaultColourEnabled) {
                String defaultColor = mainConfig.getString("default.color");
                colourAndModify(player, message, defaultColor, event);
            }

            return;
        }

        // Check default colour.
        if (defaultColourEnabled) {
            generalUtils.checkDefault(uuid);
        }

        message = checkColourCodes(message, player);

        // Check if they have a group colour, and if it should be enforced.
        String groupColour = generalUtils.getGroupColour(player);
        String colour = dataStore.getColour(uuid);

        if (groupColour != null) {
            // If it should be forced, set it so.
            if (mainConfig.getBoolean(Setting.FORCE_GROUP_COLORS.getConfigPath())) {
                colour = groupColour;
            }
        }

        colourAndModify(player, message, colour, event);
    }

    private boolean checkHasSymbolPrefix(String message) {
        boolean checkSymbols = mainConfig.getBoolean(Setting.IGNORE_SYMBOL_PREFIXES.getConfigPath());

        if (checkSymbols) {
            return SYMBOLS_REGEX.matcher(message).matches();
        }
        else {
            return false;
        }
    }

    private void colourAndModify(Player player, String message, String colour, AsyncPlayerChatEvent event) {
        if (GeneralUtils.isDifferentWhenColourised(message)) {
            boolean override = mainConfig.getBoolean(Setting.COLOR_OVERRIDE.getConfigPath());

            if (override) {
                while (GeneralUtils.isDifferentWhenColourised(message)) {
                    // Strip the colour from the message.
                    message = org.bukkit.ChatColor.stripColor(GeneralUtils.colourise(message));
                }

                event.setMessage(message);
            }
            else {
                event.setMessage(GeneralUtils.colourise(message));
            }
        }
        else {
            if (fireEvent(player, message, colour, event)) {
                event.setMessage(generalUtils.colouriseMessage(colour, message, false));
            }
        }
    }

    private String checkColourCodes(String message, Player player) {
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

        return message;
    }

    private boolean fireEvent(Player player, String message, String colour, AsyncPlayerChatEvent chatEvent) {
        ChatColorEvent chatColorEvent = new ChatColorEvent(player, message, colour, chatEvent);
        Bukkit.getPluginManager().callEvent(chatColorEvent);

        return !chatColorEvent.isCancelled();
    }

}
