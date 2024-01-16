package com.sulphate.chatcolor2.managers;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigsManager {

    private HashMap<String, YamlConfiguration> configs;
    private final File PLAYERS_FOLDER = new File(ChatColor.getPlugin().getDataFolder(), "players");

    public ConfigsManager() {
        configs = new HashMap<>();

        loadAllConfigs();
        PLAYERS_FOLDER.mkdir();
    }

    // (re)loads all configs.
    public void loadAllConfigs() {
        String[] fileNames = { "config.yml", "messages.yml", "player-list.yml", "groups.yml", "gui.yml", "custom-colors.yml" };
        configs.clear();

        for (String fileName : fileNames) {
            File file = new File(ChatColor.getPlugin().getDataFolder(), fileName);
            configs.put(fileName, YamlConfiguration.loadConfiguration(file));
        }
    }

    // Returns a given config.
    public YamlConfiguration getConfig(String fileName) {
        return configs.get(fileName);
    }

    // Saves a config.
    public void saveConfig(String configName) {
        ChatColor.getPlugin().getSaveScheduler().saveConfigWithDelay(configName, getConfig(configName));
    }

    // Loads a player config.
    public void loadPlayerConfig(UUID uuid) {
        File file = new File(PLAYERS_FOLDER, uuid.toString() + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException ex) {
                Bukkit.getConsoleSender().sendMessage(GeneralUtils.colourise("&b[ChatColor] &cError: Failed to create player config file."));
                return;
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        configs.put(uuid + ".yml", config);
    }

    // Saves a player config.
    public void savePlayerConfig(UUID uuid) {
        ChatColor.getPlugin().getSaveScheduler().saveConfigWithDelay("players" + File.separator + uuid.toString() + ".yml", getConfig(uuid.toString() + ".yml"));
    }

    // Gets a player config.
    public YamlConfiguration getPlayerConfig(UUID uuid) {
        return configs.get(uuid.toString() + ".yml");
    }

}
