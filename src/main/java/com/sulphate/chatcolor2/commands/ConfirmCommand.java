package com.sulphate.chatcolor2.commands;

import org.bukkit.ChatColor;
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
            case "auto-save": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.getUtils().setSetting("auto-save", val);
                String col = val ? "§aTRUE" : "§cFALSE";
                s.sendMessage(CCStrings.prefix + "Success! §cauto-save §eis now " + col);
                return true;
            }
            case "color-override": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.getUtils().setSetting("color-override", val);
                String col = val ? "§aTRUE" : "§cFALSE";
                s.sendMessage(CCStrings.prefix + "Success! §ccolor-override §eis now " + col);
                return true;
            }
            case "reset": {
                if (!hasPermission("chatcolor.admin.reset", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                MainClass.get().reload();
                MainClass.getUtils().loadSettings();
                MainClass.getUtils().loadMessages();
                s.sendMessage(CCStrings.prefix + "Config reset!");
                return true;
            }
            case "notify-others": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.getUtils().setSetting("notify-others", val);
                String col = val ? "§aTRUE" : "§cFALSE";
                s.sendMessage(CCStrings.prefix + "Success! §cnotify-others §eis now " + col);
                return true;
            }
            case "join-message": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                boolean val = (boolean) cs.val;
                MainClass.getUtils().setSetting("join-message", val);
                String col = val ? "§aTRUE" : "§cFALSE";
                s.sendMessage(CCStrings.prefix + "Success! §cjoin-message §eis now " + col);
                return true;
            }
            case "confirm-timeout": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                int val = (int) cs.val;
                MainClass.getUtils().setSetting("confirm-timeout", val);
                s.sendMessage(CCStrings.prefix + "Success! §cconfirm-timeout §ehas been set to §c" + val + "seconds");
                return true;
            }
            case "default-color": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                String color = (String) cs.val;
                MainClass.getUtils().setSetting("default-color", color);
                MainClass.getUtils().newDefaultColor(color);
                s.sendMessage(CCStrings.prefix + "Success! §cdefault-color §ehas been set to " + color.replace('&', ChatColor.COLOR_CHAR) + "this");
                return true;
            }
            case "rainbow-sequence": {
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                String seq = (String) cs.val;
                MainClass.getUtils().setSetting("rainbow-sequence", seq);
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
                if (!hasPermission("chatcolor.admin.set", s)) {
                    s.sendMessage(CCStrings.nopermissions);
                    return true;
                }
                String cmnd = (String) cs.val;
                MainClass.getUtils().setSetting("command-name", cmnd);
                s.sendMessage(CCStrings.prefix + "Success! §ccommand-name §ehas been set to §c/" + cmnd);
                return true;
            }

        }

        return true;

    }

    public boolean hasPermission(String permission, Player player) {
        int d1 = permission.indexOf(".");
        String root = permission.substring(0, d1 + 1);
        String s = permission.replace(root, "");
        int d2 = s.indexOf(".");
        String cat = s.substring(0, d2 + 1);
        if (!player.hasPermission(permission) && !player.hasPermission(root + cat + ".*") && !player.hasPermission(root + ".*")) {
            return false;
        }
        return true;
    }

}
