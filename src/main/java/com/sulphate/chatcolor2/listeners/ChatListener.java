package com.sulphate.chatcolor2.listeners;

import java.util.Arrays;
import java.util.List;

import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sulphate.chatcolor2.main.MainClass;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(AsyncPlayerChatEvent e) {

        if (!MainClass.getPluginEnabled()) {
            return;
        }

        String uuid = e.getPlayer().getUniqueId().toString();
        boolean override = (boolean)MainClass.getUtils().getSetting("color-override");
        checkDefault(uuid);
        String color = MainClass.getUtils().getColor(uuid);

        if (e.getMessage().contains("&")) {
            if (override) {
                color = color.replace("&", "");
            }
            else {
                e.setMessage(CC2Utils.colourise(e.getMessage()));
            }
        }

        if (color.contains("rainbow")) {
            String rseq = (String) MainClass.getUtils().getSetting("rainbow-sequence");

            if (!verifyRainbowSequence(rseq)) {
                MainClass.getUtils().setSetting("rainbow-sequence", "abcde");
                rseq = "abcde";
            }

            String mods = color.replace("rainbow", "");
            char[] colors = rseq.toCharArray();
            char[] msgchars = e.getMessage().toCharArray();
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
                    sb.append(ChatColor.COLOR_CHAR).append(colors[currentColorIndex]).append(mods).append(msgchars[i]);
                    currentColorIndex++;
                }
            }

            String end = sb.toString();
            e.setMessage(end);
            return;
        }

        e.setMessage(CC2Utils.colourise(color + e.getMessage()));
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
