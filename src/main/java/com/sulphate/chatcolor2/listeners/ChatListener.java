package com.sulphate.chatcolor2.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.ColorUtils;
import com.sulphate.chatcolor2.utils.FileUtils;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(AsyncPlayerChatEvent e) {

        String name = e.getPlayer().getName();

        File f = new File(MainClass.get().getDataFolder() + File.separator + "defcol.yml");
        if (f.exists()) {
            String defcol = YamlConfiguration.loadConfiguration(f).getString("default-color");
            String defcode = YamlConfiguration.loadConfiguration(f).getString("default-code");
            if (ColorUtils.getDefaultCode(name) == null) {
                FileConfiguration conf = FileUtils.getPlayerFileConfig(name);
                conf.set("default-code", defcode);
                FileUtils.saveConfig(conf, FileUtils.getPlayerFile(name));
                ColorUtils.setColor(name, defcol);
            }
            else if (!ColorUtils.getDefaultCode(name).equals(defcode)) {
                FileConfiguration conf = FileUtils.getPlayerFileConfig(name);
                conf.set("default-code", defcode);
                FileUtils.saveConfig(conf, FileUtils.getPlayerFile(name));
                ColorUtils.setColor(name, defcol);
            }

        }

        String color = ColorUtils.getColor(e.getPlayer().getName());
        if (e.getMessage().contains("&") && !MainClass.get().getConfig().getBoolean("settings.color-override")) {
            e.setMessage(e.getMessage().replace("&", "§"));
            return;
        }
        if (color.contains("rainbow")) {
            String rseq = MainClass.get().getConfig().getString("settings.rainbow-sequence");
            if (!verifyRainbowSequence(rseq)) {
                MainClass.get().getConfig().set("settings.rainbow-sequence", "abcde");
                MainClass.get().saveConfig();
            }
            String rs = MainClass.get().getConfig().getString("settings.rainbow-sequence");
            String msg = e.getMessage().replace("&", "");
            String mods = color.replace("rainbow","");
            String[] rss = rs.split("");
            String[] mss = msg.split("");
            int rn = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mss.length; i++) {
                if (rn == rss.length) {
                    rn = 0;
                }
                if (mss[i].equals(" ")) {
                    sb.append(" ");
                }
                else {
                    sb.append("§" + rss[rn] + mods + mss[i]);
                    rn++;
                }
            }
            String end = sb.toString();
            e.setMessage(end);
            return;
        }

        e.setMessage(ColorUtils.getColor(e.getPlayer().getName()) + e.getMessage().replace("&", ""));

    }

    public boolean verifyRainbowSequence(String seq) {

        boolean verify = true;
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");
        for (String s : chars) {
            if (!cols.contains(s)) {
                verify = false;
            }
        }
        return verify;
    }

}
