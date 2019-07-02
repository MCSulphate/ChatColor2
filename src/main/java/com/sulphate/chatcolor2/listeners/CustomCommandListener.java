package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommandListener implements Listener {

    private ConfigUtils configUtils;

    public CustomCommandListener(ConfigUtils configUtils) {
        this.configUtils = configUtils;
    }

    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        String customCommand = (String) configUtils.getSetting("command-name");

        // If the custom command is at the start, replace it with the actual command.
        if (command.toLowerCase().startsWith("/" + customCommand)) {
            e.setMessage(command.replace("/" + customCommand, "/chatcolor"));
        }
    }

}
