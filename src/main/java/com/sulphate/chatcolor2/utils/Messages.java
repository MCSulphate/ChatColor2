package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;

import org.bukkit.Bukkit;

public class Messages {
    
    private ConfigUtils utils;
    
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
    public String RAINBOW;

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

    // Custom-Colour Messages
    public String CUSTOM_COLOR_EXISTS;
    public String CUSTOM_COLOR_NOT_EXISTS;
    public String USING_CUSTOM_COLOR;
    public String ADDED_CUSTOM_COLOR;
    public String REMOVED_CUSTOM_COLOR;
    public String CUSTOM_COLOR_LIST;
    public String CUSTOM_COLOR_FORMAT;

    // Confirmation and Settings Messages
    public String CONFIRM;
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
    public String GUI_TITLE;
    public String GUI_COLOR_ALREADY_SET;
    public String GUI_UNAVAILABLE;
    public String GUI_AVAILABLE;
    public String GUI_ACTIVE;
    public String GUI_CLICK_TO_TOGGLE;
    public String GUI_CLICK_TO_SELECT;
    public String GUI_SELECTED;
    public String GUI_INACTIVE;

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
        RAINBOW = getAndColourise("rainbow");

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

        // Custom-Color Messages
        CUSTOM_COLOR_EXISTS = getAndColourise("custom-color-exists");
        CUSTOM_COLOR_NOT_EXISTS = getAndColourise("custom-color-not-exists");
        USING_CUSTOM_COLOR = getAndColourise("using-custom-color");
        ADDED_CUSTOM_COLOR = getAndColourise("added-custom-color");
        REMOVED_CUSTOM_COLOR = getAndColourise("removed-custom-color");
        CUSTOM_COLOR_LIST = getAndColourise("custom-color-list");
        CUSTOM_COLOR_FORMAT = getAndColourise("custom-color-format");

        // Confirmation and Settings Messages
        CONFIRM = getAndColourise("confirm");
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
        GUI_TITLE = getAndColourise("gui-title");
        GUI_COLOR_ALREADY_SET = getAndColourise("gui-color-already-set");
        GUI_UNAVAILABLE = getAndColourise("gui-unavailable");
        GUI_AVAILABLE = getAndColourise("gui-available");
        GUI_ACTIVE = getAndColourise("gui-active");
        GUI_CLICK_TO_TOGGLE = getAndColourise("gui-click-to-toggle");
        GUI_CLICK_TO_SELECT = getAndColourise("gui-click-to-select");
        GUI_SELECTED = getAndColourise("gui-selected");
        GUI_INACTIVE = getAndColourise("gui-inactive");

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
