package com.sulphate.chatcolor2.schedulers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;

public class ConfirmScheduler {

    private Player p = null;
    private int id = 0;
    public String type;
    public Object val;

    public void confirm(Player player, String confirmation, Object value) {

        type = confirmation;
        val = value;
        p = player;
        id = Bukkit.getScheduler().scheduleSyncDelayedTask(MainClass.get(), new Runnable() {
            public void run() {
                p.sendMessage(CCStrings.didnotconfirm);
                MainClass.get().removeConfirmee(p);
            }
        },(int)MainClass.getUtils().getSetting("confirm-timeout") * 20);

    }

    public void cancelTask() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
