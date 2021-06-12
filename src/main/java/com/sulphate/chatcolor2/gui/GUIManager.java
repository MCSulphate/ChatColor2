package com.sulphate.chatcolor2.gui;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GUIManager implements Listener {

    private final ConfigsManager configsManager;
    private final ConfigUtils configUtils;

    private final Map<String, GUI> guis;
    private final Map<Player, GUI> openGUIs;
    private final Set<Player> transitioningPlayers;

    public GUIManager(ConfigsManager configsManager, ConfigUtils configUtils) {
        this.configsManager = configsManager;
        this.configUtils = configUtils;

        guis = new HashMap<>();
        openGUIs = new HashMap<>();
        transitioningPlayers = new HashSet<>();

        reload();
    }

    public void openGUI(Player player, String guiName) {
        if (guis.containsKey(guiName)) {
            GUI gui = guis.get(guiName);

            // If they are already in a ChatColor GUI, make sure it doesn't count the InventoryClosedEvent by using
            // a transition state.
            if (openGUIs.containsKey(player)) {
                transitioningPlayers.add(player);
                gui.open(player);
                transitioningPlayers.remove(player);
            }
            else {
                gui.open(player);
            }

            openGUIs.put(player, gui);
        }
        else {
            // Close inventory to show *something* happened.
            if (player.getOpenInventory() != null) {
                player.closeInventory();
            }

            GeneralUtils.sendConsoleMessage("&6[ChatColor] &eWarning: Tried to open an invalid GUI: " + guiName);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (openGUIs.containsKey(player)) {
            event.setCancelled(true);
            openGUIs.get(player).onClick(player, event.getCurrentItem(), event.getSlot());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (openGUIs.containsKey(player) && !transitioningPlayers.contains(player)) {
            openGUIs.remove(player);
        }
    }

    public void reload() {
        // Close any open GUIs.
        for (Player player : openGUIs.keySet()) {
            player.closeInventory();
        }

        guis.clear();
        openGUIs.clear();

        YamlConfiguration config = configsManager.getConfig("gui.yml");
        Set<String> keys = config.getKeys(false);

        for (String key : keys) {
            ConfigurationSection guiSection = config.getConfigurationSection(key);
            GUI gui = new GUI(this, guiSection, configUtils);

            if (gui.loaded()) {
                guis.put(key, gui);
            }
        }
    }

}
