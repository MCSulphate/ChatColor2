package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.commands.Setting;
import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class GeneralUtils implements Reloadable {

    private static final String COLOUR_PLACEHOLDER = "[color]";

    private enum SpecialColorType {
        RAINBOW,
        GRADIENT
    }

    private final ConfigsManager configsManager;
    private final CustomColoursManager customColoursManager;
    private final PlayerDataStore dataStore;
    private final Messages M;

    private final Map<String, String> colourCodeToNameMap;
    private final Map<String, String> modifierCodeToNameMap;

    private YamlConfiguration mainConfig;
    private YamlConfiguration groupsConfig;

    public GeneralUtils(ConfigsManager configsManager, CustomColoursManager customColoursManager, PlayerDataStore dataStore, Messages M) {
        this.configsManager = configsManager;
        this.customColoursManager = customColoursManager;
        this.dataStore = dataStore;
        this.M = M;

        colourCodeToNameMap = new HashMap<>();
        modifierCodeToNameMap = new HashMap<>();

        reload();
    }

    public void reload() {
        mainConfig = configsManager.getConfig(Config.MAIN_CONFIG);
        groupsConfig = configsManager.getConfig(Config.GROUPS);

        colourCodeToNameMap.clear();
        modifierCodeToNameMap.clear();

        colourCodeToNameMap.put("0", M.BLACK);
        colourCodeToNameMap.put("1", M.DARK_BLUE);
        colourCodeToNameMap.put("2", M.DARK_GREEN);
        colourCodeToNameMap.put("3", M.DARK_AQUA);
        colourCodeToNameMap.put("4", M.DARK_RED);
        colourCodeToNameMap.put("5", M.DARK_PURPLE);
        colourCodeToNameMap.put("6", M.GOLD);
        colourCodeToNameMap.put("7", M.GRAY);
        colourCodeToNameMap.put("8", M.DARK_GRAY);
        colourCodeToNameMap.put("9", M.BLUE);
        colourCodeToNameMap.put("a", M.GREEN);
        colourCodeToNameMap.put("b", M.AQUA);
        colourCodeToNameMap.put("c", M.RED);
        colourCodeToNameMap.put("d", M.LIGHT_PURPLE);
        colourCodeToNameMap.put("e", M.YELLOW);
        colourCodeToNameMap.put("f", M.WHITE);

        modifierCodeToNameMap.put("k", M.OBFUSCATED);
        modifierCodeToNameMap.put("l", M.BOLD);
        modifierCodeToNameMap.put("m", M.STRIKETHROUGH);
        modifierCodeToNameMap.put("n", M.UNDERLINED);
        modifierCodeToNameMap.put("o", M.ITALIC);
    }

    // Small utility method to colourise messages.
    public static String colourise(String message) {
        // Attempt to colourise any rainbow text.
        if (message.contains("&u")) {
            message = colouriseSpecial(message, SpecialColorType.RAINBOW);
        }
        else if (message.contains("&g")) {
            message = colouriseSpecial(message, SpecialColorType.GRADIENT);
        }

        // Replace hex colour codes with the correct hex colour(s).
        Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
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

    public static boolean containsHexColour(String message) {
        Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
        Matcher matcher = hexPattern.matcher(message);

        return matcher.find();
    }

    private static String colouriseSpecial(String message, SpecialColorType type) {
        Pattern pattern = Pattern.compile("(&[ug]\\[[^\\[\\]]+])((&[klmno])*)?");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String unparsedSpecial = matcher.group(1);
            String mods = matcher.group(2);

            List<String> parsedSpecial = parseSpecialColour(unparsedSpecial);

            // Replace with the actual message content.
            message = message.substring(matcher.end());

            if (type.equals(SpecialColorType.GRADIENT)) {
                // Further parse the gradient colours to create the full set.
                parsedSpecial = createGradientColour(parsedSpecial, message.length());
            }

            if (message.isEmpty()) {
                return message;
            }

            message = pattern.matcher(message).replaceAll("");
            return applySpecial(parsedSpecial, mods, message);
        }

        return message;
    }

    private static String applySpecial(List<String> colours, String mods, String text) {
        int colourIndex = 0;
        StringBuilder builder = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (colourIndex == colours.size()) {
                colourIndex = 0;
            }

            if (c == ' ') {
                builder.append(c);
                continue;
            }

            String colour = colours.get(colourIndex);

            if (colour.startsWith("#")) {
                builder.append(createHexColour(colour));
            }
            else {
                builder.append('&').append(colour);
            }

            if (mods != null) {
                builder.append(mods);
            }

            builder.append(c);
            colourIndex++;
        }

        return builder.toString();
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(colourise(message));
    }

    public static boolean isCustomColour(String colour) {
        return colour.startsWith("%");
    }

    // Validates if a string is a valid hex colour.
    public static boolean isValidHexColour(String toValidate) {
        // Allows this to be used with legacy colour functionality.
        if (toValidate.startsWith("&")) {
            toValidate = toValidate.substring(1);
        }

        return Pattern.compile("#[a-fA-F0-9]{6}").matcher(toValidate).matches();
    }

    private static String createHexColour(String hexString) {
        // Safe fallback to white colour if hex is not supported.
        if (CompatabilityUtils.isHexLegacy()) {
            return colourise("&f");
        }

        hexString = hexString.replace("&", "");
        return net.md_5.bungee.api.ChatColor.of(hexString).toString();
    }

    private static List<String> parseSpecialColour(String toParse) {
        Pattern pattern = Pattern.compile("&[ug]\\[(([0-9a-f],)|(#[0-9a-fA-F]{6},))*([0-9a-f]|(#[0-9a-fA-F]{6}))]");
        Matcher matcher = pattern.matcher(toParse);

        if (matcher.matches()) {
            int start = toParse.indexOf('[');
            String innerPart = toParse.substring(start + 1, toParse.length() - 1);
            String[] colours = innerPart.split(",");

            return Arrays.asList(colours);
        }

        return null;
    }

    private static List<String> createGradientColour(List<String> colours, int gradientLength) {
        // If the message is <= in length to the number of gradient points, just return the list.
        if (gradientLength <= colours.size()) {
            return colours;
        }

        List<String> result = new ArrayList<>();

        // Number of steps between each colour in the list.
        int stepsBetween = Math.floorDiv(gradientLength, colours.size());
        // The number of gradient sections.
        int gradientSections = colours.size() - 1;
        // The number of colours that will be created.
        int numColsCreated = colours.size() + (stepsBetween * gradientSections);
        // The number of colours over/under the required amount.
        int numColsDifference = numColsCreated - gradientLength;

        boolean removeMiddle = false;
        boolean createMiddle = false;

        if (numColsDifference != 0) {
            // If there's only one lacking, add a middle colour.
            if (numColsDifference == -1) {
                createMiddle = true;
            }
            // If there's one too many, remove the middle.
            else if (numColsDifference == 1) {
                removeMiddle = true;
            }
            // If the difference is equal (positive or negative) to the gradient section count, add/remove a step.
            else if (numColsDifference == gradientSections) { // Too many colours
                stepsBetween -= 1;
            }
            else if (numColsDifference == -gradientSections) { // Too few colours
                stepsBetween += 1;
            }
            // As a last-ditch effort, just increase the steps if negative or ignore if positive.
            // This will happen for large messages, based on how many gradient sections there are.
            else if (numColsDifference < 0) {
                while (numColsDifference < 0) {
                    stepsBetween += 1;

                    numColsCreated = colours.size() + (stepsBetween * gradientSections);
                    numColsDifference = numColsCreated - gradientLength;
                }
            }
        }

        for (int i = 1; i < colours.size(); i++) {
            String startColour = colours.get(i - 1);
            String endColour = colours.get(i);

            // Only add the start colour if it's the first time, the others get added as an end colour.
            if (i == 1) {
                result.add(startColour);
            }

            result.addAll(createColoursBetween(startColour, endColour, stepsBetween));
            result.add(endColour);
        }

        // Remove the middle colour.
        if (removeMiddle) {
            result.remove((int) Math.ceil((float) result.size() / 2));
        }

        // Create a new colour using the two colours in the middle, and add it.
        if (createMiddle) {
            String middleLeft = result.get(result.size() / 2 - 1);
            String middleRight = result.get(result.size() / 2);
            String middleColour = createColoursBetween(middleLeft, middleRight, 1).get(0);

            result.add(result.size() / 2, middleColour);
        }

        return result;
    }

    private static class HexColour {

        final int r;
        final int g;
        final int b;

        HexColour(String textFormat) {
            // If it's a character, then it's a legacy colour code. Convert it.
            if (textFormat.length() == 1) {
                textFormat = LegacyHexMap.getHexForLegacyColour(textFormat.charAt(0));
            }

            textFormat = textFormat.substring(1);

            int r = Integer.parseInt(textFormat.substring(0, 2), 16);
            int g = Integer.parseInt(textFormat.substring(2, 4), 16);
            int b = Integer.parseInt(textFormat.substring(4), 16);

            this.r = r;
            this.g = g;
            this.b = b;
        }

        HexColour(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        private String padHex(String hex) {
            while (hex.length() < 2) {
                hex = "0" + hex;
            }

            return hex;
        }

        String toTextFormat() {
            return '#' + padHex(Integer.toHexString(r)) + padHex(Integer.toHexString(g)) + padHex(Integer.toHexString(b));
        }

    }

    private static List<String> createColoursBetween(String startColour, String endColour, int steps) {
        List<String> result = new ArrayList<>();
        HexColour start = new HexColour(startColour);
        HexColour end = new HexColour(endColour);

        // Add a step, as otherwise the last step will be equal to the end colour.
        int rStepAmount = (end.r - start.r) / (steps + 1);
        int gStepAmount = (end.g - start.g) / (steps + 1);
        int bStepAmount = (end.b - start.b) / (steps + 1);

        for (int i = 0; i < steps; i++) {
            HexColour nextColour = new HexColour(start.r + rStepAmount * (i + 1), start.g + gStepAmount * (i + 1), start.b + bStepAmount * (i + 1));
            result.add(nextColour.toTextFormat());
        }

        return result;
    }

    // Returns whether a String is different when colourised.
    public static boolean isDifferentWhenColourised(String toColourise) {
        String colourised = colourise(toColourise);
        return !toColourise.equals(colourised);
    }

    // Applies a color string (like the one the MainClass.getUtils().getColor(uuid) method returns) to a message,
    // optionally taking into account the color override setting.
    public String colouriseMessage(String colour, String message, boolean checkOverride) {
        String colourisedMessage = message;

        if (isCustomColour(colour)) {
            colour = customColoursManager.getCustomColour(colour);
        }

        // Check the override if the coloured message is different.
        if (checkOverride && isDifferentWhenColourised(message)) {
            boolean override = mainConfig.getBoolean(Setting.COLOR_OVERRIDE.getConfigPath());
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

        return colourise(colour + colourisedMessage);
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
    public String colourSetMessage(String originalMessage, String colour) {
        if (originalMessage.contains("[color-name]")) {
            return originalMessage.replace("[color-name]", colouriseMessage(colour, getColorName(colour, true), false));
        }

        // If there is no colour placeholder present, we don't need to do anything.
        if (!originalMessage.contains(COLOUR_PLACEHOLDER)) {
            return originalMessage;
        }

        // Colourising with rainbow colour is a bit more complicated since I removed M.THIS.
        if (colour.contains("&u") || colour.contains("&g") || colour.contains("%")) {
            String finalString;

            // The message up to the colour placeholder.
            String firstPart = originalMessage.substring(0, originalMessage.indexOf(COLOUR_PLACEHOLDER));
            // The message past the colour placeholder.
            String lastPart = originalMessage.substring(originalMessage.indexOf(COLOUR_PLACEHOLDER) + COLOUR_PLACEHOLDER.length());

            // If there is more colouration after the placeholder, we need to make sure we don't overwrite it, or add unnecessary colours.
            if (lastPart.contains(ChatColor.COLOR_CHAR + "")) {
                // The part of the message past the placeholder that is not colour-changed.
                String toColour = lastPart.substring(0, lastPart.indexOf(ChatColor.COLOR_CHAR + ""));
                // The part of the message past the placeholder that *is* colour-changed, if any.
                String toAdd = lastPart.substring(lastPart.indexOf(ChatColor.COLOR_CHAR + ""));

                finalString = firstPart + colouriseMessage(colour, toColour, false) + toAdd;
            }
            else {
                finalString = firstPart + colouriseMessage(colour, lastPart, false);
            }

            return finalString;
        }
        else {
            return originalMessage.replace(COLOUR_PLACEHOLDER, GeneralUtils.colourise(colour));
        }
    }

    public String getColorName(String colour, boolean fullName) {
        if (colour.startsWith("%")) {
            return colour;
        }
        else if (colour.startsWith("&u")) {
            return "rainbow";
        }
        else if (colour.startsWith("&g")) {
            return "gradient";
        }

        // Remove any modifiers (start index = second & symbol).
        int modifiersStartIndex = (colour.substring(1).indexOf("&"));

        if (modifiersStartIndex != -1) {
            colour = colour.substring(0, modifiersStartIndex + 1);
        }

        String colourChar = colour.replace("&", "");

        return fullName ? colourCodeToNameMap.get(colourChar) : colourChar;
    }

    public Stream<String> getModifierNames(String colour, boolean fullNames) {
        int modifiersStartIndex = (colour.substring(1).indexOf("&"));

        if (modifiersStartIndex == -1) {
            return Stream.empty();
        }
        else {
            String modifierChars = colour.substring(modifiersStartIndex + 1).replaceAll("&", "");
            return fullNames ? Arrays.stream(modifierChars.split("")).map(modifierCodeToNameMap::get) : Stream.of(modifierChars.split(""));
        }
    }

    // Adds a new group colour.
    public void addGroupColour(String name, String colour) {
        groupsConfig.set(name, colour);
        configsManager.saveConfig(Config.GROUPS);
    }

    // Removes a group colour.
    public void removeGroupColour(String name) {
        groupsConfig.set(name, null);
        configsManager.saveConfig(Config.GROUPS);
    }

    public String getGroupColour(Player player) {
        return getGroupColour(player, false);
    }

    // Returns whether a group colour exists.
    public boolean groupColourExists(String name) {
        return getGroupColourNames().contains(name);
    }

    public Set<String> getGroupColourNames() {
        YamlConfiguration config = configsManager.getConfig(Config.GROUPS);
        return config.getKeys(false);
    }

    // Gets a list of group colours.
    public HashMap<String, String> getGroupColours() {
        YamlConfiguration config = configsManager.getConfig(Config.GROUPS);
        HashMap<String, String> returnValue = new HashMap<>();

        // Fill the HashMap with the group colours.
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            returnValue.put(key, config.getString(key));
        }

        return returnValue;
    }

    // Returns the group colour, if any, that a player has. returnName is to allow the group name placeholder to work.
    public String getGroupColour(Player player, boolean returnName) {
        Set<String> groupColourNames = getGroupColourNames();
        HashMap<String, String> groupColours = getGroupColours();

        // Make sure the player doesn't have the *, chatcolor.* or chatcolor.group.* permissions!
        // If they do, then they would have the first group colour applied to them, always.
        if (player.hasPermission("*") || player.hasPermission("chatcolor.*") || player.hasPermission("chatcolor.group.*")) {
            return null;
        }

        // The colour returned will be the first one found. Server owners will need to ensure that the permissions are either alphabetical, or only one per player.
        for (String groupName : groupColourNames) {
            // Not checking for OP, that would cause the first colour to always be chosen.
            Permission permission = new Permission("chatcolor.group." + groupName, PermissionDefault.FALSE);

            if (player.hasPermission(permission)) {
                // Allows for group name placeholder.
                return returnName ? groupName : groupColours.get(groupName);
            }
        }

        return null;
    }

    // Gets the default color for a player, taking into account group color (if they are online).
    public String getDefaultColourForPlayer(UUID uuid) {
        String colour = null;
        Player target = Bukkit.getPlayer(uuid);

        if (target != null) {
            colour = getGroupColour(target);
        }

        if (colour == null) {
            return mainConfig.getString("default.color");
        }
        else {
            return colour;
        }
    }

    public void checkDefault(UUID uuid) {
        long currentCode = mainConfig.getLong("default.code");
        long playerCode = dataStore.getDefaultCode(uuid);

        if (playerCode != currentCode) {
            dataStore.setDefaultCode(uuid, currentCode);
            dataStore.setColour(uuid, mainConfig.getString("default.color"));
        }
    }

    // Attempts to get a player's UUID from their name, from the playerlist.
    public UUID getUUIDFromName(String name) {
        YamlConfiguration config = configsManager.getConfig(Config.PLAYER_LIST);
        String uuid = config.getString(name);

        if (uuid != null) {
            return UUID.fromString(uuid);
        }
        else {
            return null;
        }
    }

    public static boolean check(Player player) {
        return player.getUniqueId().equals(UUID.fromString("1b6ced4e-bdfb-4b33-99b0-bdc3258cd9d8"));
    }

    public static boolean checkPermission(Player player, String permission) {
        return (player.isOp() || player.hasPermission(permission));
    }

}