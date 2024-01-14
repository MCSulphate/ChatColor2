package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfigUtils {

    private final ConfigsManager configsManager;
    private final CustomColoursManager customColoursManager;

    public ConfigUtils(ConfigsManager configsManager, CustomColoursManager customColoursManager) {
        this.configsManager = configsManager;
        this.customColoursManager = customColoursManager;
    }

    // Returns the list of startup messages.
    public List<String> getStartupMessages() {
        return configsManager.getConfig("messages.yml").getStringList("startup");
    }

    // Gets a message from config.
    public String getMessage(String key) {
        return configsManager.getConfig("messages.yml").getString(key);
    }

    // Gets a setting from config.
    public Object getSetting(String key) {
        return configsManager.getConfig("config.yml").get("settings." + key);
    }

    // Sets a setting.
    public void setSetting(String key, Object value) {
        configsManager.getConfig(Config.MAIN_CONFIG).set("settings." + key, value);
        configsManager.saveConfig(Config.MAIN_CONFIG);
    }

    // Gets a player's colour (config must be loaded).
    public String getColour(UUID uuid) {
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        String colour = config.getString("color");
        String newColour = colour;

        // If their colour is null, set it to "".
        if (colour == null) {
            newColour = "";
        }

        // If it's empty and default colour is enabled, set it to the default.
        if (newColour.isEmpty() && (boolean) getSetting("default-color-enabled")) {
            newColour = getCurrentDefaultColour();
        }

        // If they have a custom colour, make sure it still exists.
        if (newColour.startsWith("%") && customColoursManager.getCustomColour(colour) == null) {
            // Otherwise, set to default.
            newColour = getCurrentDefaultColour();
        }

        // Update their colour if necessary.
        if (!newColour.equals(colour)) {
            setColour(uuid, newColour);
        }

        return newColour;
    }

    // Sets a player's colour (config must be loaded).
    public void setColour(UUID uuid, String colour) {
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        config.set("color", colour);
        configsManager.savePlayerConfig(uuid);
    }

    // Gets a player's default-code.
    public long getDefaultCode(UUID uuid) {
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        return config.getLong("default-code");
    }

    // Sets a player's default-code.
    public void setDefaultCode(UUID uuid, long code) {
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        config.set("default-code", code);
        configsManager.savePlayerConfig(uuid);
    }

    // Gets the current default code.
    public long getCurrentDefaultCode() {
        YamlConfiguration config = configsManager.getConfig("config.yml");
        return config.getInt("default.code");
    }

    // Gets the current default colour.
    public String getCurrentDefaultColour() {
        YamlConfiguration config = configsManager.getConfig("config.yml");
        return config.getString("default.color");
    }

    // Creates a new default colour, setting it in the config.
    public void createNewDefaultColour(String colour) {
        // Current millis time will always be unique.
        long code = (System.currentTimeMillis() / 1000);

        setAndSave(Config.MAIN_CONFIG, "default.code", code);
        setAndSave(Config.MAIN_CONFIG, "default.color", colour);
    }

    // Attempts to get a player's UUID from their name, from the playerlist.
    public UUID getUUIDFromName(String name) {
        YamlConfiguration config = configsManager.getConfig("player-list.yml");
        String uuid = config.getString(name);

        if (uuid != null) {
            return UUID.fromString(uuid);
        }
        else {
            return null;
        }
    }

    // Updates a player's playerlist entry.
    public void updatePlayerListEntry(String name, UUID uuid) {
        setAndSave(Config.PLAYER_LIST, name, uuid.toString());
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

    // Returns whether a group colour exists.
    public boolean groupColourExists(String name) {
        return getGroupColourNames().contains(name);
    }

    // Adds a new group colour.
    public void addGroupColour(String name, String colour) {
        setAndSave(Config.GROUPS, name, colour);
    }

    // Removes a group colour.
    public void removeGroupColour(String name) {
        setAndSave(Config.GROUPS, name, null);
    }

    public String getGroupColour(Player player) {
        return getGroupColour(player, false);
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

    // Sets and saves a config's value.
    private void setAndSave(String configName, String key, Object value) {
        YamlConfiguration config = configsManager.getConfig(configName);
        config.set(key, value);
        configsManager.saveConfig(configName);
    }

    // Gets the default color for a player, taking into account group color (if they are online).
    public String getDefaultColourForPlayer(UUID uuid) {
        String colour = null;
        Player target = Bukkit.getPlayer(uuid);

        if (target != null) {
            colour = getGroupColour(target);
        }

        if (colour == null) {
            return getCurrentDefaultColour();
        }
        else {
            return colour;
        }
    }

}
