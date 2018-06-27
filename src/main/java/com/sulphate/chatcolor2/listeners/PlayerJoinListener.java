package com.sulphate.chatcolor2.listeners;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        MainClass.getUtils().updatePlayer(p);
        String uuid = p.getUniqueId().toString();

        checkDefault(uuid);

        if ((Boolean)MainClass.getUtils().getSetting("join-message")) {
            String color = MainClass.getUtils().getColor(uuid);

            if (color.contains("rainbow")) {
                String mods = color.replace("rainbow", "");
                String rseq = (String)MainClass.getUtils().getSetting("rainbow-sequence");
                if (!verifyRainbowSequence(rseq)) {
                    MainClass.getUtils().setSetting("rainbow-sequence", "abcde");
                }

                String[] rss = rseq.split("");
                StringBuilder sb = new StringBuilder();
                for (String s : rss) {
                    sb.append("&").append(s).append(mods).append(s);
                }

                String end = CC2Utils.colourise(sb.toString());
                p.sendMessage(CCStrings.currentcolor + end);
                return;
            }
            e.getPlayer().sendMessage(CC2Utils.colourise(CCStrings.currentcolor + color + CCStrings.colthis));
        }
        MainClass.getUtils().check(p);
    }

    private void checkDefault(String uuid) {
        String defcol = MainClass.getUtils().getCurrentDefaultColor();
        String defcode = MainClass.getUtils().getCurrentDefaultCode();

        if (!MainClass.getUtils().getDefaultCode(uuid).equals(defcode)) {
            MainClass.getUtils().setDefaultCode(uuid, defcode);
            MainClass.getUtils().setColor(uuid, defcol);
        }
    }

    private boolean verifyRainbowSequence(String seq) {
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
