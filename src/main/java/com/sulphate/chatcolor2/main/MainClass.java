package com.sulphate.chatcolor2.main;

import java.io.File;
import java.util.*;

import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.listeners.ColorGUIListener;
import com.sulphate.chatcolor2.listeners.CustomCommandListener;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.CC2Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.commands.ConfirmCommand;
import com.sulphate.chatcolor2.listeners.ChatListener;
import com.sulphate.chatcolor2.listeners.PlayerJoinListener;
import com.sulphate.chatcolor2.utils.CCStrings;

public class MainClass extends JavaPlugin {

    private static MainClass plugin;
    private HashMap<Player,ConfirmScheduler> toconfirm = new HashMap<>();
    private ConsoleCommandSender console = Bukkit.getConsoleSender();
    private static boolean pluginEnabled = true;
    private static CC2Utils utils = new CC2Utils();
    private HashMap<String, Object> defaultConfig = new HashMap<>();
    private AutoSaveScheduler autosaver;

    @Override
    public void onEnable() {
        plugin = this;
        boolean metrics;

        // Set up the default config.
        setupDefaultConfig();

        // Checking if first time setup needed (Config reload)
        if (!new File(getDataFolder(), "config.yml").exists() || !getConfig().getBoolean("loaded") || !getConfig().contains("loaded")) {
            reload();
        }

        // Checking config for errors!
        checkConfig();
        // Load all data.
        utils.loadAllData();
        // Load the messages.
        CCStrings.reloadMessages();
        // Start autosaver.
        autosaver = new AutoSaveScheduler();
        autosaver.startTask();

        //Checking if Metrics is allowed for this plugin
        metrics = getConfig().getBoolean("stats");
        if (metrics) {
            new Metrics(this);
        }

        //Console startup messages
        console.sendMessage("§b------------------------------------------------------------");
        console.sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + getDescription().getVersion() + " §ehas been §aLoaded§e!");
        console.sendMessage(CCStrings.prefix + "Current update: §b1.14.2 Compatability & Small Changes");
        if (!metrics) {
            console.sendMessage(CCStrings.prefix + "§bMetrics §eis §cdisabled §efor this plugin.");
        }
        else {
            console.sendMessage(CCStrings.prefix + "§bMetrics §eis §aenabled §efor this plugin. Stats sent to §bhttps://bstats.org");
        }
        console.sendMessage("§b------------------------------------------------------------");

        //Commands & Listeners
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("confirm").setExecutor(new ConfirmCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomCommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new ColorGUIListener(), this);
    }

    @Override
    public void onDisable() {
        autosaver.cancel();
        utils.saveAllData();
        plugin = null;
        console.sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + getDescription().getVersion() + " §ehas been §cDisabled§e!");
    }

    public static MainClass get() {
        return plugin;
    }

    public HashMap<Player, ConfirmScheduler> getConfirmees() {
        return toconfirm;
    }

    public void addConfirmee(Player p, ConfirmScheduler s) {
        toconfirm.put(p, s);
    }

    public void removeConfirmee(Player p) {
        toconfirm.remove(p);
    }

    private void setupDefaultConfig() {
        defaultConfig.put("loaded", true);
        defaultConfig.put("version", getDescription().getVersion());
        defaultConfig.put("stats", true);
        defaultConfig.put("settings.auto-save", true);
        defaultConfig.put("settings.color-override", false);
        defaultConfig.put("settings.notify-others", true);
        defaultConfig.put("settings.join-message", true);
        defaultConfig.put("settings.confirm-timeout", 10);
        defaultConfig.put("settings.default-color", "&f");
        defaultConfig.put("settings.rainbow-sequence", "abcde");
        defaultConfig.put("settings.command-name", "chatcolor");
        defaultConfig.put("messages.prefix", "&5&l[&6Chat&aC&bo&cl&do&er&5&l] &e");
        defaultConfig.put("messages.gui-title", "&9Color Picker GUI");
        defaultConfig.put("messages.help", "&eType &c/chatcolor cmdhelp &eto see valid colors, modifiers and settings!");
        defaultConfig.put("messages.not-enough-args", "&cNot enough arguments!");
        defaultConfig.put("messages.too-many-args", "&cToo many arguments!");
        defaultConfig.put("messages.player-not-joined", "&cThat player has not joined yet!");
        defaultConfig.put("messages.players-only", "&cThis command can only be run by players.");
        defaultConfig.put("messages.no-permissions", "&cYou do not have permission to use that command.");
        defaultConfig.put("messages.no-color-perms", "&cYou do not have permission to use the color: ");
        defaultConfig.put("messages.no-mod-perms", "&cYou do not have permission to use the modifier: &e");
        defaultConfig.put("messages.invalid-color", "&cInvalid color: &e");
        defaultConfig.put("messages.invalid-command", "&cThat is an invalid command!");
        defaultConfig.put("messages.invalid-modifier", "&cInvalid modifier: &e");
        defaultConfig.put("messages.invalid-setting", "&cInvalid setting: &e");
        defaultConfig.put("messages.needs-boolean", "&cThat setting requires a boolean! &eUse either &aTRUE &eor &cFALSE");
        defaultConfig.put("messages.needs-number", "&cThat setting requires a number!");
        defaultConfig.put("messages.current-color", "Your color is currently: ");
        defaultConfig.put("messages.set-own-color", "Successfully set your color to: ");
        defaultConfig.put("messages.set-others-color", "Successfully set &c[player]'s &ecolor to: ");
        defaultConfig.put("messages.player-set-your-color", "&c[player] &eset your color to: ");
        defaultConfig.put("messages.colour-already-set", "&cColour already selected!");
        defaultConfig.put("messages.gui-color-set", "&aColour set to ");
        defaultConfig.put("messages.gui-unavailable", "&cUnavailable");
        defaultConfig.put("messages.gui-active", "&aActive");
        defaultConfig.put("messages.this", "this");
        defaultConfig.put("messages.confirm", "Are you sure you want to do that? Type &c/confirm &eif you are sure.");
        defaultConfig.put("messages.did-not-confirm", "&cYou did not confirm in time. &eNothing has been changed.");
        defaultConfig.put("messages.already-confirming", "&cYou cannot do that until you have confirmed or waited.");
        defaultConfig.put("messages.nothing-to-confirm", "&cYou have nothing to confirm!");
        defaultConfig.put("messages.reloaded-messages", "Reloaded messages!");
        defaultConfig.put("messages.already-set", "&cThat value is already set!");
        defaultConfig.put("messages.is-currently", " &eis currently: ");
        defaultConfig.put("messages.to-change", "You are changing it to: ");
        defaultConfig.put("messages.command-exists", "&cThat command already exists!");
        defaultConfig.put("messages.internal-error", "&cInternal error. Please check the console for details.");
        defaultConfig.put("messages.error-details", "Error details: ");
        defaultConfig.put("messages.plugin-disabled", "The plugin has been disabled. Please type §b/chatcolor enable §eto attempt to re-enable.");
        defaultConfig.put("messages.failed-to-enable", "Failed to enable the plugin. Please try again, making sure all folders are not locked.");
        defaultConfig.put("messages.successfully-enabled", "The plugin has been successfully enabled.");
        defaultConfig.put("messages.already-enabled", "The plugin is already enabled.");
        defaultConfig.put("messages.black", "Black");
        defaultConfig.put("messages.darkblue", "Dark Blue");
        defaultConfig.put("messages.darkgreen", "Dark Green");
        defaultConfig.put("messages.darkaqua", "Dark Aqua");
        defaultConfig.put("messages.darkred", "Dark Red");
        defaultConfig.put("messages.darkpurple", "Dark Purple");
        defaultConfig.put("messages.gold", "Gold");
        defaultConfig.put("messages.gray", "Gray");
        defaultConfig.put("messages.darkgray", "Dark Gray");
        defaultConfig.put("messages.blue", "Blue");
        defaultConfig.put("messages.green", "Green");
        defaultConfig.put("messages.aqua", "Aqua");
        defaultConfig.put("messages.red", "Red");
        defaultConfig.put("messages.lightpurple", "Light Purple");
        defaultConfig.put("messages.yellow", "Yellow");
        defaultConfig.put("messages.white", "White");
        defaultConfig.put("messages.gui-click-to-toggle", "&eClick to Toggle");
        defaultConfig.put("messages.gui-selected", "&aSelected");
        defaultConfig.put("messages.gui-click-to-select", "&eClick to Select");
        defaultConfig.put("messages.gui-inactive", "&7Inactive");
        defaultConfig.put("messages.obfuscated", "Obfuscated");
        defaultConfig.put("messages.bold", "Bold");
        defaultConfig.put("messages.strikethrough", "Strikethrough");
        defaultConfig.put("messages.underlined", "Underlined");
        defaultConfig.put("messages.italic", "Italic");
        defaultConfig.put("messages.available-colors-and-modifiers", "Here are your available colors and modifiers:");
        defaultConfig.put("messages.colors", "Colors");
        defaultConfig.put("messages.modifiers", "Modifiers");
        defaultConfig.put("messages.rainbow", "Rainbow");
    }

    public void reload() {
        FileConfiguration config = getConfig();
        for (String key : defaultConfig.keySet()) {
            config.set(key, defaultConfig.get(key));
        }

        saveConfig();
        reloadConfig();
    }

    public void checkConfig() {
        // Loop through the default config keys, make sure that every value is set, and that each value does not throw an error.
        Set<String> keys = defaultConfig.keySet();
        FileConfiguration config = getConfig();

        // The message-list 'message' can now cause issues, so let's remove it.
        config.set("messages.message-list", null);

        for (String st : keys) {
            if (!config.contains(st)) {
                config.set(st, defaultConfig.get(st));
            }
        }
        for (String st : keys) {
            try {
                config.get(st);
            }
            catch(Exception e) {
                config.set(st, defaultConfig.get(st));
            }
        }
        if (!config.getString("version").equals(getDescription().getVersion())) {
            config.set("version", getDescription().getVersion());
        }

        saveConfig();
        reloadConfig();
    }

    public static boolean getPluginEnabled() {
        return pluginEnabled;
    }
    public static void setPluginEnabled(boolean enabled) {
        if (!enabled) {
            Bukkit.getLogger().info(CCStrings.plugindisabled);
        }
        pluginEnabled = enabled;
    }

    public static CC2Utils getUtils() {
        return utils;
    }

}
