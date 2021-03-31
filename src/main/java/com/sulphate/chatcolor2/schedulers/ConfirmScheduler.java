package com.sulphate.chatcolor2.schedulers;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.Messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConfirmScheduler {

    private Messages M;
    private ConfirmationsManager confirmationsManager;
    private ConfigUtils configUtils;

    private Player player;
    private int id;
    private String type;
    private Object value;

    public ConfirmScheduler(Messages M, ConfirmationsManager confirmationsManager, ConfigUtils configUtils, Player player, String type, Object value) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configUtils = configUtils;

        this.player = player;
        this.type = type;
        this.value = value;

        run();
    }

    private void run() {
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(ChatColor.getPlugin(), () -> {
            player.sendMessage(M.DID_NOT_CONFIRM);
            confirmationsManager.removeConfirmingPlayer(player);
        }, (int) configUtils.getSetting("confirm-timeout") * 20);
    }

    public void cancelScheduler() {
        Bukkit.getScheduler().cancelTask(id);
        confirmationsManager.removeConfirmingPlayer(player);
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

}
