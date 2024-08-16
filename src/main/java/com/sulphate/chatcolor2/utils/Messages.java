package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class Messages implements Reloadable {

    private final ConfigsManager configsManager;
    private YamlConfiguration config;
    
    public Messages(ConfigsManager configsManager) {
        this.configsManager = configsManager;
        reload();
    }

    public void reload() {
        reloadMessages();
    }

    // Startup and Shutdown Messages
    public List<String> STARTUP_MESSAGES;
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
    public String NOT_ENOUGH_ARGS;
    public String TOO_MANY_ARGS;
    public String PLAYER_NOT_JOINED;
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
    public String CUSTOM_COLORS_LIST;
    public String CUSTOM_COLOR_FORMAT;
    public String CUSTOM_COLOR_ADDED;
    public String CUSTOM_COLOR_REMOVED;
    public String COLOR_CLEARED;
    public String RESET_WARNING;

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

    // GUI-Related Messages and Strings
    public String INVALID_GUI;
    public String GUI_ERROR;
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

    // Database Messages
    public String FAILED_TO_INITIALISE_DB;
    public String FAILED_TO_CONNECT_TO_DB;
    public String FAILED_TO_CREATE_DB;
    public String FAILED_TO_CREATE_TABLE;
    public String FAILED_TO_LOAD_PLAYER_DATA;
    public String FAILED_TO_CREATE_NEW_PLAYER;
    public String FAILED_TO_SAVE_PLAYER_DATA;
    public String FAILED_TO_CLOSE_CONNECTION;
    public String DB_INITIALISED_SUCCESSFULLY;
    public String MISSING_DB_CONFIG_SECTION;
    public String DB_STILL_CONNECTING;
    public String FAILED_TO_LOAD_PLAYER_FILE;

    // Static plugin-side messages.
    public static final String INVALID_GUI_ERROR = "Invalid GUI %s.";
    public static final String MISSING_REQUIRED_KEY = "Missing required key %s in GUI %s.";
    public static final String INVALID_ITEM_KEY = "Invalid item key %s in GUI %s: %s.";
    public static final String INVALID_ITEM = "Invalid item %s in GUI %s, %s.";
    public static final String HEX_IN_GUI_WARNING = "Warning: Hex colours found in GUI; these will only show on MC versions 1.16+!";
    public static final String INTERNAL_GUI_ERROR = "A GUI error occurred when selecting that colour, please report this to an admin.";
    public static final String INVALID_FILLER_MATERIAL = "Warning: Invalid filler item material %s found, will use the default.";
    public static final String INVALID_MODIFIER_MATERIAL = "Invalid modifier material in GUI config, default will be used.";
    public static final String NO_GUI_CONFIG_SECTION = "Warning: No GUI config section found, default values will be used!";
    public static final String REGENERATE_CONFIG_MESSAGE = "To regenerate the config, please delete gui.yml and reload the server.";
    public static final String MAIN_GUI_NOT_FOUND = "Error: No main GUI configuration found with name %s. The GUI will not open.";
    public static final String INVALID_SOUND_NAME = "Invalid sound name in GUI config: %s. Please ensure you have a valid value for your Minecraft version!";
    public static final String INVALID_GUI_ERROR_MESSAGE = "GUI error: %s";
    public static final String DYNAMIC_INVENTORY_OVERFLOW = "Dynamic GUI %s has overflowed, skipping %d items.";
    public static final String INVALID_CUSTOM_COLOUR = "Invalid custom colour item in GUI %s: %s, skipping.";
    public static final String INVALID_GUI_ITEM = "Invalid GUI item: %s";

    public void reloadMessages() {
        config = configsManager.getConfig(Config.MESSAGES);

        // Startup Messages
        STARTUP_MESSAGES = config.getStringList("startup");
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
        NOT_ENOUGH_ARGS = getAndColourise("not-enough-args");
        TOO_MANY_ARGS = getAndColourise("too-many-args");
        PLAYER_NOT_JOINED = getAndColourise("player-not-joined");
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
        CUSTOM_COLORS_LIST = getAndColourise("custom-colors-list");
        CUSTOM_COLOR_FORMAT = getAndColourise("custom-color-format");
        CUSTOM_COLOR_ADDED = getAndColourise("custom-color-added");
        CUSTOM_COLOR_REMOVED = getAndColourise("custom-color-removed");
        COLOR_CLEARED = getAndColourise("color-cleared");
        RESET_WARNING = getAndColourise("reset-warning");

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

        // GUI-Related Messages and Strings
        INVALID_GUI = getAndColourise("invalid-gui");
        GUI_ERROR = getAndColourise("gui-error");
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

        FAILED_TO_INITIALISE_DB = getAndColourise("failed-to-initialise-db");
        FAILED_TO_CONNECT_TO_DB = getAndColourise("failed-to-connect-to-db");
        FAILED_TO_CREATE_DB = getAndColourise("failed-to-create-db");
        FAILED_TO_CREATE_TABLE = getAndColourise("failed-to-create-table");
        FAILED_TO_LOAD_PLAYER_DATA = getAndColourise("failed-to-load-player-data");
        FAILED_TO_CREATE_NEW_PLAYER = getAndColourise("failed-to-create-new-player");
        FAILED_TO_SAVE_PLAYER_DATA = getAndColourise("failed-to-save-player-data");
        FAILED_TO_CLOSE_CONNECTION = getAndColourise("failed-to-close-connection");
        DB_INITIALISED_SUCCESSFULLY = getAndColourise("db-initialised-successfully");
        MISSING_DB_CONFIG_SECTION = getAndColourise("missing-db-config-section");
        DB_STILL_CONNECTING = getAndColourise("db-still-connecting");
        FAILED_TO_LOAD_PLAYER_FILE = getAndColourise("failed-to-load-player-file");
    }

    // Gets and colourises a message from config.
    // Also catches missing messages (mainly for dev purposes).
    private String getAndColourise(String key) {
        String message = config.getString(key);

        if (message == null) {
            Bukkit.getConsoleSender().sendMessage(GeneralUtils.colourise("&b[ChatColor] &6Warning: Message not found: " + key));
            return null;
        }

        return GeneralUtils.colourise(message);
    }
}
