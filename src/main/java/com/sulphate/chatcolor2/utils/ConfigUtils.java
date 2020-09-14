package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.managers.ConfigsManager;
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

    private ConfigsManager configsManager;

    public ConfigUtils(ConfigsManager configsManager) {
        this.configsManager = configsManager;
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
        configsManager.getConfig("config.yml").set("settings." + key, value);
        configsManager.saveConfig("config.yml");
    }

    // Gets a player's colour (config must be loaded).
    public String getColour(UUID uuid) {
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        String colour = config.getString("color");

        // If their colour is null / empty, set it to the default.
        if (colour == null || colour.equals("")) {
            setColour(uuid, getCurrentDefaultColour());
            return getCurrentDefaultColour();
        }

        return colour;
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
    public Long getCurrentDefaultCode() {
        YamlConfiguration config = configsManager.getConfig("config.yml");
        return config.getLong("default.code");
    }

    // Gets the current default colour.
    public String getCurrentDefaultColour() {
        YamlConfiguration config = configsManager.getConfig("config.yml");
        return config.getString("default.color");
    }

    // Creates a new default colour, setting it in the config.
    public void createNewDefaultColour(String colour) {
        // Current millis time will always be unique.
        String code = System.currentTimeMillis() + "";

        setAndSave("config.yml", "default.code", code);
        setAndSave("config.yml", "default.color", colour);
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
        setAndSave("player-list.yml", name, uuid.toString());
    }

    // Gets a list of custom colours.
    public HashMap<String, String> getCustomColours() {
        YamlConfiguration config = configsManager.getConfig("colors.yml");
        HashMap<String, String> returnValue = new HashMap<>();

        // Fill the HashMap with the custom colours.
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            returnValue.put(key, config.getString(key));
        }

        return returnValue;
    }

    // Returns whether a custom colour exists.
    public boolean customColourExists(String name) {
        return getCustomColours().containsKey(name);
    }

    // Adds a new custom colour.
    public void addCustomColour(String name, String colour) {
        setAndSave("colors.yml", name, colour);
    }

    // Removes a custom colour.
    public void removeCustomColour(String name) {
        setAndSave("colors.yml", name, null);
    }

    // Returns the custom colour, if any, that a player has.
    public String getCustomColour(Player player) {
        HashMap<String, String> customColours = getCustomColours();

        // Make sure the player doesn't have the *, chatcolor.* or chatcolor.custom.* permissions!
        // If they do, then they would have the first custom colour applied to them, always.
        if (player.hasPermission("*") || player.hasPermission("chatcolor.*") || player.hasPermission("chatcolor.custom.*")) {
            return null;
        }

        // The colour returned will be the first one found. Server owners will need to ensure that the permissions are either alphabetical, or only one per player.
        for (String key : customColours.keySet()) {
            // Not checking for OP, that would cause the first colour to always be chosen.
            Permission permission = new Permission("chatcolor.custom." + key, PermissionDefault.FALSE);
            if (player.hasPermission(permission)) {
                return customColours.get(key);
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

    // Gets the default color for a player, taking into account custom color (if they are online).
    public String getDefaultColourForPlayer(UUID uuid) {
        String colour = null;
        Player target = Bukkit.getPlayer(uuid);

        if (target != null) {
            colour = getCustomColour(target);
        }

        if (colour == null) {
            return getCurrentDefaultColour();
        }
        else {
            return colour;
        }
    }

}
