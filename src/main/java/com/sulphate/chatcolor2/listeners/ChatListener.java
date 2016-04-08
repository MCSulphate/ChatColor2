package com.sulphate.chatcolor2.listeners;

import java.io.File;
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
            String msg = e.getMessage().replace("&", "");
            String[] mcs = msg.split("");
            String mods = color.replace("rainbow", "");
            List<String> colors = Arrays.asList("§a", "§b", "§c", "§d", "§e");
            StringBuilder sb = new StringBuilder();
            int cn = 0;
            for (int i = 0; i < mcs.length; i++) {
                if (cn == 5) {
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

}
