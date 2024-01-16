package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;
import org.bukkit.Bukkit;

public class Messages {
    
    private final ConfigUtils utils;
    
    public Messages(ConfigUtils utils) {
        this.utils = utils;
        reloadMessages();
    }

    // Startup and Shutdown Messages
    public String METRICS_ENABLED;
    public String METRICS_DISABLED;
    public String PLACEHOLDERS_ENABLED;
    public String PLACEHOLDERS_DISABLED;
    public String SHUTDOWN;
    public String LEGACY_DETECTED;

    // General Messages and Strings
    public String PREFIX;
    public String PLUGIN_NOTIFICATION = GeneralUtils.colourise("Server is running ChatColor 2 version &c" + ChatColor.getPlugin().getDescription().getVersion());
    public String COMMAND_EXISTS;
    public String COLORS;
    public String MODIFIERS;

    // Command-Related Messages
    public String HELP;
    public String NOT_ENOUGH_ARGS;
    public String TOO_MANY_ARGS;
    public String PLAYER_NOT_JOINED;
    public String PLAYERS_ONLY;
    public String NO_PERMISSIONS;
    public String NO_COLOR_PERMS;
    public String NO_MOD_PERMS;
    public String INVALID_COLOR;
    public String INVALID_MODIFIER;
    public String INVALID_COMMAND;
    public String INVALID_SETTING;
    public String NEEDS_BOOLEAN;
    public String NEEDS_NUMBER;
    public String CURRENT_COLOR;
    public String SET_OWN_COLOR;
    public String SET_OTHERS_COLOR;
    public String PLAYER_SET_YOUR_COLOR;
    public String RELOADED_MESSAGES;
    public String AVAILABLE_COLORS;
    public String MODIFIER_NOT_IN_COLOR;
    public String MODIFIER_ALREADY_IN_COLOR;
    public String NO_HEX_PERMISSIONS;
    public String HEX_ACCESS;
    public String NO_HEX_SUPPORT;
    public String CANNOT_MODIFY_CUSTOM_COLOR;
    public String NO_CUSTOM_COLOR_PERMISSIONS;
    public String INVALID_CUSTOM_COLOR;
    public String CUSTOM_COLOR_EXISTS;
    public String INCORRECT_CUSTOM_COLOR;
    public String CUSTOM_COLORS_LIST;
    public String CUSTOM_COLOR_FORMAT;
    public String CUSTOM_COLOR_ADDED;
    public String CUSTOM_COLOR_REMOVED;

    // Group-Colour Messages
    public String GROUP_COLOR_EXISTS;
    public String GROUP_COLOR_NOT_EXISTS;
    public String USING_GROUP_COLOR;
    public String ADDED_GROUP_COLOR;
    public String REMOVED_GROUP_COLOR;
    public String GROUP_COLOR_LIST;
    public String GROUP_COLOR_FORMAT;

    // Confirmation and Settings Messages
    public String CONFIRM_ARE_YOU_SURE;
    public String CONFIRM_FINALISE;
    public String DID_NOT_CONFIRM;
    public String ALREADY_CONFIRMING;
    public String NOTHING_TO_CONFIRM;
    public String ALREADY_SET;
    public String IS_CURRENTLY;
    public String TO_CHANGE;
    public String CHANGE_SUCCESS;
    public String CONFIGS_RESET;
    public String INVALID_SEQUENCE;

    // GUI-Related Messages and Strings
    public String INVALID_GUI;
    public String GUI_COLOR_ALREADY_SET;
    public String GUI_UNAVAILABLE;
    public String GUI_AVAILABLE;

    // Colour Strings
    public String BLACK;
    public String DARK_BLUE;
    public String DARK_GREEN;
    public String DARK_AQUA;
    public String DARK_RED;
    public String DARK_PURPLE;
    public String GOLD;
    public String GRAY;
    public String DARK_GRAY;
    public String BLUE;
    public String GREEN;
    public String AQUA;
    public String RED;
    public String LIGHT_PURPLE;
    public String YELLOW;
    public String WHITE;

    // Modifier Strings
    public String OBFUSCATED;
    public String BOLD;
    public String STRIKETHROUGH;
    public String UNDERLINED;
    public String ITALIC;
    
    public void reloadMessages() {
        // Startup Messages
        METRICS_ENABLED = getAndColourise("metrics-enabled");
        METRICS_DISABLED = getAndColourise("metrics-disabled");
        PLACEHOLDERS_ENABLED = getAndColourise("placeholders-enabled");
        PLACEHOLDERS_DISABLED = getAndColourise("placeholders-disabled");
        SHUTDOWN = getAndColourise("shutdown");
        LEGACY_DETECTED = getAndColourise("legacy-detected");

        // General Messages and Strings
        PREFIX = getAndColourise("prefix");
        COMMAND_EXISTS = getAndColourise("command-exists");
        COLORS = getAndColourise("colors");
        MODIFIERS = getAndColourise("modifiers");

        // Command-Related Messages
        HELP = getAndColourise("help");
        NOT_ENOUGH_ARGS = getAndColourise("not-enough-args");
        TOO_MANY_ARGS = getAndColourise("too-many-args");
        PLAYER_NOT_JOINED = getAndColourise("player-not-joined");
        PLAYERS_ONLY = getAndColourise("players-only");
        NO_PERMISSIONS = getAndColourise("no-permissions");
        NO_COLOR_PERMS = getAndColourise("no-color-perms");
        NO_MOD_PERMS = getAndColourise("no-mod-perms");
        INVALID_COLOR = getAndColourise("invalid-color");
        INVALID_MODIFIER = getAndColourise("invalid-modifier");
        INVALID_COMMAND = getAndColourise("invalid-command");
        INVALID_SETTING = getAndColourise("invalid-setting");
        NEEDS_BOOLEAN = getAndColourise("needs-boolean");
        NEEDS_NUMBER = getAndColourise("needs-number");
        CURRENT_COLOR = getAndColourise("current-color");
        SET_OWN_COLOR = getAndColourise("set-own-color");
        SET_OTHERS_COLOR = getAndColourise("set-others-color");
        PLAYER_SET_YOUR_COLOR = getAndColourise("player-set-your-color");
        RELOADED_MESSAGES = getAndColourise("reloaded-messages");
        AVAILABLE_COLORS = getAndColourise("available-colors");
        MODIFIER_NOT_IN_COLOR = getAndColourise("modifier-not-in-color");
        MODIFIER_ALREADY_IN_COLOR = getAndColourise("modifier-already-in-color");
        NO_HEX_PERMISSIONS = getAndColourise("no-hex-permissions");
        HEX_ACCESS = getAndColourise("hex-access");
        NO_HEX_SUPPORT = getAndColourise("no-hex-support");
        CANNOT_MODIFY_CUSTOM_COLOR = getAndColourise("cannot-modify-custom-color");
        NO_CUSTOM_COLOR_PERMISSIONS = getAndColourise("no-custom-color-permissions");
        INVALID_CUSTOM_COLOR = getAndColourise("invalid-custom-color");
        CUSTOM_COLOR_EXISTS = getAndColourise("custom-color-exists");
        INCORRECT_CUSTOM_COLOR = getAndColourise("incorrect-custom-color");
        CUSTOM_COLORS_LIST = getAndColourise("custom-colors-list");
        CUSTOM_COLOR_FORMAT = getAndColourise("custom-color-format");
        CUSTOM_COLOR_ADDED = getAndColourise("custom-color-added");
        CUSTOM_COLOR_REMOVED = getAndColourise("custom-color-removed");

        // Group-Color Messages
        GROUP_COLOR_EXISTS = getAndColourise("group-color-exists");
        GROUP_COLOR_NOT_EXISTS = getAndColourise("group-color-not-exists");
        USING_GROUP_COLOR = getAndColourise("using-group-color");
        ADDED_GROUP_COLOR = getAndColourise("added-group-color");
        REMOVED_GROUP_COLOR = getAndColourise("removed-group-color");
        GROUP_COLOR_LIST = getAndColourise("group-color-list");
        GROUP_COLOR_FORMAT = getAndColourise("group-color-format");

        // Confirmation and Settings Messages
        CONFIRM_ARE_YOU_SURE = getAndColourise("confirm-are-you-sure");
        CONFIRM_FINALISE = getAndColourise("confirm-finalise");
        DID_NOT_CONFIRM = getAndColourise("did-not-confirm");
        ALREADY_CONFIRMING = getAndColourise("already-confirming");
        NOTHING_TO_CONFIRM = getAndColourise("nothing-to-confirm");
        ALREADY_SET = getAndColourise("already-set");
        IS_CURRENTLY = getAndColourise("is-currently");
        TO_CHANGE = getAndColourise("to-change");
        CHANGE_SUCCESS = getAndColourise("change-success");
        CONFIGS_RESET = getAndColourise("configs-reset");
        INVALID_SEQUENCE = getAndColourise("invalid-sequence");

        // GUI-Related Messages and Strings
        INVALID_GUI = getAndColourise("invalid-gui");
        GUI_COLOR_ALREADY_SET = getAndColourise("gui-color-already-set");
        GUI_UNAVAILABLE = getAndColourise("gui-unavailable");
        GUI_AVAILABLE = getAndColourise("gui-available");

        // Colour Strings
        BLACK = getAndColourise("black");
        DARK_BLUE = getAndColourise("dark-blue");
        DARK_GREEN = getAndColourise("dark-green");
        DARK_AQUA = getAndColourise("dark-aqua");
        DARK_RED = getAndColourise("dark-red");
        DARK_PURPLE = getAndColourise("dark-purple");
        GOLD = getAndColourise("gold");
        GRAY = getAndColourise("gray");
        DARK_GRAY = getAndColourise("dark-gray");
        BLUE = getAndColourise("blue");
        GREEN = getAndColourise("green");
        AQUA = getAndColourise("aqua");
        RED = getAndColourise("red");
        LIGHT_PURPLE = getAndColourise("light-purple");
        YELLOW = getAndColourise("yellow");
        WHITE = getAndColourise("white");

        // Modifier Strings
        OBFUSCATED = getAndColourise("obfuscated");
        BOLD = getAndColourise("bold");
        STRIKETHROUGH = getAndColourise("strikethrough");
        UNDERLINED = getAndColourise("underlined");
        ITALIC = getAndColourise("italic");
    }

    // Gets and colourises a message from config.
    // Also catches missing messages (mainly for dev purposes).
    private String getAndColourise(String key) {
        String message = utils.getMessage(key);

        if (message == null) {
            Bukkit.getConsoleSender().sendMessage(GeneralUtils.colourise("&b[ChatColor] &6Warning: Message not found: " + key));
            return null;
        }

        return GeneralUtils.colourise(utils.getMessage(key));
    }
}
