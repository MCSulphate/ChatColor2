package com.sulphate.chatcolor2.managers;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.utils.Config;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class ConfigsManager {

    private final ConfigUtils configUtils;

    private final HashMap<String, YamlConfiguration> configs;

    public ConfigsManager(ConfigUtils configUtils) {
        this.configUtils = configUtils;
        configs = new HashMap<>();

        loadAllConfigs();

        File playersFolder = new File(ChatColor.getPlugin().getDataFolder(), "players");
        playersFolder.mkdir();
    }

    // (re)loads all configs.
    public void loadAllConfigs() {
        configs.clear();

        for (Config config : Config.values()) {
            String fileName = config.getFilename();

            if (fileName.equals(Config.PLAYER_LIST.getFilename())) {
                configs.put(fileName, configUtils.getConfigOrCreateBlank(fileName));
            }
            else {
                configs.put(fileName, configUtils.getConfigOrCopyDefault(fileName));
            }
        }
    }

    // Returns a given config.
    public YamlConfiguration getConfig(Config config) {
        return configs.get(config.getFilename());
    }

    // Saves a config.
    public void saveConfig(Config config) {
        configUtils.saveConfig(config.getFilename());
    }

    // Loads a player config.
    public void loadPlayerConfig(UUID uuid) {
        String filePath = "players" + File.separator + uuid + ".yml";
        configs.put(uuid + ".yml", configUtils.getConfigOrCreateBlank(filePath));
    }

    // Gets a player config.
    public YamlConfiguration getPlayerConfig(UUID uuid) {
        return configs.get(uuid + ".yml");
    }

}
