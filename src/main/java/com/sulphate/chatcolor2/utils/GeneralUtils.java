package com.sulphate.chatcolor2.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtils {

    // Small utility method to colourise messages.
    public static String colourise(String message) {
        // Replace hex colour codes with the correct hex colour(s).
        Pattern hexPattern = Pattern.compile("#[A-Fa-f0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(result, createHexColour(matcher.group()));
        }

        matcher.appendTail(result);
        message = result.toString();

        // Then, translate '&' colour codes.
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(colourise(message));
    }

    // Validates if a string is a valid hex colour.
    public static boolean isValidHexColour(String toValidate) {
        // If it replaces the regex to nothing, then it's a valid colour.
        return toValidate.replaceFirst("#[a-fA-F0-9]{6}", "").equals("");
    }

    public static String createHexColour(String hexString) {
        return net.md_5.bungee.api.ChatColor.of(hexString).toString();
    }

    public static boolean verifyRainbowSequence(String seq, ConfigUtils configUtils) {
        return verifyRainbowSequence(seq, false, configUtils);
    }

    public static boolean verifyRainbowSequence(String seq, boolean replace, ConfigUtils configUtils) {
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");

        boolean verify = true;
        // Hex flag to check for legacy availability.
        boolean hexFlag = false;
        for (int i = 0; i < chars.length; i++) {
            String c = chars[i];

            // Handle hex colours in the rainbow sequence.
            if (c.equals("#")) {
                hexFlag = true;

                if (seq.length() < i + 7) {
                    verify = false;
                    break;
                }

                String hexSubstring = seq.substring(i, i + 7);

                if (!GeneralUtils.isValidHexColour(hexSubstring)) {
                    verify = false;
                    break;
                }
                else {
                    i += 6;
                }
            }
            else {
                if (!cols.contains(c)) {
                    verify = false;
                    break;
                }
            }
        }

        if (hexFlag && verify && CompatabilityUtils.isMaterialLegacy()) {
            verify = false;
        }

        if (replace && !verify) {
            configUtils.setSetting("rainbow-sequence", "abcde");
        }

        return verify;
    }

    public static String getRainbowSequenceText(char[] sequence, String sequenceString) {
        // Only need to manually colour the new one.
        StringBuilder builder = new StringBuilder();

        // Use a skip counter to handle hex colours.
        int hexCounter = 0;
        String currentHexColour = null;

        for (int i = 0; i < sequence.length; i++) {
            char c = sequence[i];

            if (c == '#') {
                hexCounter = 6;
                currentHexColour = sequenceString.substring(i, i + 7);

                builder.append(currentHexColour);
                builder.append(c);
            }
            else if (hexCounter > 0) {
                hexCounter--;

                builder.append(currentHexColour);
                builder.append(c);
            }
            else {
                builder.append("&").append(c).append(c);
            }
        }

        return colourise(builder.toString());
    }

    // Returns whether a String is different when colourised.
    public static boolean isDifferentWhenColourised(String toColourise) {
        String colourised = colourise(toColourise);
        return !toColourise.equals(colourised);
    }

    // Applies a color string (like the one the MainClass.getUtils().getColor(uuid) method returns) to a message,
    // optionally taking into account the color override setting.
    public static String colouriseMessage(String colour, String message, boolean checkOverride, ConfigUtils configUtils) {
        String colourisedMessage = message;

        // Check the override if the coloured message is different.
        if (checkOverride && isDifferentWhenColourised(message)) {
            boolean override = ((boolean) configUtils.getSetting("color-override")) && checkOverride;
            String colourised = colourise(message);

            if (override) {
                while (isDifferentWhenColourised(message)) {
                    // Remove the colour (override it).
                    colourisedMessage = ChatColor.stripColor(colourised);
                }
            }
            else {
                // If not overriding, return the colourised message.
                return colourised;
            }
        }

        if (colour.contains("rainbow")) {
            String rseq = (String) configUtils.getSetting("rainbow-sequence");

            if (!verifyRainbowSequence(rseq, configUtils)) {
                configUtils.setSetting("rainbow-sequence", "abcde");
                rseq = "abcde";
            }

            String mods = colour.replace("rainbow", "");
            char[] colours = rseq.toCharArray();
            int[] colourOffsets = new int[colours.length];
            char[] msgchars = message.toCharArray();
            int currentColourIndex = 0;

            // Find the colour offsets - this accounts for hex colours.
            int offsetIndex = 0;
            for (int i = 0; i < colours.length; i++) {
                char c = colours[i];
                colourOffsets[offsetIndex] = i;
                offsetIndex++;

                // Add six characters for a hex colour.
                if (c == '#') {
                    i += 6;
                }
            }

            StringBuilder builder = new StringBuilder();

            for (char c : msgchars) {
                int colourIndex = colourOffsets[currentColourIndex];
                char colourChar = colours[colourIndex];

                if (colourChar == '#') {
                    builder.append(rseq, colourIndex, colourIndex + 7);
                }
                else {
                    builder.append('&').append(colourChar);
                }

                builder.append(mods);
                builder.append(c);

                currentColourIndex++;
                if (currentColourIndex == offsetIndex) {
                    currentColourIndex = 0;
                }
            }

            return GeneralUtils.colourise(builder.toString());
        }

        return GeneralUtils.colourise(colour) + colourisedMessage;
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
    // This is a clever (if I say so myself) workaround for removing M.THIS, to keep the intended behaviour (using substrings).
    public static String colourSetMessage(String originalMessage, String colour, ConfigUtils configUtils, Messages M) {
        String placeholder = originalMessage.contains("[color]") ? "[color]" : originalMessage.contains("[color-text]") ? "[color-text]" : null;

        // If there is no placeholder present, we don't need to do anything.
        if (placeholder == null) {
            return originalMessage;
        }

        // If the color-text placeholder is present, set colour to the text equivalent.
        if (placeholder.equals("[color-text]")) {
            colour = GeneralUtils.getTextEquivalent(colour, M, configUtils);
        }

        // Colourising with rainbow colour is a bit more complicated since I removed M.THIS.
        if (colour.contains("rainbow")) {
            String finalString;

            // The message up to the colour placeholder.
            String firstPart = originalMessage.substring(0, originalMessage.indexOf(placeholder));
            // The message past the colour placeholder.

            String lastPart = originalMessage.substring(originalMessage.indexOf(placeholder) + placeholder.length());

            // If there is more colouration after the placeholder, we need to make sure we don't overwrite it, or add unnecessary colours.
            if (lastPart.contains(ChatColor.COLOR_CHAR + "")) {
                // The part of the message past the placeholder that is not colour-changed.
                String toColour = lastPart.substring(0, lastPart.indexOf(ChatColor.COLOR_CHAR + ""));
                // The part of the message past the placeholder that *is* colour-changed, if any.
                String toAdd = lastPart.substring(lastPart.indexOf(ChatColor.COLOR_CHAR + ""));

                finalString = firstPart + GeneralUtils.colouriseMessage(colour, toColour, false, configUtils) + toAdd;
            }
            else {
                finalString = firstPart + GeneralUtils.colouriseMessage(colour, lastPart, false, configUtils);
            }

            return finalString;
        }
        else {
            return originalMessage.replace(placeholder, GeneralUtils.colourise(colour));
        }
    }

    // Returns the text equivalent of a string of colours or modifiers.
    public static String getTextEquivalent(String str, Messages M, ConfigUtils configUtils) {
        StringBuilder builder = new StringBuilder();
        String stripped = str.replaceAll("&", "");

        if (stripped.contains("rainbow")) {
            builder.append(colouriseMessage("rainbow", M.RAINBOW, false, configUtils)).append("&r");
            stripped = stripped.replace("rainbow", "");
        }

        char[] chars = stripped.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            List<String> specialChars = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "l", "m", "n", "o");
            String[] charNames = { M.BLACK, M.DARK_BLUE, M.DARK_GREEN, M.DARK_AQUA, M.DARK_RED, M.DARK_PURPLE, M.GOLD, M.GRAY, M.DARK_GRAY, M.BLUE, M.GREEN, M.AQUA, M.RED, M.LIGHT_PURPLE, M.YELLOW, M.WHITE, M.OBFUSCATED, M.BOLD, M.STRIKETHROUGH, M.UNDERLINED, M.ITALIC };

            char chr = chars[i];
            int index = specialChars.indexOf(chr + "");

            // Get the correct text equiv., and add it to the builder.
            String text = "&" + chr + charNames[index] + "&r";

            if (builder.length() > 0 || i != 0) {
                builder.append(", ");
            }

            builder.append(text);
        }

        return colourise(builder.toString());
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

    public static boolean checkPermission(Player player, String permission) {
        return (player.isOp() || player.hasPermission(permission));
    }

}