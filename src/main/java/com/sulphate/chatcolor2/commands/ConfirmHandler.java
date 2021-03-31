package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;

import org.bukkit.entity.Player;

public class ConfirmHandler extends Handler {

    private Messages M;
    private ConfirmationsManager confirmationsManager;
    private ConfigUtils configUtils;

    public ConfirmHandler(Messages M, ConfirmationsManager confirmationsManager, ConfigUtils configUtils) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configUtils = configUtils;
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
                ChatColor.getPlugin().saveResource("colors.yml", true);
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
                valueString = GeneralUtils.colouriseMessage(value, "this", false, configUtils);
                break;
            }

            case "rainbow-sequence": {
                String value = (String) scheduler.getValue();

                configUtils.setSetting("rainbow-sequence", value);
                valueString = GeneralUtils.getRainbowSequenceText(value.toCharArray(), value);
                break;
            }

            case "command-name": {
                String value = (String) scheduler.getValue();

                configUtils.setSetting("command-name", value);
                valueString = "&b" + value;
                break;
            }

            case "force-custom-colors": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("force-custom-colors", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }

            case "default-color-enabled": {
                boolean value = (boolean) scheduler.getValue();

                configUtils.setSetting("default-color-enabled", value);
                valueString = value ? "&aTRUE" : "&cFALSE";
                break;
            }
        }

        sender.sendMessage(M.PREFIX + M.CHANGE_SUCCESS.replace("[setting]", type).replace("[value]", GeneralUtils.colourise(valueString)));
        return true;
    }

}
