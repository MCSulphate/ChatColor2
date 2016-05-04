package com.sulphate.chatcolor2.schedulers;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.SQLUtils;
import org.bukkit.Bukkit;

public class SQLDefaultForceUpdater {

    private int id = 0;
    private MainClass mc = MainClass.get();
    private SQLUtils sql = mc.getSQL();

    public void startForceUpdater() {

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {
            @Override
            public void run() {
                sql.getSQLDefaultColorOrCode(false, true);
            }
        }, 400L, 400L);

    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
