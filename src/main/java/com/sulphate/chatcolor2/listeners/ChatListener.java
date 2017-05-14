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

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(AsyncPlayerChatEvent e) {

        if (!MainClass.getPluginEnabled()) {
            return;
        }

        String uuid = e.getPlayer().getUniqueId().toString();
        checkDefault(uuid);

        String color = MainClass.getUtils().getColor(uuid);
        if (e.getMessage().contains("&") && !(Boolean)MainClass.getUtils().getSetting("color-override")) {
            e.setMessage(e.getMessage().replace("&", "§"));
            return;
        }
        if (color.contains("rainbow")) {
            String rseq = (String)MainClass.getUtils().getSetting("rainbow-sequence");
            if (!verifyRainbowSequence(rseq)) {
                MainClass.getUtils().setSetting("rainbow-sequence", "abcde");
                rseq = "abcde";
            }
            String msg = e.getMessage().replace("&", "");
            String mods = color.replace("rainbow","");
            char[] cols = rseq.toCharArray();
            char[] msgchars = msg.toCharArray();
            int rn = 0;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < msgchars.length; i++) {
                if (rn == cols.length) {
                    rn = 0;
                }
                if (msgchars[i] == ' ') {
                    sb.append(" ");
                }
                else {
                    sb.append("§" + cols[rn] + mods + msgchars[i]);
                    rn++;
                }
            }
            String end = sb.toString();
            e.setMessage(end);
            return;
        }

        e.setMessage(MainClass.getUtils().getColor(uuid) + e.getMessage().replace("&", ""));

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

    public void checkDefault(String uuid) {
        String defcol = MainClass.getUtils().getCurrentDefaultColor();
        String defcode = MainClass.getUtils().getCurrentDefaultCode();

        if (!MainClass.getUtils().getDefaultCode(uuid).equals(defcode)) {
            MainClass.getUtils().setDefaultCode(uuid, defcode);
            MainClass.getUtils().setColor(uuid, defcol);
        }
    }

}
