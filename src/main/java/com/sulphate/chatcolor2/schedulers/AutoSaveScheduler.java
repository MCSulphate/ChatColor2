package com.sulphate.chatcolor2.schedulers;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;
import org.bukkit.Bukkit;

public class AutoSaveScheduler {

    private boolean active = (boolean)MainClass.getUtils().getSetting("auto-save");
    private int id = 0;

    public void startTask() {

        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(MainClass.get(), new Runnable() {
            @Override
            public void run() {
                active = (boolean)MainClass.getUtils().getSetting("auto-save");
                if (active) {
                    MainClass.getUtils().saveAllData();
                    Bukkit.getLogger().info(CCStrings.prefix + "Saved all data.");
                }
            }
        }, 5*60*20L, 5*6*20L);

    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(id);
    }

}
