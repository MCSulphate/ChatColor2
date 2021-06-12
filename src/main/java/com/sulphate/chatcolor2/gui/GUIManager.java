package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {

    private final ConfigsManager configsManager;

    private final Map<String, GUI> guis;
    private final Map<Player, GUI> openGUIs;

    public GUIManager(ConfigsManager configsManager) {
        this.configsManager = configsManager;

        guis = new HashMap<>();
        openGUIs = new HashMap<>();

        reload();
    }

    private void reload() {

    }

}
