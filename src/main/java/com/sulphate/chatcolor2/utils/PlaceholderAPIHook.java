package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private ChatColor plugin;
    private ConfigUtils configUtils;
    private Messages M;

    public PlaceholderAPIHook(ChatColor plugin, ConfigUtils configUtils, Messages M) {
        this.plugin = plugin;
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
    public String onPlaceholderRequest(Player player, String identifier) {

        // Ignore if player is null.
        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();

        switch (identifier) {
            case "full-color": {
                // Return the player's full colour, including modifiers. Does not work for rainbow colour!
                String colour = configUtils.getColour(uuid);

                return GeneralUtils.colourise(colour);
            }

            case "modifiers": {
                String colour = configUtils.getColour(uuid);
                String modPart = colour.substring(2);

                return GeneralUtils.colourise(modPart);
            }

            case "color": {
                String colour = configUtils.getColour(uuid);
                String colourPart = colour.substring(0, 2);

                return GeneralUtils.colourise(colourPart);
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
