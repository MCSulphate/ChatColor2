package com.sulphate.chatcolor2.schedulers;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SQLColorForceUpdater {

    private int id = 0;
    private MainClass mc = MainClass.get();
    private int interval = mc.getConfig().getInt("backend.sql.force-update-time")*20;

    public void startForceUpdater() {

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
            @Override
            public void run() {
                if (interval < 200) {
                }
                else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        mc.getSQL().getSQLColor(p.getName(), true);
                    }
                }
            }
        }, interval, interval);

    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
