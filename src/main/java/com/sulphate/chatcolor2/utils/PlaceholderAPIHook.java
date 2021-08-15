package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final ChatColor plugin;
    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;
    private final CustomColoursManager customColoursManager;
    private final Messages M;

    public PlaceholderAPIHook(ChatColor plugin, ConfigUtils configUtils, GeneralUtils generalUtils, CustomColoursManager customColoursManager, Messages M) {
        this.plugin = plugin;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
        this.customColoursManager = customColoursManager;
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
            case "full_color": {
                // Return the player's full colour, including modifiers. Does not work for rainbow colour!
                String colour = configUtils.getColour(uuid);

                if (GeneralUtils.isCustomColour(colour)) {
                    colour = customColoursManager.getCustomColour(colour);
                }

                return GeneralUtils.colourise(colour);
            }

            case "full_color_text": {
                String colour = configUtils.getColour(uuid);

                return generalUtils.getTextEquivalent(colour);
            }

            case "full_color_text_plain": {
                String colour = configUtils.getColour(uuid);

                return org.bukkit.ChatColor.stripColor(generalUtils.getTextEquivalent(colour));
            }

            case "modifiers": {
                String colour = configUtils.getColour(uuid);
                String modPart = colour.substring(2);

                return GeneralUtils.colourise(modPart);
            }

            case "modifiers_text": {
                String colour = configUtils.getColour(uuid);

                // TODO: Fix this for custom & custom rainbows
                if (colour.contains("rainbow")) {
                    return generalUtils.getTextEquivalent(colour.replace("rainbow", ""));
                }
                else {
                    return generalUtils.getTextEquivalent(colour.substring(2));
                }
            }

            case "modifiers_text_plain": {
                String colour = configUtils.getColour(uuid);

                // TODO: Fix this for custom & custom rainbows
                if (colour.contains("rainbow")) {
                    return org.bukkit.ChatColor.stripColor(generalUtils.getTextEquivalent(colour.replace("rainbow", "")));
                }
                else {
                    return org.bukkit.ChatColor.stripColor(generalUtils.getTextEquivalent(colour.substring(2)));
                }
            }

            case "color": {
                String colour = configUtils.getColour(uuid);
                String colourPart = colour.substring(0, 2);

                return GeneralUtils.colourise(colourPart);
            }

            case "color_text": {
                String colour = configUtils.getColour(uuid);

                // TODO: Fix this for custom & custom rainbows
                if (colour.contains("rainbow")) {
                    return generalUtils.getTextEquivalent("rainbow");
                }
                else {
                    return generalUtils.getTextEquivalent(colour.substring(0, 2));
                }
            }

            case "color_text_plain": {
                String colour = configUtils.getColour(uuid);

                // TODO: Fix this for custom & custom rainbows
                if (colour.contains("rainbow")) {
                    return org.bukkit.ChatColor.stripColor(generalUtils.getTextEquivalent("rainbow"));
                }
                else {
                    return org.bukkit.ChatColor.stripColor(generalUtils.getTextEquivalent(colour.substring(0, 2)));
                }
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
