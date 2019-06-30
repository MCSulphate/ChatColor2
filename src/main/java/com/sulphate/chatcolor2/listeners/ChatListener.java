package com.sulphate.chatcolor2.listeners;

import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.sulphate.chatcolor2.main.MainClass;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEvent(AsyncPlayerChatEvent e) {

        if (!MainClass.getPluginEnabled()) {
            return;
        }

        String uuid = e.getPlayer().getUniqueId().toString();
        checkDefault(uuid);
        e.setMessage(CC2Utils.colouriseMessage(MainClass.getUtils().getColor(uuid), e.getMessage(), true));
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
