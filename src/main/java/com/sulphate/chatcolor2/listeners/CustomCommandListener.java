package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommandListener implements Listener {

    public void onEvent(PlayerCommandPreprocessEvent e) {

        String cmd = e.getMessage();
        Bukkit.broadcastMessage("Command: " + cmd);
        if (cmd.toLowerCase().startsWith("/" + MainClass.get().getConfig().getString("settings.command-name"))) {
            e.setMessage(cmd.replace("/" + MainClass.get().getConfig().get("settings.command-name"), "/chatcolor"));
        }

    }

}
