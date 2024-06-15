package com.sulphate.chatcolor2.newgui;

import com.sulphate.chatcolor2.data.PlayerDataStore;
import com.sulphate.chatcolor2.exception.InvalidGuiException;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.CustomColoursManager;
import com.sulphate.chatcolor2.newgui.item.GuiItem;
import com.sulphate.chatcolor2.newgui.item.ItemStackTemplate;
import com.sulphate.chatcolor2.newgui.item.impl.ColourItem;
import com.sulphate.chatcolor2.newgui.item.impl.ModifierItem;
import com.sulphate.chatcolor2.utils.Config;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import com.sulphate.chatcolor2.utils.Reloadable;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GuiManager implements Reloadable, Listener {

    private static final String GUI_CONFIG_KEY = "config";

    private final ConfigsManager configsManager;
    private final PlayerDataStore dataStore;
    private final GeneralUtils generalUtils;
    private final CustomColoursManager customColoursManager;
    private final Messages M;

    private final Map<String, ConfigurationSection> guiConfigs;
    private final Map<Player, Gui> openGuis;
    private final List<Player> transitioningPlayers;

    private String mainConfigName = "main";

    public GuiManager(ConfigsManager configsManager, PlayerDataStore dataStore, GeneralUtils generalUtils, CustomColoursManager customColoursManager, Messages M) {
        this.configsManager = configsManager;
        this.dataStore = dataStore;
        this.generalUtils = generalUtils;
        this.customColoursManager = customColoursManager;
        this.M = M;

        guiConfigs = new HashMap<>();
        openGuis = new HashMap<>();
        transitioningPlayers = new ArrayList<>();

        reload();
    }

    @Override
    public void reload() {
        for (Player player : openGuis.keySet()) {
            player.closeInventory();
        }

        transitioningPlayers.clear();
        openGuis.clear();
        guiConfigs.clear();

        YamlConfiguration config = configsManager.getConfig(Config.GUI);
        Set<String> keys = config.getKeys(false);

        if (keys.contains(GUI_CONFIG_KEY)) {
            ConfigurationSection configSection = config.getConfigurationSection(GUI_CONFIG_KEY);

            if (configSection.contains("main-inventory")) {
                mainConfigName = configSection.getString("main-inventory");
            }

            if (configSection.contains("no-permission-item")) {
                Gui.noPermissionItemTemplate = ItemStackTemplate.fromConfigSection(configSection.getConfigurationSection("no-permission-item"));
            }

            if (configSection.contains("color.selected-text")) {
                ColourItem.setSelectedText(configSection.getString("color.selected-text"));
            }

            if (configSection.contains("color.unselected-text")) {
                ColourItem.setUnselectedText(configSection.getString("color.unselected-text"));
            }

            if (configSection.contains("modifier.selected-text")) {
                ModifierItem.setSelectedText(configSection.getString("color.selected-text"));
            }

            if (configSection.contains("modifier.unselected-text")) {
                ModifierItem.setUnselectedText(configSection.getString("color.unselected-text"));
            }

            try {
                if (configSection.contains("modifier.selected-material")) {
                    ModifierItem.setSelectedMaterial(Material.valueOf(configSection.getString("modifier.selected-material")));
                }

                if (configSection.contains("modifier.unselected-material")) {
                    ModifierItem.setUnselectedMaterial(Material.valueOf(configSection.getString("modifier.unselected-material")));
                }
            }
            catch (IllegalArgumentException ex) {
                throw new InvalidGuiException("Invalid modifier material in GUI config, default will be used.");
            }
        }
        else {
            // Warning message TODO
            GeneralUtils.sendConsoleMessage("Warning: No GUI config section found, default values will be used!");
            GeneralUtils.sendConsoleMessage("To regenerate the config, please delete gui.yml and reload the server.");
        }

        for (String inventoryName : keys) {
            if (inventoryName.equals(GUI_CONFIG_KEY)) {
                continue;
            }

            guiConfigs.put(inventoryName, config.getConfigurationSection(inventoryName));
        }

        if (!guiConfigs.containsKey(mainConfigName)) {
            // TODO Error message
            GeneralUtils.sendConsoleMessage(String.format("Error: No main GUI configuration found with name %s. The GUI will not open.", mainConfigName));
        }
    }

    public void openMainGui(Player player) {
        Gui main = createGui(mainConfigName, player);
        openGui(main, player);
    }

    public Gui createGui(String name, Player player) {
        if (!guiConfigs.containsKey(name)) {
            return null;
        }

        try {
            ConfigurationSection section = guiConfigs.get(name);
            return new Gui(name, section, player, dataStore.getPlayerData(player.getUniqueId()), this, generalUtils, customColoursManager, M);
        }
        catch (InvalidGuiException ex) {
            // TODO: Error message
            GeneralUtils.sendConsoleMessage("Error: " + ex.getMessage());
            return null;
        }
    }

    public boolean guiExists(String name) {
        return guiConfigs.containsKey(name);
    }

    public void openGui(Gui gui, Player player) {
        if (gui == null) {
            // TODO: Error message
            player.sendMessage("Tried to open invalid GUI.");
            return;
        }

        if (openGuis.containsKey(player)) {
            transitioningPlayers.add(player);
            gui.open();
            transitioningPlayers.remove(player);
        }
        else {
            gui.open();
        }

        openGuis.put(player, gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getView().getTopInventory();

        if (openGuis.containsKey(player)) {
            event.setCancelled(true);

            InventoryAction action = event.getAction();
            ItemStack clicked = event.getCurrentItem();

            if (action.equals(InventoryAction.PICKUP_ALL) && clicked != null && !clicked.getType().equals(Material.AIR)) {
                // Perform the interaction, persist any changes to their colour.
                openGuis.get(player).onInteract(event.getRawSlot(), inventory);
                dataStore.savePlayerData(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!transitioningPlayers.contains(player)) {
            openGuis.remove(player);
        }
    }

}
