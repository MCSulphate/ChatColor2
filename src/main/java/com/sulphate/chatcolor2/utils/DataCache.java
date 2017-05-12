package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class DataCache {

    private HashMap<String, String> settings = new HashMap<>();
    private HashMap<String, String> colours = new HashMap<>();
    private FileConfiguration config = MainClass.get().getConfig();

    public DataCache() {
        loadAllData();
    }

    public void loadAllData() {
        loadMessages();
        loadSettings();
        loadColours();
    }
    public void loadSettings() {
        settings = new HashMap<>();
        ConfigurationSection settingsSection = MainClass.get().getConfig().getConfigurationSection("settings");
        Map<String, Object> settings = settingsSection.getValues(false);
        for(Map.Entry<String, Object> entry : settings.entrySet()) {
            try {
                settings.put(entry.getKey(), entry.getValue());
            } catch(Exception e) {
                MainClass.setPluginEnabled(false);
            }
        }
    }
    public void loadColours() {

    }
    public void loadMessages() {

    }

    public void saveAllData() {
        saveSettings();
        saveColours();
    }
    public void saveSettings() {

    }
    public void saveColours() {

    }

}
