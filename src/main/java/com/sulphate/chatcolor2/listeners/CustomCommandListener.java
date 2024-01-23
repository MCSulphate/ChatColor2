package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.commands.Setting;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CustomCommandListener implements Listener {

    private final ConfigsManager configsManager;

    private YamlConfiguration mainConfig;

    public CustomCommandListener(ConfigsManager configsManager) {
        this.configsManager = configsManager;

        mainConfig = configsManager.getConfig(Config.MAIN_CONFIG);
    }

    @EventHandler
    public void onEvent(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        String customCommand = mainConfig.getString(Setting.COMMAND_NAME.getConfigPath());

        // If the custom command is at the start, replace it with the actual command.
        if (command.toLowerCase().startsWith("/" + customCommand)) {
            e.setMessage(command.replace("/" + customCommand, "/chatcolor"));
        }
    }

}
