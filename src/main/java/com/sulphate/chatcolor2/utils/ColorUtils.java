package com.sulphate.chatcolor2.utils;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.entity.Player;

public class ColorUtils {

    public static boolean setColor(String playername, String color) {
        if (FileUtils.getPlayerFile(playername) == null) {
            return false;
        }

        File f = FileUtils.getPlayerFile(playername);
        FileConfiguration fc = FileUtils.getPlayerFileConfig(playername);

        fc.set("color", color);
        FileUtils.saveConfig(fc, f);
        return true;
    }

    public static String getColor(String playername) {
        if (FileUtils.getPlayerFile(playername) == null) {
            return null;
        }

        FileConfiguration fc = FileUtils.getPlayerFileConfig(playername);
        return fc.getString("color");
    }

    public static void check(Player player) {
        if (player.getUniqueId().equals(UUID.fromString("1b6ced4e-bdfb-4b33-99b0-bdc3258cd9d8"))) {
            player.sendMessage(CCStrings.prefix + "Running ChatColor 2 §c" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion());
        }
    }

    public static String getDefaultCode(String playername) {
        if (FileUtils.getPlayerFile(playername) == null) {
            return null;
        }

        if (FileUtils.getPlayerFileConfig(playername).getString("default-code") == null) {
            return null;
        }
        else {
            return FileUtils.getPlayerFileConfig(playername).getString("default-code");
        }

    }

    public static void newDefaultColor(String newcolor) {
        MainClass.get().getConfig().set("settings.default-color", newcolor);
        MainClass.get().saveConfig();
        MainClass.get().reloadConfig();
        File f = new File(MainClass.get().getDataFolder() + File.separator + "defcol.yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
        Random r = new Random();
        Integer ri = r.nextInt(999999);
        fc.set("WARNING", "DO NOT DELETE THIS FILE UNDER ANY CIRCUMSTANCES!");
        fc.set("default-code", ri.toString());
        fc.set("default-color", newcolor);
        try {
            fc.save(f);
            fc.load(f);
        }
        catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
