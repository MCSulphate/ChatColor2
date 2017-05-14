package com.sulphate.chatcolor2.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;

public class ConfirmCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CCStrings.playersonly);
            return true;
        }

        Player s = (Player) sender;

        if (!MainClass.get().getConfirmees().containsKey(s)) {
            s.sendMessage(CCStrings.nothingtoconfirm);
            return true;
        }

        ConfirmScheduler cs = MainClass.get().getConfirmees().get(s);
        String type = cs.type;
        cs.cancelTask();
        MainClass.get().removeConfirmee(s);

        switch(type) {

            case "color-override": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.get().getConfig().set("settings.color-override", val);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                if (val) {
                    s.sendMessage(CCStrings.prefix + "Success! §ccolor-override §eis now §aTRUE");
                } else {
                    s.sendMessage(CCStrings.prefix + "Success! §ccolor-override §eis now §cFALSE");
                }
                return true;
            }
            case "reset": {
                if (!s.hasPermission("chatcolor.admin.reset") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*" ) && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                s.sendMessage(CCStrings.prefix + "Config reset!");
                MainClass.get().reload();
                MainClass.get().reloadConfig();
                return true;
            }
            case "notify-others": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.get().getConfig().set("settings.notify-others", val);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                if (val) {
                    s.sendMessage(CCStrings.prefix + "Success! §cnotify-others §eis now §aTRUE");
                } else {
                    s.sendMessage(CCStrings.prefix + "Success! §cnotify-others §eis now §cFALSE");
                }
                return true;
            }
            case "join-message": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.get().getConfig().set("settings.join-message", val);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                if (val) {
                    s.sendMessage(CCStrings.prefix + "Success! §cjoin-message §eis now §aTRUE");
                } else {
                    s.sendMessage(CCStrings.prefix + "Success! §cjoin-message §eis now §cFALSE");
                }
                return true;
            }
            case "confirm-timeout": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                int val = (int) cs.val;
                MainClass.get().getConfig().set("settings.confirm-timeout", val);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                s.sendMessage(CCStrings.prefix + "Success! §cconfirm-timeout §ehas been set to §c" + val + "seconds");
                return true;
            }
            case "default-color": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                String color = (String) cs.val;
                MainClass.get().getConfig().set("settings.default-color", color.replace("§", "&"));
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                ColorUtils.newDefaultColor(color);
                s.sendMessage(CCStrings.prefix + "Success! §cdefault-color §ehas been set to " + color + "this");
                return true;
            }
            case "rainbow-sequence": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                }
                String seq = (String) cs.val;
                MainClass.get().getConfig().set("settings.rainbow-sequence", seq);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                String[] ss = seq.split("");
                StringBuilder sb = new StringBuilder();
                for (String st : ss) {
                    sb.append("§" + st + st);
                }
                String rc = sb.toString();
                s.sendMessage(CCStrings.prefix + "Success! §crainbow-sequence §ehas been set to §r" + rc);
                return true;
            }
            case "command-name": {
                if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*") && !s.isOp()) {
                    s.sendMessage(CCStrings.nopermissions);
                }
                String cmnd = (String) cs.val;
                MainClass.get().getConfig().set("settings.command-name", cmnd);
                MainClass.get().saveConfig();
                MainClass.get().reloadConfig();
                Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("ChatColor2"));
                Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin("ChatColor2"));
                s.sendMessage(CCStrings.prefix + "Success! §ccommand-name §ehas been set to §c/" + cmnd);
                return true;
            }

        }

        return true;

    }

}
