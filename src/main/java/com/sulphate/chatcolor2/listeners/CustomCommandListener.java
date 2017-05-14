package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommandListener implements Listener {

    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e) {
        String cmd = e.getMessage();
        String cuscmd = (String) MainClass.getUtils().getSetting("command-name");
        if (cmd.toLowerCase().startsWith("/" + cuscmd)) {
            e.setMessage(cmd.replace("/" + cuscmd, "/chatcolor"));
        }
    }

}
