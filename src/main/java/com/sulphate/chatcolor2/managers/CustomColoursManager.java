package com.sulphate.chatcolor2.managers;

import com.sulphate.chatcolor2.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CustomColoursManager {

    private final ConfigsManager configsManager;

    private final Map<String, String> customColoursMap;
    private YamlConfiguration config;

    public CustomColoursManager(ConfigsManager configsManager) {
        this.configsManager = configsManager;
        customColoursMap = new HashMap<>();

        reload();
    }

    // Returns the actual name (inclusive of %).
    public String addCustomColour(String name, String colour) {
        if (customColoursMap.containsKey(name)) {
            return null;
        }
        else if (!name.startsWith("%")) {
            name = '%' + name;
        }

        customColoursMap.put(name, colour);
        config.set(name.substring(1), colour); // Substring call to remove the %
        configsManager.saveConfig(Config.CUSTOM_COLOURS);
        return name;
    }

    // Returns the removed colour.
    public String removeCustomColour(String name) {
        if (!customColoursMap.containsKey(name)) {
            return null;
        }
        else if (!name.startsWith("%")) {
            name = '%' + name;
        }

        String removedColour = customColoursMap.remove(name);
        config.set(name.substring(1), null);
        configsManager.saveConfig(Config.CUSTOM_COLOURS);
        return removedColour;
    }

    public String getCustomColour(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        if (!name.startsWith("%")) {
            name = '%' + name;
        }

        return customColoursMap.get(name);
    }

    public Map<String, String> getCustomColours() {
        return customColoursMap;
    }

    public void reload() {
        customColoursMap.clear();
        config = configsManager.getConfig(Config.CUSTOM_COLOURS);
        Set<String> keys = config.getKeys(false);

        for (String key : keys) {
            String colour = config.getString(key);
            customColoursMap.put('%' + key, colour);
        }
    }

}
