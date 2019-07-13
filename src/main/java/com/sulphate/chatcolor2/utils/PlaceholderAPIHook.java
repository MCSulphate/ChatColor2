package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private ChatColor plugin;
    private ConfigUtils configUtils;
    private Messages M;

    public PlaceholderAPIHook(ChatColor plugin, ConfigUtils configUtils, Messages M) {
        this.plugin = plugin;
        this.configUtils = configUtils;
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

                return GeneralUtils.colourise(colour);
            }

            case "full_color_text": {
                String colour = configUtils.getColour(uuid);

                return getTextEquivalent(colour);
            }

            case "modifiers": {
                String colour = configUtils.getColour(uuid);
                String modPart = colour.substring(2);

                return GeneralUtils.colourise(modPart);
            }

            case "modifiers_text": {
                String colour = configUtils.getColour(uuid);
                String modPart = colour.substring(2);

                return getTextEquivalent(modPart);
            }

            case "color": {
                String colour = configUtils.getColour(uuid);
                String colourPart = colour.substring(0, 2);

                return GeneralUtils.colourise(colourPart);
            }

            case "color_text": {
                String colour = configUtils.getColour(uuid);
                String colourPart = colour.substring(0, 2);

                return getTextEquivalent(colourPart);
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

    // Returns the text equivalent of a string of colours or modifiers.
    private String getTextEquivalent(String str) {
        String stripped = str.replace(org.bukkit.ChatColor.COLOR_CHAR + "", "");
        char[] chars = stripped.toCharArray();

        StringBuilder builder = new StringBuilder(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            List<String> specialChars = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o");
            String[] charNames = { M.BLACK, M.DARK_BLUE, M.DARK_GREEN, M.DARK_AQUA, M.DARK_RED, M.DARK_PURPLE, M.GOLD, M.GRAY, M.DARK_GRAY, M.BLUE, M.GREEN, M.AQUA, M.RED, M.LIGHT_PURPLE, M.YELLOW, M.WHITE, M.OBFUSCATED, M.BOLD, M.STRIKETHROUGH, M.UNDERLINED, M.ITALIC };

            char chr = chars[i];
            int index = specialChars.indexOf(chr + "");

            // Get the correct text equiv., and add it to the builder.
            String text = "&" + chr + charNames[index];
            builder.append(',').append(text);
        }

        return GeneralUtils.colourise(builder.toString());
    }

}
