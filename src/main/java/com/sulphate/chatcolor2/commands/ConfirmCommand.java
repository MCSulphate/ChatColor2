package com.sulphate.chatcolor2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;
import com.sulphate.chatcolor2.utils.ColorUtils;

public class ConfirmCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CCStrings.notplayer);
            return true;
        }

        Player s = (Player) sender;

        if (!MainClass.get().getConfirmees().containsKey(s)) {
            s.sendMessage(CCStrings.noconfirm);
            return true;
        }

        ConfirmScheduler cs = MainClass.get().getConfirmees().get(s);
        String type = cs.type;
        cs.cancelTask();
        MainClass.get().removeConfirmee(s);

        if (type.equals("color-override")) {
            if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
                return true;
            }
            boolean val = (boolean) cs.val;
            MainClass.get().getConfig().set("settings.color-override", val);
            MainClass.get().saveConfig();
            MainClass.get().reloadConfig();
            if (val) {
                s.sendMessage(CCStrings.prefix + "Success! §ccolor-override §eis now §aTRUE");
            }
            else {
                s.sendMessage(CCStrings.prefix + "Success! §ccolor-override §eis now §cFALSE");
            }
            return true;
        }
        else if (type.equals("reset")) {
            if (!s.hasPermission("chatcolor.admin.reset") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
                return true;
            }
            s.sendMessage(CCStrings.prefix + "Config reset!");
            MainClass.get().reload();
            MainClass.get().reloadConfig();
            return true;
        }
        else if (type.equals("notify-others")) {
            if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
                return true;
            }
            boolean val = (boolean) cs.val;
            MainClass.get().getConfig().set("settings.notify-others", val);
            MainClass.get().saveConfig();
            MainClass.get().reloadConfig();
            if (val) {
                s.sendMessage(CCStrings.prefix + "Success! §cnotify-others §eis now §aTRUE");
            }
            else {
                s.sendMessage(CCStrings.prefix + "Success! §cnotify-others §eis now §cFALSE");
            }
            return true;
        }
        else if (type.equals("join-message")) {
            if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
                return true;
            }
            boolean val = (boolean) cs.val;
            MainClass.get().getConfig().set("settings.join-message", val);
            MainClass.get().saveConfig();
            MainClass.get().reloadConfig();
            if (val) {
                s.sendMessage(CCStrings.prefix + "Success! §cjoin-message §eis now §aTRUE");
            }
            else {
                s.sendMessage(CCStrings.prefix + "Success! §cjoin-message §eis now §cFALSE");
            }
            return true;
        }
        else if (type.equals("confirm-timeout")) {
            if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
                return true;
            }
            int val = (int) cs.val;
            MainClass.get().getConfig().set("settings.confirm-timeout", val);
            MainClass.get().saveConfig();
            MainClass.get().reloadConfig();
            s.sendMessage(CCStrings.prefix + "Success! §cconfirm-timeout §ehas been set to §c" + val + "seconds");
            return true;
        }
        else if (type.equals("default-color")) {
            if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                s.sendMessage(CCStrings.noperms);
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

        return true;

    }

}
