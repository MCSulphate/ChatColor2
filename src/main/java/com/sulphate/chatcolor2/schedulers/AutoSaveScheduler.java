package com.sulphate.chatcolor2.schedulers;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

public class AutoSaveScheduler {

    private ChatColor plugin;

    private BukkitTask task;
    private ConcurrentHashMap<String, YamlConfiguration> configsToSave;
    private int saveInterval;

    public AutoSaveScheduler(int saveInterval) {
        this.plugin = ChatColor.getPlugin();
        this.configsToSave = new ConcurrentHashMap<>();
        this.saveInterval = saveInterval;

        run();
    }

    private void run() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::saveAllConfigs, saveInterval * 20 * 60, saveInterval * 20 * 60);
    }

    public void setSaveInterval(int saveInterval) {
        this.saveInterval = saveInterval;
        restart();
    }

    private void restart() {
        task.cancel();
        run();
    }

    public void stop() {
        task.cancel();
        saveAllConfigs();
    }

    // Adds a config to the HashMap to be saved.
    public void saveConfigWithDelay(String configName, YamlConfiguration config) {
        configsToSave.remove(configName);
        configsToSave.put(configName, config);
    }

    // Saves all pending configs to files.
    private void saveAllConfigs() {
        for (String configName : configsToSave.keySet()) {
            YamlConfiguration config = configsToSave.get(configName);
            File file = new File(plugin.getDataFolder(), configName);

            try {
                config.save(file);
            }
            catch (IOException ex) {
                Bukkit.getConsoleSender().sendMessage(GeneralUtils.colourise("&cError: Failed to save a config (" + configName + ")!"));
            }

            configsToSave.remove(configName);
        }
    }

}
