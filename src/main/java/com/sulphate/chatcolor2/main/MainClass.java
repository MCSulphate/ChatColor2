package com.sulphate.chatcolor2.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.listeners.CustomCommandListener;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.CC2Utils;
import com.sulphate.chatcolor2.utils.Metrics;
import org.bukkit.Bukkit;
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
    private Logger log = Bukkit.getLogger();
    private static boolean pluginEnabled = true;
    private static CC2Utils utils = new CC2Utils();
    private AutoSaveScheduler autosaver;

    @Override
    public void onEnable() {
        plugin = this;
        boolean metrics;

        //Checking if first time setup needed (Config reload)
        if (!new File(getDataFolder(), "config.yml").exists() || !getConfig().getBoolean("loaded") || !getConfig().contains("loaded")) {
            reload();
        }

        //Checking config for errors!
        checkConfig();
        //Load all data.
        utils.loadAllData();
        //Start autosaver.
        autosaver = new AutoSaveScheduler();
        autosaver.startTask();

        //Checking if Metrics is allowed for this plugin
        metrics = getConfig().getBoolean("stats");
        if (metrics) {
            new Metrics(this);
        }

        //Console startup messages
        log.info("§b------------------------------------------------------------");
        log.info(CCStrings.prefix + "ChatColor 2 Version §b" + getDescription().getVersion() + " §ehas been §aLoaded§e!");
        log.info(CCStrings.prefix + "Current update: §bConfigurable prefix!");
        if (!metrics) {
            log.info(CCStrings.prefix + "§bMetrics §eis §cdisabled §efor this plugin.");
        }
        else {
            log.info(CCStrings.prefix + "§bMetrics §eis §aenabled §efor this plugin. Stats sent to §bhttps://bstats.org");
        }
        log.info("§b------------------------------------------------------------");

        //Commands & Listeners
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("confirm").setExecutor(new ConfirmCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new CustomCommandListener(), this);
    }

    @Override
    public void onDisable() {
        autosaver.cancel();
        utils.saveAllData();
        plugin = null;
        log.info(CCStrings.prefix + "ChatColor 2 Version §b" + getDescription().getVersion() + " §ehas been §cDisabled§e!");
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

    public void reload() {
        getConfig().set("loaded", false);
        getConfig().set("version", getDescription().getVersion());
        getConfig().set("stats", true);
        getConfig().set("settings.auto-save", true);
        getConfig().set("settings.color-override", true);
        getConfig().set("settings.notify-others", true);
        getConfig().set("settings.join-message", true);
        getConfig().set("settings.confirm-timeout", 10);
        getConfig().set("settings.default-color", "&f");
        getConfig().set("settings.rainbow-sequence", "abcde");
        getConfig().set("settings.command-name", "chatcolor");
        getConfig().set("messages.prefix", "&5&l[&6Chat&aC&bo&cl&do&er&5&l] &e");
        getConfig().set("messages.help", "&eType &c/chatcolor cmdhelp &eto see valid colors, modifiers and settings!");
        getConfig().set("messages.not-enough-args", "&cNot enough arguments!");
        getConfig().set("messages.too-many-args", "&cToo many arguments!");
        getConfig().set("messages.player-not-joined", "&cThat player has not joined yet!");
        getConfig().set("messages.players-only", "&cThis command can only be run by players.");
        getConfig().set("messages.no-permissions", "&cYou do not have permission to use that command.");
        getConfig().set("messages.no-color-perms", "&cYou do not have permission to use the color: &");
        getConfig().set("messages.no-mod-perms", "&cYou do not have permission to use the modifier: &e&");
        getConfig().set("messages.invalid-color", "&cInvalid color: &e");
        getConfig().set("messages.invalid-command", "&cThat is an invalid command!");
        getConfig().set("messages.invalid-modifier", "&cInvalid modifier: &e");
        getConfig().set("messages.invalid-setting", "&cInvalid setting: &e");
        getConfig().set("messages.needs-boolean", "&cThat setting requires a boolean! &eUse either &aTRUE &eor &cFALSE");
        getConfig().set("messages.needs-number", "&cThat setting requires a number!");
        getConfig().set("messages.current-color", "Your color is currently: ");
        getConfig().set("messages.set-own-color", "Successfully set your color to: ");
        getConfig().set("messages.set-others-color", "Successfully set &c[player]'s &ecolor to: ");
        getConfig().set("messages.player-set-your-color", "&c[player] &eset your color to: ");
        getConfig().set("messages.this", "this");
        getConfig().set("messages.confirm", "Are you sure you want to do that? Type &c/confirm &eif you are sure.");
        getConfig().set("messages.did-not-confirm", "&cYou did not confirm in time. &eNothing has been changed.");
        getConfig().set("messages.already-confirming", "&cYou cannot do that until you have confirmed or waited.");
        getConfig().set("messages.nothing-to-confirm", "&cYou have nothing to confirm!");
        getConfig().set("messages.reloaded-messages", "Reloaded messages!");
        getConfig().set("messages.already-set", "&cThat value is already set!");
        getConfig().set("messages.is-currently", " &eis currently: ");
        getConfig().set("messages.to-change", "You are changing it to: ");
        getConfig().set("messages.command-exists", "&cThat command already exists!");
        getConfig().set("messages.internal-error", "&cInternal error. Please check the console for details.");
        getConfig().set("messages.error-details", "Error details: ");
        getConfig().set("messages.plugin-disabled", "The plugin has been disabled. Please type §b/chatcolor enable §eto attempt to re-enable.");
        getConfig().set("messages.failed-to-enable", "Failed to enable the plugin. Please try again, making sure all folders are not locked.");
        getConfig().set("messages.successfully-enabled", "The plugin has been successfully enabled.");
        getConfig().set("messages.already-enabled", "The plugin is already enabled.");
        List<String> messages = Arrays.asList("help", "not-enough-args", "too-many-args", "player-not-joined", "players-only", "no-permissions", "no-color-perms", "no-col-mod-perms", "invalid-color", "invalid-command", "invalid-setting", "needs-boolean", "needs-number", "current-color", "set-own-color", "set-others-color", "player-set-your-color", "this", "confirm", "did-not-confirm", "already-confirming", "nothing-to-confirm", "reloaded-config", "already-set", "is-currently", "to-change");
        getConfig().set("messages.message-list", messages);
        getConfig().set("loaded", true);
        saveConfig();
        reloadConfig();
    }

    public void checkConfig() {
        
        //HashMap with all the defaults in it
        HashMap<String, Object> hmp = new HashMap<String, Object>();
        hmp.put("loaded", true);
        hmp.put("version", getDescription().getVersion());
        hmp.put("stats", true);
        hmp.put("settings.auto-save", true);
        hmp.put("settings.color-override", false);
        hmp.put("settings.notify-others", true);
        hmp.put("settings.join-message", true);
        hmp.put("settings.confirm-timeout", 10);
        hmp.put("settings.default-color", "&f");
        hmp.put("settings.rainbow-sequence", "abcde");
        hmp.put("settings.command-name", "chatcolor");
        hmp.put("messages.prefix", "&5&l[&6Chat&aC&bo&cl&do&er&5&l] &e");
        hmp.put("messages.help", "&eType &c/chatcolor cmdhelp &eto see valid colors, modifiers and settings!");
        hmp.put("messages.not-enough-args", "&cNot enough arguments!");
        hmp.put("messages.too-many-args", "&cToo many arguments!");
        hmp.put("messages.player-not-joined", "&cThat player has not joined yet!");
        hmp.put("messages.players-only", "&cThis command can only be run by players.");
        hmp.put("messages.no-permissions", "&cYou do not have permission to use that command.");
        hmp.put("messages.no-color-perms", "&cYou do not have permission to use the color: &");
        hmp.put("messages.no-mod-perms", "&cYou do not have permission to use the modifier: &e&");
        hmp.put("messages.invalid-color", "&cInvalid color: &e");
        hmp.put("messages.invalid-command", "&cThat is an invalid command!");
        hmp.put("messages.invalid-modifier", "&cInvalid modifier: &e");
        hmp.put("messages.invalid-setting", "&cInvalid setting: &e");
        hmp.put("messages.needs-boolean", "&cThat setting requires a boolean! &eUse either &aTRUE &eor &cFALSE");
        hmp.put("messages.needs-number", "&cThat setting requires a number!");
        hmp.put("messages.current-color", "Your color is currently: ");
        hmp.put("messages.set-own-color", "Successfully set your color to: ");
        hmp.put("messages.set-others-color", "Successfully set &c[player]'s &ecolor to: ");
        hmp.put("messages.player-set-your-color", "&c[player] &eset your color to: ");
        hmp.put("messages.this", "this");
        hmp.put("messages.confirm", "Are you sure you want to do that? Type &c/confirm &eif you are sure.");
        hmp.put("messages.did-not-confirm", "&cYou did not confirm in time. &eNothing has been changed.");
        hmp.put("messages.already-confirming", "&cYou cannot do that until you have confirmed or waited.");
        hmp.put("messages.nothing-to-confirm", "&cYou have nothing to confirm!");
        hmp.put("messages.reloaded-messages", "Reloaded messages!");
        hmp.put("messages.already-set", "&cThat value is already set!");
        hmp.put("messages.is-currently", " &eis currently: ");
        hmp.put("messages.to-change", "You are changing it to: ");
        hmp.put("messages.command-exists", "&cThat command already exists!");
        hmp.put("messages.internal-error", "&cInternal error. Please check the console for details.");
        hmp.put("messages.error-details", "Error details: ");
        hmp.put("messages.plugin-disabled", "The plugin has been disabled. Please type §b/chatcolor enable §eto attempt to re-enable.");
        hmp.put("messages.failed-to-enable", "Failed to enable the plugin. Please try again, making sure all folders are not locked.");
        hmp.put("messages.successfully-enabled", "The plugin has been successfully enabled.");
        hmp.put("messages.already-enabled", "The plugin is already enabled.");
        
        //ArrayList with all keys
        List<String> keys = new ArrayList<String>();
        keys.add("loaded");
        keys.add("version");
        keys.add("stats");
        keys.add("settings.auto-save");
        keys.add("settings.color-override");
        keys.add("settings.notify-others");
        keys.add("settings.join-message");
        keys.add("settings.confirm-timeout");
        keys.add("settings.default-color");
        keys.add("settings.rainbow-sequence");
        keys.add("settings.command-name");
        keys.add("messages.prefix");
        keys.add("messages.help");
        keys.add("messages.not-enough-args");
        keys.add("messages.too-many-args");
        keys.add("messages.player-not-joined");
        keys.add("messages.players-only");
        keys.add("messages.player-not-online");
        keys.add("messages.no-permissions");
        keys.add("messages.no-color-perms");
        keys.add("messages.no-col-mod-perms");
        keys.add("messages.invalid-color");
        keys.add("messages.invalid-command");
        keys.add("messages.invalid-modifier");
        keys.add("messages.invalid-setting");
        keys.add("messages.needs-boolean");
        keys.add("messages.needs-number");
        keys.add("messages.current-color");
        keys.add("messages.set-own-color");
        keys.add("messages.set-others-color");
        keys.add("messages.player-set-your-color");
        keys.add("messages.this");
        keys.add("messages.confirm");
        keys.add("messages.did-not-confirm");
        keys.add("messages.already-confirming");
        keys.add("messages.nothing-to-confirm");
        keys.add("messages.reloaded-messages");
        keys.add("messages.already-set");
        keys.add("messages.is-currently");
        keys.add("messages.to-change");
        keys.add("messages.command-exists");
        keys.add("messages.internal-error");
        keys.add("messages.error-details");
        keys.add("messages.plugin-disabled");
        keys.add("messages.failed-to-enable");
        keys.add("messages.successfully-enabled");
        keys.add("messages.already-enabled");

        for (String st : keys) {
            if (!getConfig().contains(st)) {
                getConfig().set(st, hmp.get(st));
            }
        }
        for (String st : keys) {
            try {
                getConfig().get(st);
            }
            catch(Exception e) {
                getConfig().set(st, hmp.get(st));
                saveConfig();
            }
        }
        if (!getConfig().getString("version").equals(getDescription().getVersion())) {
            getConfig().set("version", getDescription().getVersion());
            saveConfig();
        }

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
