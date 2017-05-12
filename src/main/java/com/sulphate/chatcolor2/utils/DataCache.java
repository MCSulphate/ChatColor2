package com.sulphate.chatcolor2.utils; //TODO: Remove ColorUtils completely when done, come up with a better name for this class.

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class DataCache {

    private HashMap<String, String> settings = new HashMap<>();
    private HashMap<String, String> colors = new HashMap<>();
    private HashMap<String, String> messages = new HashMap<>();
    private HashMap<String, String> defcodes = new HashMap<>(); //TODO: Set default defcode of 0 on join.
    private FileConfiguration playerlist;

    public DataCache() {
        loadAllData();
    }

    public boolean loadAllData() {
        loadPlayerList();
        return (loadMessages() && loadSettings() && loadPlayerData());
    }
    public void loadPlayerList() {
        playerlist = FileUtils.getPlayerListConfig();  
    }
    public boolean loadSettings() {
        settings = new HashMap<>();
        
        reloadConfig();
        ConfigurationSection settingsSection = MainClass.get().getConfig().getConfigurationSection("settings");
        Map<String, Object> settings = settingsSection.getValues(false);
        
        for(Map.Entry<String, Object> entry : settings.entrySet()) {
            try {
                this.settings.put(entry.getKey(), entry.getValue());
            } catch(Exception e) {
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errdet + "§bError loading settings. Please check they are all correct!");
                return false;
            }
        }
        
        return true;
    }
    public boolean loadMessages() {
        messages = new HashMap<>();
        
        reloadConfig();
        ConfigurationSection messagesSection = MainClass.get().getConfig().getConfigurationSection("messages");
        Map<String, Object> messages = messagesSection.getValues(false);
        
        for(Map.Entry<String, Object> entry : messages.entrySet()) {
            try {
                this.messages.put(entry.getKey(), entry.getValue());
            } catch(Exception e) {
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errdet + "§bError loading messages. Please check they are all correct!");
                return false;
            }
        }
        
        return true;
    }
    public boolean loadPlayerData() {
        for (string key : playerlist.getKeys()) {
            String uuid = playerlist.get(key);
            String color = ColorUtils.getColor(uuid) == null || ColorUtils.getColor(uuid) == "" ? "§f" : ColorUtils.getColor(uuid);
            String defcode = ColorUtils.getDefaultCode(uuid) == null || ColorUtils.getDefaultCode(uuid) == "" ? "0" : ColorUtils.getDefaultCode(uuid);
            colors.put(uuid, color);
            defcodes.put(uuid, defcode);
        }
    }

    public void saveAllData() {
        saveSettings();
        savePlayerData();
    }
    public void savePlayerList() {
        FileUtils.saveConfig(playerlist, FileUtils.getPlayerList());   
    }
    public void saveSettings() {
        reloadConfig();
        FileConfiguration config = Main.get().getConfig()
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }
        Main.get().saveConfig();
    }
    public void savePlayerData() {

    }
    
    private void reloadConfig() {
        MainClass.get().reloadConfig();   
    }
    
    public String getColor(String uuid) {
        return colors.get(uuid);
    }
    public void setColor(String uuid, String color) {
        
    }
    private void loadColor(String uuid) {
        
    }
    private void saveColor(String uuid) {
    
    }
    
    public String getDefaultCode(String uuid) {
        return defcodes.get(uuid);
    }
    public void setDefaultCode(String uuid, String defcode) {
        
    }
    private void loadDefaultCode(String uuid) {
        
    }
    private void saveDefaultCode(String uuid) {
        
    }
    
    public String getMessage(String message) {
        return messages.get("messages." + message);
    }

}
