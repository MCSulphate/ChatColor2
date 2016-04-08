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
            String rs = MainClass.get().getConfig().getString("rainbow-sequence");
            if (!verifyRainbowSequence(rs)) {
                MainClass.get().getConfig().set("rainbow-sequence", "abcde");
                MainClass.get().saveConfig();
            }
            String rainbowseq = MainClass.get().getConfig().getString("rainbow-sequence");
            String[] rsc = rainbowseq.split("");
            List<String> colors = new ArrayList<String>();
            for (String s : rsc) {
                colors.add(s);
            }
            String msg = e.getMessage().replace("&", "");
            String[] mcs = msg.split("");
            String mods = color.replace("rainbow", "");
            StringBuilder sb = new StringBuilder();
            int cn = 0;
            for (int i = 0; i < mcs.length; i++) {
                if (cn == colors.size()) {
                    cn = 0;
                }
                String col = colors.get(cn);
                String message = col + mods + mcs[i];
                sb.append(message);
                cn++;
            }

            String finish = sb.toString();
            e.setMessage(finish);
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
        return false;
    }

}
