package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.data.YamlStorageImpl;
import com.sulphate.chatcolor2.gui.GUIManager;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.gui.GuiManager;
import com.sulphate.chatcolor2.utils.Config;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.Messages;

public class ConfirmHandler extends Handler implements Reloadable {

    private final Messages M;
    private final ConfirmationsManager confirmationsManager;
    private final ConfigsManager configsManager;
    private final CustomColoursManager customColoursManager;
    private final GuiManager guiManager;
    private final GeneralUtils generalUtils;
    private final PlayerDataStore dataStore;

    private YamlConfiguration mainConfig;

    public ConfirmHandler(Messages M, ConfirmationsManager confirmationsManager, ConfigsManager configsManager, CustomColoursManager customColoursManager, GuiManager guiManager, GeneralUtils generalUtils, PlayerDataStore dataStore) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configsManager = configsManager;
        this.customColoursManager = customColoursManager;
        this.guiManager = guiManager;
        this.generalUtils = generalUtils;
        this.dataStore = dataStore;

        reload();
    }

    public void reload() {
        mainConfig = configsManager.getConfig(Config.MAIN_CONFIG);
    }

    @Override
    public boolean handle(Player sender) {
        if (!confirmationsManager.isConfirming(sender)) {
            sender.sendMessage(M.PREFIX + M.NOTHING_TO_CONFIRM);
            return true;
        }

        if (!sender.isOp() && !sender.hasPermission("chatcolor.admin")) {
            sender.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
            return true;
        }

        ConfirmScheduler scheduler = confirmationsManager.getSchedulerForPlayer(sender);
        Setting setting = scheduler.getSetting();
        scheduler.cancelScheduler();

        String valueString = "";

        switch(setting.getDataType()) {
            case NONE: {
                // Overwrite configs with default ones.
                ChatColor.getPlugin().saveResource("config.yml", true);
                ChatColor.getPlugin().saveResource("messages.yml", true);
                ChatColor.getPlugin().saveResource("groups.yml", true);
                ChatColor.getPlugin().saveResource("custom-colors.yml", true);
                ChatColor.getPlugin().saveResource("gui.yml", true);

                configsManager.reload();
                M.reloadMessages();
                guiManager.reload();
                customColoursManager.reload();

                // Re-load player configs to avoid plugin inoperation.
                for (Player player : Bukkit.getOnlinePlayers()) {
                    configsManager.loadPlayerConfig(player.getUniqueId());
                }

                sender.sendMessage(M.PREFIX + M.CONFIGS_RESET);
                return true;
            }

            case BOOLEAN: {
                boolean value = (boolean) scheduler.getValue();

                mainConfig.set(setting.getConfigPath(), value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case INTEGER: {
                int value = (int) scheduler.getValue();

                mainConfig.set(setting.getConfigPath(), value);
                valueString = String.valueOf(value);

                // Update the save scheduler with the new interval. Only applicable to the YAML implementation.
                if (setting.getName().equals("save-interval") && dataStore instanceof YamlStorageImpl) {
                    ((YamlStorageImpl) dataStore).updateSaveInterval(value);
                }

                break;
            }

            // The actual updating of these types is the same, bar the exception of default-color.
            case COMMAND_NAME:
            case STRING: {
                String value = (String) scheduler.getValue();

                mainConfig.set(setting.getConfigPath(), value);
                valueString = value;

                if (setting.getName().equals("default-color")) {
                    createNewDefaultColour(value);
                }

                break;
            }

            case COLOUR_STRING: {
                String value = (String) scheduler.getValue();

                mainConfig.set(setting.getConfigPath(), value);
                valueString = generalUtils.colouriseMessage(value, "this", false);

                if (setting.getName().equals("default-color")) {
                    createNewDefaultColour(value);
                }

                break;
            }
        }

        configsManager.saveConfig(Config.MAIN_CONFIG);
        sender.sendMessage(M.PREFIX + M.CHANGE_SUCCESS.replace("[setting]", setting.getName()).replace("[value]", GeneralUtils.colourise(valueString)));
        return true;
    }

    private void createNewDefaultColour(String colour) {
        // Current millis time will always be unique.
        long code = (System.currentTimeMillis() / 1000);

        mainConfig.set("default.code", code);
        mainConfig.set("default.color", colour);
    }

}
