package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;
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
        return config.getString("color");
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
        long code = System.currentTimeMillis();

        YamlConfiguration config = configsManager.getConfig("config.yml");
        config.set("default.code", code);
        config.set("default.color", colour);
        configsManager.saveConfig("config.yml");
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
        YamlConfiguration config = configsManager.getConfig("player-list.yml");
        config.set(name, uuid.toString());
        configsManager.saveConfig("player-list.yml");
    }

}
