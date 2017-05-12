package com.sulphate.chatcolor2.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.main.MainClass;

public class FileUtils {

    public static void checkPlayerList() {
        File plist = new File(MainClass.get().getDataFolder(), "playerlist.yml");
        if (!plist.exists()) {
            try {
                plist.createNewFile();
            }
            catch(IOException e) {
            }
        }
    }

    public static File getPlayerList() {
        checkPlayerList();
        return new File(MainClass.get().getDataFolder(), "playerlist.yml");
    }

    public static FileConfiguration getPlayerListConfig() {
        return YamlConfiguration.loadConfiguration(getPlayerList());
    }

    public static void checkPlayersFolder() {
        File pfold = new File(MainClass.get().getDataFolder() + "/players/");
        if (!pfold.exists()) {
            pfold.mkdirs();
        }
    }

    public static File getPlayersFolder() {
        checkPlayersFolder();
        return new File(MainClass.get().getDataFolder() + "/players/");
    }

    public static void saveConfig(FileConfiguration config, File file) {
        try {
            config.save(file);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePlayer(Player p) { //TODO: Change so it uses the cache instead.
        String name = p.getName();
        String uuid = p.getUniqueId().toString();
        File plist = getPlayerList();
        FileConfiguration plconfig = getPlayerListConfig();

        plconfig.set(name, uuid);
        saveConfig(plconfig, plist);
        checkPlayerFile(p.getName());
    }

    public static void checkPlayerFile(String playername) {
        FileConfiguration plconfig = getPlayerListConfig();
        String uuid = plconfig.getString(playername);
        File pfile = new File(getPlayersFolder(), uuid + ".yml");
        if (!pfile.exists()) {
            try {
                pfile.createNewFile();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static File getPlayerFile(String playername) {
        checkPlayerFile(playername);
        String uuid = getPlayerListConfig().getString(playername);
        return new File(getPlayersFolder(), uuid + ".yml");
    }

    public static FileConfiguration getPlayerFileConfig(String playername) {
        return YamlConfiguration.loadConfiguration(getPlayerFile(playername));
    }



}
