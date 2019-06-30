package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEvent(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        MainClass.getUtils().updatePlayer(p);
        String uuid = p.getUniqueId().toString();

        checkDefault(uuid);

        if ((Boolean)MainClass.getUtils().getSetting("join-message")) {
            if (MainClass.getUtils().getColor(uuid).contains("rainbow")) {
                String rseq = (String) MainClass.getUtils().getSetting("rainbow-sequence");
                CC2Utils.verifyRainbowSequence(rseq, true);
            }
            e.getPlayer().sendMessage(CC2Utils.colourise(CCStrings.currentcolor) + CC2Utils.colouriseMessage(MainClass.getUtils().getColor(uuid), CCStrings.colthis, false));
        }
        MainClass.getUtils().check(p);
    }

    private void checkDefault(String uuid) {
        String defcol = MainClass.getUtils().getCurrentDefaultColor();
        String defcode = MainClass.getUtils().getCurrentDefaultCode();

        if (!MainClass.getUtils().getDefaultCode(uuid).equals(defcode)) {
            MainClass.getUtils().setDefaultCode(uuid, defcode);
            MainClass.getUtils().setColor(uuid, defcol);
        }
    }
}
