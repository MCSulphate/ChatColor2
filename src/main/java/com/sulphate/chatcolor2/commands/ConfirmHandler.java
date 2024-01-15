package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.Messages;

public class ConfirmHandler extends Handler {

    private final Messages M;
    private final ConfirmationsManager confirmationsManager;
    private final ConfigUtils configUtils;
    private final GeneralUtils generalUtils;

    public ConfirmHandler(Messages M, ConfirmationsManager confirmationsManager, ConfigUtils configUtils, GeneralUtils generalUtils) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configUtils = configUtils;
        this.generalUtils = generalUtils;
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
        String type = scheduler.getType();
        scheduler.cancelScheduler();

        String valueString = "";

        switch(type) {
            case "auto-save": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("auto-save", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "save-interval": {
                int value = (int) scheduler.getValue();

                configUtils.setSetting("save-interval", value);
                // Update the save scheduler with the new interval.
                ChatColor.getPlugin().getSaveScheduler().setSaveInterval(value);
                valueString = value + "";
                break;
            }

            case "color-override": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("color-override", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "reset": {
                // Overwrite configs with default ones.
                ChatColor.getPlugin().saveResource("config.yml", true);
                ChatColor.getPlugin().saveResource("messages.yml", true);
                ChatColor.getPlugin().saveResource("groups.yml", true);
                M.reloadMessages();

                sender.sendMessage(M.PREFIX + M.CONFIGS_RESET);
                return true;
            }

            case "notify-others": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("notify-others", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "join-message": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("join-message", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "confirm-timeout": {
                int value = (int) scheduler.getValue();

                configUtils.setSetting("confirm-timeout", value);
                valueString = "&b" + value;
                break;
            }

            case "default-color": {
                String value = (String) scheduler.getValue();

                configUtils.createNewDefaultColour(value);
                valueString = generalUtils.colouriseMessage(value, "this", false);
                break;
            }

            case "command-name": {
                String value = (String) scheduler.getValue();

                configUtils.setSetting("command-name", value);
                valueString = "&b" + value;
                break;
            }

            case "force-group-colors": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("force-group-colors", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "default-color-enabled": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("default-color-enabled", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "command-opens-gui": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("command-opens-gui", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }
        }

        sender.sendMessage(M.PREFIX + M.CHANGE_SUCCESS.replace("[setting]", type).replace("[value]", GeneralUtils.colourise(valueString)));
        return true;
    }

}
