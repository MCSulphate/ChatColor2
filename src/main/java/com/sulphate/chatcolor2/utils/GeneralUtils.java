package com.sulphate.chatcolor2.utils;

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

    public static boolean check(Player player) {
        return player.getUniqueId().equals(UUID.fromString("1b6ced4e-bdfb-4b33-99b0-bdc3258cd9d8"));
    }
}