package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.data.YamlStorageImpl;
import com.sulphate.chatcolor2.gui.GUIManager;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.Messages;

public class ConfirmHandler extends Handler {

    private final Messages M;
    private final ConfirmationsManager confirmationsManager;
    private final ConfigsManager configsManager;
    private final CustomColoursManager customColoursManager;
    private final GUIManager guiManager;
    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;
    private final PlayerDataStore dataStore;

    public ConfirmHandler(Messages M, ConfirmationsManager confirmationsManager, ConfigsManager configsManager, CustomColoursManager customColoursManager, GUIManager guiManager, ConfigUtils configUtils, GeneralUtils generalUtils, PlayerDataStore dataStore) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configsManager = configsManager;
        this.customColoursManager = customColoursManager;
        this.guiManager = guiManager;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
        this.dataStore = dataStore;
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

                configsManager.loadAllConfigs();
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

                configUtils.setSetting(setting.getName(), value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case INTEGER: {
                int value = (int) scheduler.getValue();

                configUtils.setSetting(setting.getName(), value);
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

                configUtils.setSetting(setting.getName(), value);
                valueString = value;

                if (setting.getName().equals("default-color")) {
                    configUtils.createNewDefaultColour(value);
                }

                break;
            }

            case COLOUR_STRING: {
                String value = (String) scheduler.getValue();

                configUtils.setSetting(setting.getName(), value);
                valueString = generalUtils.colouriseMessage(value, "this", false);

                if (setting.getName().equals("default-color")) {
                    configUtils.createNewDefaultColour(value);
                }

                break;
            }
        }

        sender.sendMessage(M.PREFIX + M.CHANGE_SUCCESS.replace("[setting]", setting.getName()).replace("[value]", GeneralUtils.colourise(valueString)));
        return true;
    }

}
