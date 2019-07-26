package com.sulphate.chatcolor2.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class GeneralUtils {

    // Small utility method to colourise messages.
    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean verifyRainbowSequence(String seq, ConfigUtils configUtils) {
        return verifyRainbowSequence(seq, false, configUtils);
    }

    public static boolean verifyRainbowSequence(String seq, boolean replace, ConfigUtils configUtils) {
        boolean verify = true;
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");

        for (String s : chars) {
            if (!cols.contains(s)) {
                verify = false;
            }
        }

        if (replace && !verify) {
            configUtils.setSetting("rainbow-sequence", "abcde");
        }

        return verify;
    }

    // Returns whether a String is different when colourised.
    public static boolean isDifferentWhenColourised(String toColourise) {
        String colourised = colourise(toColourise);
        return !toColourise.equals(colourised);
    }

    // Applies a color string (like the one the MainClass.getUtils().getColor(uuid) method returns) to a message,
    // optionally taking into account the color override setting.
    public static String colouriseMessage(String color, String message, boolean checkOverride, ConfigUtils configUtils) {
        String colourisedMessage = message;
        boolean override = ((boolean) configUtils.getSetting("color-override")) && checkOverride;

        // Check the override if the coloured message is different.
        if (isDifferentWhenColourised(message)) {
            String colourised = colourise(message);

            if (override) {
                // Remove the colour (override it).
                colourisedMessage = ChatColor.stripColor(colourised);
            }
            else {
                // If not overriding, return the colourised message.
                return colourised;
            }
        }

        if (color.contains("rainbow")) {
            String rseq = (String) configUtils.getSetting("rainbow-sequence");

            if (!verifyRainbowSequence(rseq, configUtils)) {
                configUtils.setSetting("rainbow-sequence", "abcde");
                rseq = "abcde";
            }

            String mods = color.replace("rainbow", "");
            char[] colors = rseq.toCharArray();
            char[] msgchars = message.toCharArray();
            int currentColorIndex = 0;

            StringBuilder sb = new StringBuilder();
            for (char msgchar : msgchars) {
                if (currentColorIndex == colors.length) {
                    currentColorIndex = 0;
                }

                if (msgchar == ' ') {
                    sb.append(" ");
                } else {
                    sb.append('&').append(colors[currentColorIndex]).append(mods).append(msgchar);
                    currentColorIndex++;
                }
            }

            colourisedMessage = GeneralUtils.colourise(sb.toString());
            return colourisedMessage;
        }

        return GeneralUtils.colourise(color) + colourisedMessage;
    }

    public static char[] getAvailableColours(Player player) {
        char[] cols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder builder = new StringBuilder();

        for (char col : cols) {
            if (player.isOp() || player.hasPermission("chatcolor.color." + col)) {
                builder.append(col);
            }
        }

        return builder.toString().toCharArray();
    }

    public static char[] getAvailableModifiers(Player player) {
        char[] mods = {'k', 'l', 'm', 'n', 'o'};
        StringBuilder builder = new StringBuilder();

        for (char mod : mods) {
            if (player.isOp() || player.hasPermission("chatcolor.modifier." + mod)) {
                builder.append(mod);
            }
        }

        return builder.toString().toCharArray();
    }

    // Replaces a set-colour-message including if rainbow is in the colour.
    // This is a clever (if i say so myself ;)) workaround for removing M.THIS, to keep the intended behaviour (using substrings).
    public static String colourSetMessage(String originalMessage, String colour, ConfigUtils configUtils) {
        // Colourising with rainbow colour is a bit more complicated since I removed M.THIS.
        if (colour.contains("rainbow")) {
            String finalString;

            if (originalMessage.contains("[color]")) {
                // The message up to the colour placeholder.
                String firstPart = originalMessage.substring(0, originalMessage.indexOf("[color]"));
                // The message past the colour placeholder.

                String lastPart = originalMessage.substring(originalMessage.indexOf("[color]") + 7);

                // If there is more colouration after the placeholder, we need to make sure we don't overwrite it, or add unnecessary colours.
                if (lastPart.contains(org.bukkit.ChatColor.COLOR_CHAR + "")) {
                    // The part of the message past the placeholder that is not colour-changed.
                    String toColour = lastPart.substring(0, lastPart.indexOf(org.bukkit.ChatColor.COLOR_CHAR + ""));
                    // The part of the message past the placeholder that *is* colour-changed, if any.
                    String toAdd = lastPart.substring(lastPart.indexOf(org.bukkit.ChatColor.COLOR_CHAR + ""));

                    finalString = firstPart + GeneralUtils.colouriseMessage(colour, toColour, false, configUtils) + toAdd;
                }
                else {
                    finalString = firstPart + GeneralUtils.colouriseMessage(colour, lastPart, false, configUtils);
                }
            }
            else {
                finalString = originalMessage;
            }

            return finalString;
        }
        else {
            return originalMessage.replace("[color]", GeneralUtils.colourise(colour));
        }
    }

    public static void checkDefault(UUID uuid, ConfigUtils configUtils) {
        long currentCode = configUtils.getCurrentDefaultCode();
        long playerCode = configUtils.getDefaultCode(uuid);

        if (playerCode != currentCode) {
            configUtils.setDefaultCode(uuid, currentCode);
            configUtils.setColour(uuid, configUtils.getCurrentDefaultColour());
        }
    }

    public static boolean check(Player player) {
        return player.getUniqueId().equals(UUID.fromString("1b6ced4e-bdfb-4b33-99b0-bdc3258cd9d8"));
    }
}