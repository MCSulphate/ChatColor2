package com.sulphate.chatcolor2.listeners;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;
import com.sulphate.chatcolor2.utils.ColorUtils;
import com.sulphate.chatcolor2.utils.FileUtils;

public class PlayerJoinListener implements Listener {

    String pname;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerJoinEvent e) {

        pname = e.getPlayer().getName();

        String name = e.getPlayer().getName();
        FileConfiguration fc = FileUtils.getPlayerFileConfig(name);

        if (fc.getString("color") == null || fc.getString("color").equalsIgnoreCase("")) {
            ColorUtils.setColor(name, MainClass.get().getConfig().getString("settings.default-color").replace("&", "§"));
        }

        checkDefault();

        if (MainClass.get().getConfig().getBoolean("settings.join-message")) {
            String color = ColorUtils.getColor(e.getPlayer().getName());
            if (color.contains("rainbow")) {
                String mods = color.replace("rainbow", "");
                String ths = CCStrings.colthis;
                String[] thss = ths.split("");
                StringBuilder sb = new StringBuilder();
                List<String> colors = Arrays.asList("§a", "§b", "§c", "§d", "§e");
                int cn = 0;
                for (int i = 0; i < thss.length; i++) {
                    if (cn == 5) {
                        cn = 0;
                    }
                    String col = colors.get(cn);
                    String message = col + mods + thss[i];
                    sb.append(message);
                    cn++;
                }
                String end = sb.toString();
                e.getPlayer().sendMessage(CCStrings.yourcol + end);
                return;
            }
            e.getPlayer().sendMessage(CCStrings.yourcol + ColorUtils.getColor(e.getPlayer().getName()) + CCStrings.colthis);
        }

    }

    public void checkDefault() {
        String name = pname;
        File f = new File(MainClass.get().getDataFolder() + "defcol.yml");
        if (!f.exists()) {
            return;
        }
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

}
