package com.sulphate.chatcolor2.test;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.CCStrings;
import com.sulphate.chatcolor2.utils.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class NewChatColorCommand implements CommandExecutor {

    //This is a test class. It has not been implemented.
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int argsno = args.length;

        //These strings will be put into CCStrings and the config when testing is done.
        String notargs = CCStrings.prefix + "&cNot enough arguments!";

        //This is a test for console commands.
        if (sender instanceof Player) {

            Player s = (Player) sender;
            if (!checkCommand(args, s)) {
                return true;
            }

            List<String> cmds = Arrays.asList("help", "set", "reset", "reload");
            if (cmds.contains(args[0])) {
                switch(args[0]) {
                    case "help": {
                        handleHelp(s);
                        return true;
                    }
                    case "set": {
                        handleSet(args, s);
                        return true;
                    }
                    case "reset": {
                        s.sendMessage(CCStrings.confirm);
                        ConfirmScheduler cs = new ConfirmScheduler();
                        MainClass.get().addConfirmee(s, cs);
                        cs.confirm(s, "reset", null);
                        return true;
                    }
                    case "reload": {
                        MainClass.get().checkConfig();
                        s.sendMessage(CCStrings.relconfig);
                        return true;
                    }
                }
            }

        }

        //This is the console.
        else {

            if (argsno < 2) {
                sender.sendMessage(notargs);
                return true;
            }

        }
        return true;

    }

    //This is a test for a universal permissions checker, to clean up the code.
    public boolean checkCommand(String[] args, Player player) {

        if (player.hasPermission("chatcolor.*")) {
            return true;
        }

        if (!player.hasPermission("chatcolor.use")) {
            player.sendMessage(CCStrings.noperms);
            return false;
        }

        List<String> cmds = Arrays.asList("set", "reload", "reset");
        if (cmds.contains(args[0])) {
            if (!hasPermission("chatcolor.admin." + args[0], player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            return true;
        }

        if (FileUtils.getPlayerFile(args[0]) != null) {
            if (!hasPermission("chatcolor.change.others", player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            if (getColor(args[1]) != null) {
                for (int i = 0; i < args.length; i++) {
                    if (i == 0) {
                        if (!hasPermission("chatcolor.color." + args[0], player)) {
                            player.sendMessage(CCStrings.nocolperm + args[0] + args[0]);
                            return false;
                        }
                        continue;
                    }
                    if (getModifier(args[i]) == null) {
                        player.sendMessage(CCStrings.invmod + args[i]);
                        return false;
                    }
                    if (!hasPermission("chatcolor.modifier." + args[i], player)) {
                        player.sendMessage(CCStrings.nomodperm + args[i] + args[i]);
                        return false;
                    }
                }
                return true;
            }
            player.sendMessage(CCStrings.invcol + args[0] + args[0]);
            return false;
        }

        if (getColor(args[0]) != null) {
            if (!hasPermission("chatcolor.change.self", player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    if (!hasPermission("chatcolor.color." + args[0], player)) {
                        player.sendMessage(CCStrings.nocolperm + args[0] + args[0]);
                        return false;
                    }
                    continue;
                }
                if (getModifier(args[i]) == null) {
                    player.sendMessage(CCStrings.invmod + args[i]);
                    return false;
                }
                if (!hasPermission("chatcolor.modifier." + args[i], player)) {
                    player.sendMessage(CCStrings.nomodperm + args[i] + args[i]);
                    return false;
                }
            }
            return true;
        }
        if (args[0].length() == 1) {
            player.sendMessage(CCStrings.invcol + args[0]);
            return false;
        }
        player.sendMessage(CCStrings.invcom);
        return false;

    }

    //This is a test for checking a single permission node.
    public boolean hasPermission(String permission, Player player) {
        int d1 = permission.indexOf(".");
        String root = permission.substring(0, d1 + 1);
        String s = permission.replace(root, "");
        int d2 = s.indexOf(".");
        String cat = s.substring(0, d2 + 1);
        if (!player.hasPermission(permission) && !player.hasPermission(root + cat + ".*")) {
            return false;
        }
        return true;
    }

    //This is how the help command will be handled.
    public void handleHelp(Player player) {
        player.sendMessage(CCStrings.prefix + "Displaying help!");
        player.sendMessage(colorString(" &7- &eMain Command: &b/chatcolor <color> [modifiers]"));
        player.sendMessage(colorString(" &eOther Commands:"));
        player.sendMessage(colorString(" &eBANTER"));
    }

    //This is how the set command will be handled.
    public void handleSet(String[] args, Player player) {

    }

    //New method for safety of colors.
    public String colorString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public boolean verifyRainbowSequence(String seq) {

        boolean verify = true;
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");
        for (String s : chars) {
            if (!cols.contains(s)) {
                verify = false;
            }
        }
        return verify;
    }

    public String getColor(String s) {
        if (s.equalsIgnoreCase("rainbow")) {
            return s;
        }
        List<String> words = Arrays.asList("black", "dark.blue", "green", "dark.aqua", "red", "purple", "gold", "gray", "dark.grey", "blue", "light.green", "aqua", "light.red", "magenta", "yellow", "white");
        if (words.contains(s)) {
            for (Integer i = 0; i < words.size(); i++) {
                if (i == 10) {
                    break;
                }
                String st = words.get(i);
                if (s.equalsIgnoreCase(st)) {
                    return "§" + i.toString();
                }
            }
            if (s.equalsIgnoreCase("light.green")) {
                return "§a";
            }
            else if (s.equalsIgnoreCase("aqua")) {
                return "§b";
            }
            else if (s.equalsIgnoreCase("light.red")) {
                return "§c";
            }
            else if (s.equalsIgnoreCase("magenta")) {
                return "§d";
            }
            else if (s.equalsIgnoreCase("yellow")) {
                return "§e";
            }
            else if (s.equalsIgnoreCase("white")) {
                return "§f";
            }
        }
        List<String> other = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        if (other.contains(s)) {
            return "§" + s;
        }
        else {
            return null;
        }
    }

    public String getModifier(String s) {
        if (s.equalsIgnoreCase("obfuscated")) {
            return "§k";
        }
        else if (s.equalsIgnoreCase("bold")) {
            return "§l";
        }
        else if (s.equalsIgnoreCase("strikethrough")) {
            return "§m";
        }
        else if (s.equalsIgnoreCase("underlined")) {
            return "§n";
        }
        else if (s.equalsIgnoreCase("italic")) {
            return "§o";
        }
        else {
            List<String> other = Arrays.asList("k", "l", "m", "n", "o");
            if (other.contains(s)) {
                return "§" + s;
            }
            return null;
        }
    }

}
