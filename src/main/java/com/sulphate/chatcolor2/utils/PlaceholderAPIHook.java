package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final ChatColor plugin;
    private final ConfigUtils configUtils;
    private final CustomColoursManager customColoursManager;
    private final PlayerDataStore dataStore;
    private final Messages M;

    public PlaceholderAPIHook(ChatColor plugin, ConfigUtils configUtils, CustomColoursManager customColoursManager, PlayerDataStore dataStore, Messages M) {
        this.plugin = plugin;
        this.configUtils = configUtils;
        this.customColoursManager = customColoursManager;
        this.dataStore = dataStore;
        this.M = M;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "cc";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {

        // Ignore if player is null.
        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();
        String colour = dataStore.getColour(uuid);

        switch (identifier) {
            case "full_color": {
                // Return the player's full colour, including modifiers. Does not work for rainbow colour!
                if (GeneralUtils.isCustomColour(colour)) {
                    colour = customColoursManager.getCustomColour(colour);
                }

                return GeneralUtils.colourise(colour);
            }

            case "modifiers": {
                if (GeneralUtils.isCustomColour(colour)) {
                    colour = customColoursManager.getCustomColour(colour);
                }

                int modifiersStartIndex = (colour.substring(1).indexOf("&"));
                String modPart = colour.substring(modifiersStartIndex + 1);

                return GeneralUtils.colourise(modPart);
            }

            case "color": {
                if (GeneralUtils.isCustomColour(colour)) {
                    colour = customColoursManager.getCustomColour(colour);
                }

                // Remove any modifiers (start index = second & symbol).
                int modifiersStartIndex = (colour.substring(1).indexOf("&"));

                if (modifiersStartIndex != -1) {
                    colour = colour.substring(0, modifiersStartIndex + 1);
                }

                return GeneralUtils.colourise(colour);
            }

            case "color_name": {
                // Remove any modifiers (start index = second & symbol).
                int modifiersStartIndex = (colour.substring(1).indexOf("&"));

                if (modifiersStartIndex != -1) {
                    colour = colour.substring(0, modifiersStartIndex + 1);
                }

                return colour.replaceAll("&", "");
            }

            case "modifier_names": {
                int modifiersStartIndex = (colour.substring(1).indexOf("&"));

                if (modifiersStartIndex == -1) {
                    return "";
                }
                else {
                    return colour.substring(modifiersStartIndex + 1).replaceAll("&", "");
                }
            }

            case "group": {
                String groupName = configUtils.getGroupColour(player, true);
                return groupName == null ? "None" : groupName;
            }

            default: {
                // Check if it is a valid <colour>_available identifier.
                if (identifier.matches("^[0-9abcdef]_available$")) {
                    String codeToCheck = identifier.split("_")[0];

                    if (player.hasPermission("chatcolor.color." + codeToCheck)) {
                        return M.GUI_AVAILABLE;
                    }
                    else {
                        return M.GUI_UNAVAILABLE;
                    }
                }
                // Check if it is a valid <modifier>_available identifier.
                else if (identifier.matches("^[klmno]_available$")) {
                    String codeToCheck = identifier.split("_")[0];

                    if (player.hasPermission("chatcolor.modifier." + codeToCheck)) {
                        return M.GUI_AVAILABLE;
                    }
                    else {
                        return M.GUI_UNAVAILABLE;
                    }
                }
            }
        }

        return null;
    }

}
