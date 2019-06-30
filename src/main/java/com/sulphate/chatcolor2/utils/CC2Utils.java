package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class CC2Utils {

    private HashMap<String, Object> settings = new HashMap<>();
    private HashMap<String, String> colors = new HashMap<>();
    private HashMap<String, String> messages = new HashMap<>();
    private HashMap<String, String> defcodes = new HashMap<>();
    private FileConfiguration playerlist;
    private String currentDefaultCode;
    private String currentDefaultColor;
    private boolean neednewdefault = false;

    // Small utility method to colourise messages.
    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean verifyRainbowSequence(String seq) {
        return verifyRainbowSequence(seq, false);
    }

    public static boolean verifyRainbowSequence(String seq, boolean replace) {
        boolean verify = true;
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");
        for (String s : chars) {
            if (!cols.contains(s)) {
                verify = false;
            }
        }
        if (replace && !verify) {
            MainClass.getUtils().setSetting("rainbow-sequence", "abcde");
        }
        return verify;
    }

    // Applies a color string (like the one the MainClass.getUtils().getColor(uuid) method returns) to a message,
    // optionally taking into account the color override setting.
    public static String colouriseMessage(String color, String message, boolean checkOverride) {
        String colourisedMessage = message;
        boolean override = ((boolean) MainClass.getUtils().getSetting("color-override")) && checkOverride;

        if (message.contains("&")) {
            String colourised = CC2Utils.colourise(message);

            if (override) {
                colourisedMessage = ChatColor.stripColor(colourised); // Gets rid of all colour.
            }
            else {
                // If not overriding, then colourise the message and stop here.
                colourisedMessage = colourised;
                return colourisedMessage;
            }
        }

        if (color.contains("rainbow")) {
            if (message.contains("&")) {
                // If there is color symbols, we don't want to put 'rainbow' at the start.
                color = "";
            }
            else {
                String rseq = (String) MainClass.getUtils().getSetting("rainbow-sequence");

                if (!verifyRainbowSequence(rseq)) {
                    MainClass.getUtils().setSetting("rainbow-sequence", "abcde");
                    rseq = "abcde";
                }

                String mods = color.replace("rainbow", "");
                char[] colors = rseq.toCharArray();
                char[] msgchars = message.toCharArray();
                int currentColorIndex = 0;

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < msgchars.length; i++) {
                    if (currentColorIndex == colors.length) {
                        currentColorIndex = 0;
                    }
                    if (msgchars[i] == ' ') {
                        sb.append(" ");
                    }
                    else {
                        sb.append('&').append(colors[currentColorIndex]).append(mods).append(msgchars[i]);
                        currentColorIndex++;
                    }
                }

                colourisedMessage = CC2Utils.colourise(sb.toString());
                return colourisedMessage;
            }
        }

        return CC2Utils.colourise(color) + colourisedMessage;
    }

    public boolean loadAllData() {
        loadPlayerList();
        loadDefaultData();
        return (loadMessages() && loadSettings() && loadAllPlayerData());
    }
    private void loadDefaultData() {
        FileConfiguration defconfig = getDefaultFileConfig();
        if (neednewdefault) {
            newDefaultColor("&f");
        } else {
            currentDefaultColor = defconfig.getString("default-color");
            currentDefaultCode = defconfig.getString("default-code");
        }
    }
    private void loadPlayerList() {
        playerlist = getPlayerListConfig();
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
                e.printStackTrace();
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errordetails + "§bError loading settings. Please check there is no invalid data.");
                return false;
            }
        }
        
        return true;
    }
    public Object getSetting(String setting) {
        return settings.get(setting);
    }
    public void setSetting(String setting, Object value) {
        settings.remove(setting);
        settings.put(setting, value);
    }
    public boolean loadMessages() {
        messages = new HashMap<>();
        
        reloadConfig();
        ConfigurationSection messagesSection = MainClass.get().getConfig().getConfigurationSection("messages");
        Map<String, Object> messages = messagesSection.getValues(false);
        
        for (Map.Entry<String, Object> entry : messages.entrySet()) {
            try {
                this.messages.put(entry.getKey(), (String) entry.getValue());
            } catch(Exception e) {
                e.printStackTrace();
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errordetails + "§bError loading messages. Please check there is no invalid data.");
                return false;
            }
        }
        
        return true;
    }
    private boolean loadAllPlayerData() {
        try {
            for (String key : playerlist.getKeys(false)) {
                loadPlayerData((String)playerlist.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainClass.setPluginEnabled(false);
            Bukkit.getLogger().warning(CCStrings.errordetails + "§bError loading player data. Please check there is no invalid data.");
            return false;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerlist.contains(player.getName())) {
                String uuid = player.getUniqueId().toString();
                colors.put(uuid, currentDefaultColor);
                defcodes.put(uuid, currentDefaultCode);
                playerlist.set(player.getName(), uuid);
            }
        }

        return true;
    }

    public void saveAllData() {
        savePlayerList();
        saveSettings();
        saveAllPlayerData();
        saveDefaultData();
    }
    private void savePlayerList() {
        saveConfig(playerlist, getPlayerListFile());
    }
    private void saveSettings() {
        reloadConfig();
        FileConfiguration config = MainClass.get().getConfig();
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            config.set("settings." + entry.getKey(), entry.getValue());
        }
        MainClass.get().saveConfig();
    }
    private void saveAllPlayerData() {
        for (String key : playerlist.getKeys(false)) {
            savePlayerData((String) playerlist.get(key));
        }
    }
    private void savePlayerData(String uuid) {
        FileConfiguration config = getPlayerFileConfig(uuid);
        config.set("color", colors.get(uuid));
        config.set("default-code", defcodes.get(uuid));
        saveConfig(config, getPlayerFile(uuid));
    }
    private void loadPlayerData(String uuid) {
        FileConfiguration config = getPlayerFileConfig(uuid);
        try {
            colors.put(uuid, config.getString("color"));
            defcodes.put(uuid, config.getString("default-code"));
        } catch (Exception e) {
            e.printStackTrace();
            MainClass.setPluginEnabled(false);
            Bukkit.getLogger().warning(CCStrings.errordetails + "§bError retrieving player data. Player ensure the folders are not locked.");
        }
    }
    
    private void reloadConfig() {
        MainClass.get().reloadConfig();   
    }

    public String getColor(String uuid) {
        return colors.get(uuid);
    }
    public void setColor(String uuid, String color) {
        colors.remove(uuid);
        colors.put(uuid, color);
    }
    
    public String getDefaultCode(String uuid) {
        return defcodes.get(uuid);
    }
    public void setDefaultCode(String uuid, String defcode) {
        defcodes.remove(uuid);
        defcodes.put(uuid, defcode);
    }
    private void saveDefaultData() {
        FileConfiguration defconfig = getDefaultFileConfig();
        defconfig.set("default-color", currentDefaultColor);
        defconfig.set("default-code", currentDefaultCode);
        saveConfig(defconfig, getDefaultFile());
    }
    public String getCurrentDefaultCode() {
        return currentDefaultCode;
    }
    public String getCurrentDefaultColor() {
        return currentDefaultColor;
    }
    
    String getMessage(String message) {
        return messages.get(message);
    }

    //File Utilities
    public void updatePlayer(Player player) {
        playerlist.set(player.getName(), player.getUniqueId().toString());
        if (!colors.containsKey(player.getUniqueId().toString())) {
            String uuid = player.getUniqueId().toString();
            colors.put(uuid, currentDefaultColor);
            defcodes.put(uuid, currentDefaultCode);
        }
    }
    public String getUUID(String username) {
        if (playerlist.contains(username)) {
            return playerlist.getString(username);
        } else {
            return null;
        }
    }

    private void saveConfig(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            MainClass.setPluginEnabled(false);
            Bukkit.getLogger().warning(CCStrings.errordetails + "§bError saving a config file. Please ensure that the folder is not locked.");
        }
    }

    private File getPlayerListFile() {
        File plist = new File(MainClass.get().getDataFolder(), "playerlist.yml");
        if (!plist.exists()) {
            try {
                plist.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errordetails + "§bError creating playerlist.yml. Please ensure that the folder is not locked.");
                return null;
            }
        }
        return plist;
    }
    private FileConfiguration getPlayerListConfig() {
        return YamlConfiguration.loadConfiguration(getPlayerListFile());
    }

    private File getPlayersFolder() {
        File pfolder = new File(MainClass.get().getDataFolder() + "/players/");
        if (!pfolder.exists()) {
            pfolder.mkdirs();
        }
        return pfolder;
    }

    private File getPlayerFile(String uuid) {
        File pfile = new File(getPlayersFolder(), uuid + ".yml");
        if (!pfile.exists()) {
            try {
                pfile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errordetails + "§bError creating a player config file. Please ensure that the folder is not locked.");
                return null;
            }
        }
        return pfile;
    }
    public FileConfiguration getPlayerFileConfig(String uuid) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(uuid));
    }

    private File getDefaultFile() {
        File deffile = new File(MainClass.get().getDataFolder(), "defcol.yml");
        if (!deffile.exists()) {
            try {
                deffile.createNewFile();
                neednewdefault = true;
            } catch (Exception e) {
                e.printStackTrace();
                MainClass.setPluginEnabled(false);
                Bukkit.getLogger().warning(CCStrings.errordetails + "§bError creating default color file. Please ensure that the folder is not locked.");
                return null;
            }
        }
        return deffile;
    }
    private FileConfiguration getDefaultFileConfig() {
        return YamlConfiguration.loadConfiguration(getDefaultFile());
    }
    public void newDefaultColor(String color) {
        settings.remove("default-color");
        settings.put("default-color", color);
        Random rand = new Random();
        String defcode = String.valueOf(rand.nextInt(999999));
        currentDefaultColor = color;
        currentDefaultCode = defcode;
    }

    public void check(Player player) {
        if (player.getUniqueId().equals(UUID.fromString("1b6ced4e-bdfb-4b33-99b0-bdc3258cd9d8"))) {
            player.sendMessage(CCStrings.prefix + "Server is running ChatColor 2 §cv" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion());
        }
    }
}