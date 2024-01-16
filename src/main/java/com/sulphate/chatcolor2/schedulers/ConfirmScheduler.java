package com.sulphate.chatcolor2.schedulers;

import com.sulphate.chatcolor2.commands.Setting;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.utils.Messages;

public class ConfirmScheduler {

    private final Messages M;
    private final ConfirmationsManager confirmationsManager;
    private final ConfigUtils configUtils;

    private final Player player;
    private int id;
    private final Setting setting;
    private final Object value;

    public ConfirmScheduler(Messages M, ConfirmationsManager confirmationsManager, ConfigUtils configUtils, Player player, Setting setting, Object value) {
        this.M = M;
        this.confirmationsManager = confirmationsManager;
        this.configUtils = configUtils;

        this.player = player;
        this.setting = setting;
        this.value = value;

        run();
    }

    private void run() {
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(ChatColor.getPlugin(), () -> {
            player.sendMessage(M.PREFIX + M.DID_NOT_CONFIRM);
            confirmationsManager.removeConfirmingPlayer(player);
        }, (int) configUtils.getSetting("confirm-timeout") * 20);
    }

    public void cancelScheduler() {
        Bukkit.getScheduler().cancelTask(id);
        confirmationsManager.removeConfirmingPlayer(player);
    }

    public Setting getSetting() {
        return setting;
    }

    public Object getValue() {
        return value;
    }

}
