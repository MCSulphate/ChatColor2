package com.sulphate.chatcolor2.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.sulphate.chatcolor2.listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.commands.ChatColorCommand;
import com.sulphate.chatcolor2.commands.ConfirmCommand;
import com.sulphate.chatcolor2.listeners.ChatListener;
import com.sulphate.chatcolor2.listeners.PlayerJoinListener;
import com.sulphate.chatcolor2.utils.CCStrings;

public class MainClass extends JavaPlugin {

    private static MainClass plugin;
    private HashMap<Player,ConfirmScheduler> toconfirm = new HashMap<Player,ConfirmScheduler>();

    @Override
    public void onEnable() {
        plugin = this;
        //Checking if first time setup needed (Config reload)
        if (getConfig().getString("loaded") == null) {
            reload();
        }
        else if (!getConfig().getString("version").equals(this.getDescription().getVersion())) {
            reload();
        }
        //Console startup messages
        Bukkit.getConsoleSender().sendMessage("§b------------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion() + " §ehas been §aLoaded§e!");
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "Current update: PlayerFiles integration!");
        Bukkit.getConsoleSender().sendMessage("§b------------------------------------------------------------");
        //Commands & Listeners
        getCommand("chatcolor").setExecutor(new ChatColorCommand());
        getCommand("confirm").setExecutor(new ConfirmCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    @Override
    public void onDisable() {
        plugin = null;
        Bukkit.getConsoleSender().sendMessage(CCStrings.prefix + "ChatColor 2 Version §b" + Bukkit.getPluginManager().getPlugin("ChatColor2").getDescription().getVersion() + " §ehas been §cDisabled§e!");
    }

    public static MainClass get() {
        return plugin;
    }

    public HashMap<Player,ConfirmScheduler> getConfirmees() {
        return toconfirm;
    }

    public void addConfirmee(Player p, ConfirmScheduler s) {
        toconfirm.put(p, s);
    }

    public void removeConfirmee(Player p) {
        toconfirm.remove(p);
    }


    public void reload() {
        getConfig().set("loaded", "no");
        getConfig().set("version", this.getDescription().getVersion());
        getConfig().set("settings.color-override", false);
        getConfig().set("settings.notify-others", true);
        getConfig().set("settings.join-message", true);
        getConfig().set("settings.confirm-timeout", 10);
        getConfig().set("settings.default-color", "&f");
        getConfig().set("settings.rainbow-sequence", "abcde");
        getConfig().set("messages.help", "&eType &c/chatcolor help &eto see valid colors, modifiers and settings!");
        getConfig().set("messages.players-only", "&cThis command can only be run by players.");
        getConfig().set("messages.player-not-online", "&cThat player is not online!");
        getConfig().set("messages.no-permissions", "&cYou do not have permission to use that command.");
        getConfig().set("messages.no-color-perms", "&cYou do not have permission to use that color.");
        getConfig().set("messages.no-col-mod-perms", "&cYou do not have permission to use that color or modifier!");
        getConfig().set("messages.invalid-color", "&cThat is an invalid color! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-command", "&cThat is an invalid command! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-modifier", "&cThat is an invalid modifier! &eType &d/chatcolor help &eto see valid colors and commands.");
        getConfig().set("messages.invalid-setting", "&cThat is an invalid setting!");
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
        getConfig().set("messages.reloaded-config", "Reloaded the config!");
        getConfig().set("messages.already-set", "&cThat value is already set!");
        getConfig().set("messages.set-description", "This command changes settings within the plugin.");
        List<String> messages = Arrays.asList("help", "players-only", "player-not-online", "no-permissions", "no-color-perms", "no-col-mod-perms", "invalid-color", "invalid-command", "invalid-setting", "needs-boolean", "needs-number", "current-color", "set-own-color", "set-others-color", "player-set-your-color", "this", "confirm", "did-not-confirm", "already-confirming", "nothing-to-confirm", "reloaded-config", "already-set", "set-description");
        getConfig().set("messages.message-list", messages);
        getConfig().set("loaded", "yes");
        saveConfig();
        reloadConfig();
    }

    public void check() {

    }

    public String getMessage(String message) {
        return getConfig().getString("messages." + message);
    }

}
