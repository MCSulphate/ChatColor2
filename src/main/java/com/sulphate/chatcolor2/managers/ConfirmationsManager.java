package com.sulphate.chatcolor2.managers;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ConfirmationsManager {

    private final HashMap<Player, ConfirmScheduler> confirmingPlayers = new HashMap<>();

    public boolean isConfirming(Player player) {
        return confirmingPlayers.containsKey(player);
    }

    public void addConfirmingPlayer(Player player, ConfirmScheduler scheduler) {
        confirmingPlayers.put(player, scheduler);
    }

    public void removeConfirmingPlayer(Player player) {
        confirmingPlayers.remove(player);
    }

    public ConfirmScheduler getSchedulerForPlayer(Player player) {
        return confirmingPlayers.get(player);
    }

}
