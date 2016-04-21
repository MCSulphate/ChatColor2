package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.CCStrings;
import com.sulphate.chatcolor2.utils.ColorUtils;
import com.sulphate.chatcolor2.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class NewChatColorCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int argsno = args.length;

        if (sender instanceof Player) {

            Player s = (Player) sender;
            if (!checkCommand(args, s)) {
                return true;
            }

            List<String> cmds = Arrays.asList("cmdhelp", "permhelp", "set", "reset", "reload");
            if (cmds.contains(args[0].toLowerCase())) {
                switch(args[0].toLowerCase()) {
                    case "cmdhelp": {
                        handleCmdHelp(s);;
                        return true;
                    }
                    case "permhelp": {
                        handlePermHelp(s);
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

            if (FileUtils.getPlayerListConfig().contains(args[0])) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (i == 1) {
                        sb.append(getColor(args[i]));
                        continue;
                    }
                    sb.append(getModifier(args[i]));
                }
                String color = sb.toString();
                if (color.contains("rainbow")) {
                    verifyRainbowSequence(MainClass.get().getConfig().getString("settings.rainbow-sequence"), true);
                    char[] seq = MainClass.get().getConfig().getString("settings.rainbow-sequence").toCharArray();
                    StringBuilder sb2 = new StringBuilder();
                    String mods = color.replace("rainbow", "");
                    for (char c : seq) {
                        sb2.append("&" + c + mods + c);
                    }
                    String end = colorString(sb2.toString());
                    ColorUtils.setColor(args[0], colorString(color));
                    s.sendMessage(CCStrings.setothc.replace("[player]", args[0]) + end);
                    if (MainClass.get().getConfig().getBoolean("settings.notify-others") && Bukkit.getPlayer(args[0]) != null) {
                        Bukkit.getPlayer(args[0]).sendMessage(CCStrings.setyourc.replace("[player]", s.getName()) + end);
                    }
                    return true;
                }
                ColorUtils.setColor(args[0], colorString(color));
                s.sendMessage(CCStrings.setothc.replace("[player]", args[0]) + colorString(color) + CCStrings.colthis);
                if (MainClass.get().getConfig().getBoolean("settings.notify-others") && Bukkit.getPlayer(args[0]) != null) {
                    Bukkit.getPlayer(args[0]).sendMessage(CCStrings.setyourc.replace("[player]", s.getName()) + colorString(color) + CCStrings.colthis);
                }
                return true;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    sb.append(getColor(args[i]));
                    continue;
                }
                sb.append(getModifier(args[i]));
            }
            String color = sb.toString();
            if (color.contains("rainbow")) {
                verifyRainbowSequence(MainClass.get().getConfig().getString("settings.rainbow-sequence"), true);
                char[] seq = MainClass.get().getConfig().getString("settings.rainbow-sequence").toCharArray();
                StringBuilder sb2 = new StringBuilder();
                String mods = color.replace("rainbow", "");
                for (char c : seq) {
                    sb2.append("&" + c + mods + c);
                }
                String end = colorString(sb2.toString());
                ColorUtils.setColor(s.getName(), colorString(color));
                s.sendMessage(CCStrings.setownc + end);
                return true;
            }
            ColorUtils.setColor(s.getName(), colorString(color));
            s.sendMessage(CCStrings.setownc + colorString(color) + CCStrings.colthis);
            return true;

        }

        else {
            if (argsno < 2) {
                sender.sendMessage(CCStrings.notargs);
                return true;
            }
            if (argsno > 6) {
                sender.sendMessage(CCStrings.plusargs);
                return true;
            }
            if (FileUtils.getPlayerListConfig().contains(args[0])) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    if (i == 1) {
                        sb.append(getColor(args[i]));
                        continue;
                    }
                    sb.append(getColor(args[i]));
                }
                String color = sb.toString();
                if (color.contains("rainbow")) {
                    verifyRainbowSequence(MainClass.get().getConfig().getString("settings.rainbow-sequence"), true);
                    char[] seq = MainClass.get().getConfig().getString("settings.rainbow-sequence").toCharArray();
                    StringBuilder sb2 = new StringBuilder();
                    String mods = color.replace("rainbow", "");
                    for (char c : seq) {
                        sb2.append("&" + c + mods + c);
                    }
                    String end = colorString(sb2.toString());
                    ColorUtils.setColor(args[0], colorString(color));
                    sender.sendMessage(CCStrings.setothc.replace("[player]", args[0]) + end);
                    if (MainClass.get().getConfig().getBoolean("settings.notify-others") && Bukkit.getPlayer(args[0]) != null) {
                        Bukkit.getPlayer(args[0]).sendMessage(CCStrings.setyourc.replace("[player]", "CONSOLE") + end);
                    }
                    return true;
                }
                ColorUtils.setColor(args[0], colorString(color));
                sender.sendMessage(CCStrings.setothc.replace("[player]", args[0]) + colorString(color) + CCStrings.colthis);
                if (MainClass.get().getConfig().getBoolean("settings.notify-others") && Bukkit.getPlayer(args[0]) != null) {
                    Bukkit.getPlayer(args[0]).sendMessage(CCStrings.setyourc.replace("[player]", "CONSOLE") + colorString(color) + CCStrings.colthis);
                }
                return true;
            }
            else {
                sender.sendMessage(CCStrings.notjoin);
            }
        }
        return true;

    }

    public boolean checkCommand(String[] args, Player player) {

        boolean other = false;

        if (args.length == 0) {
            String color = ColorUtils.getColor(player.getName());
            if (color.contains("rainbow")) {
                verifyRainbowSequence(MainClass.get().getConfig().getString("settings.rainbow-sequence"), true);
                char[] seq = MainClass.get().getConfig().getString("settings.rainbow-sequence").toCharArray();
                StringBuilder sb = new StringBuilder();
                String mods = color.replace("rainbow", "");
                for (char c : seq) {
                    sb.append("&" + c + mods + c);
                }
                String end = sb.toString();
                player.sendMessage(CCStrings.yourcol + end);
                return false;
            }
            player.sendMessage(CCStrings.yourcol + color + CCStrings.colthis);
            return false;
        }

        if (!player.hasPermission("chatcolor.use")) {
            player.sendMessage(CCStrings.noperms);
            return false;
        }

        List<String> cmds = Arrays.asList("set", "reload", "reset", "permhelp");
        if (cmds.contains(args[0])) {
            if (args[0].equalsIgnoreCase("set") && args.length < 3) {
                player.sendMessage(CCStrings.notargs);
                return false;
            }
            List<String> settings = Arrays.asList("color-override", "notify-others", "join-message", "confirm-timeout", "default-color", "rainbow-sequence", "command-name");
            if (args[0].equalsIgnoreCase("set") && !settings.contains(args[1])) {
                player.sendMessage(CCStrings.invset + colorString("&e" + args[1]));
                return false;
            }
            if ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset")) && MainClass.get().getConfirmees().containsKey(player)) {
                player.sendMessage(CCStrings.alreadycon);
                return false;
            }
            if (!hasPermission("chatcolor.admin." + args[0], player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            return true;
        }

        if (FileUtils.getPlayerListConfig().contains(args[0])) {
            other = true;
            if (!hasPermission("chatcolor.change.others", player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            if (args.length > 6) {
                player.sendMessage(CCStrings.plusargs);
                return false;
            }
            if (args.length < 2) {
                player.sendMessage(CCStrings.notargs);
                return false;
            }
            if (getColor(args[1]) != null) {
                for (int i = 1; i < args.length; i++) {
                    if (i == 1) {
                        if (!hasPermission("chatcolor.color." + args[1], player)) {
                            player.sendMessage(CCStrings.nocolperm + args[1] + args[1]);
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
            if (args[1].length() == 1) {
                player.sendMessage(CCStrings.invcol + args[1]);
            }
            player.sendMessage(CCStrings.invcom);
            return false;
        }

        if (getColor(args[0]) != null) {
            if (!hasPermission("chatcolor.change.self", player)) {
                player.sendMessage(CCStrings.noperms);
                return false;
            }
            if (args.length > 5) {
                player.sendMessage(CCStrings.plusargs);
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
        if (other) {
            player.sendMessage(CCStrings.invcol + args[1]);
        }
        player.sendMessage(CCStrings.invcom);
        return false;

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

    //This is how the help command will be handled.
    public void handleCmdHelp(Player player) {
        player.sendMessage(CCStrings.prefix + "Displaying command help!");
        player.sendMessage(colorString(" &7- &eMain Command: &c/chatcolor <color> [modifiers]"));
        player.sendMessage("");
        player.sendMessage(colorString(" &eOther Commands:"));
        player.sendMessage(colorString(" &7- &eCommand Help: &c/chatcolor cmdhelp"));
        if (hasPermission("chatcolor.admin.permhelp", player)) {
            player.sendMessage(colorString(" &7- &ePerms Help: &c/chatcolor permhelp"));
        }
        if (hasPermission("chatcolor.admin.reload", player)) {
            player.sendMessage(colorString(" &7- &eReload Config: &c/chatcolor reload"));
        }
        if (hasPermission("chatcolor.admin.reset", player)) {
            player.sendMessage(colorString(" &7- &eReset Config: &c/chatcolor reset"));
        }
        if (hasPermission("chatcolor.admin.set", player)) {
            player.sendMessage(colorString(" &7- &eSet Settings: &c/chatcolor set <setting> <value>"));
        }
        player.sendMessage("");
        player.sendMessage(colorString("&eValid Colors:"));
        player.sendMessage(colorString("&00&11&22&33&44&55&66&77&88&99"));
        player.sendMessage(colorString("&aa&bb&cc&dd&ee&ff"));
        player.sendMessage("");
        player.sendMessage(colorString("&eAlternatively:"));
        player.sendMessage(colorString("&0black, &1dark.blue, &2green, &3dark.aqua,"));
        player.sendMessage(colorString("&4red, &5purple, &6gold, &7grey, &8dark.grey, &9blue"));
        player.sendMessage(colorString("&alight.green, &baqua, &clight.red, &dmagenta, &eyellow, &fwhite"));
        player.sendMessage("");
        player.sendMessage(colorString("&eValid modifiers:"));
        player.sendMessage(colorString("&ck, &c&ll&r, &c&mm&r, &c&nn&r, &c&oo"));
        player.sendMessage("");
        player.sendMessage(colorString("&eAlternatively:"));
        player.sendMessage(colorString("&cobfuscated, &c&lbold&r, &c&mstrikethrough&r, &c&nunderlined&r, &c&oitalic"));
    }

    public void handlePermHelp(Player player) {
        player.sendMessage(CCStrings.prefix + "Displaying permissions help!");
        player.sendMessage(colorString(" &7- &eMain Permission: &cchatcolor.use"));
        player.sendMessage(colorString(" &7- &eAll Perms: &cchatcolor.*"));
        player.sendMessage("");
        player.sendMessage(colorString("&eAdmin Permissions:"));
        player.sendMessage(colorString(" &7- &ePerms Help: &cchatcolor.admin.permhelp"));
        player.sendMessage(colorString(" &7- &eReload Config: &cchatcolor.admin.reload"));
        player.sendMessage(colorString(" &7- &eReset Config: &cchatcolor.admin.reset"));
        player.sendMessage(colorString(" &7- &eSet Settings: &cchatcolor.admin.set"));
        player.sendMessage(colorString(" &7- &eAll Admin Perms: &cchatcolor.admin.*"));
        player.sendMessage("");
        player.sendMessage(colorString("&eColor Permissions:"));
        player.sendMessage(colorString(" &7- Permission: &cchatcolor.color.<color>"));
        player.sendMessage(colorString(" &7- Example: &cchatcolor.color.a"));
        player.sendMessage(colorString("&eNote: &cchatcolor.color.rainbow &ecan be used, but no other words."));
        player.sendMessage("");
        player.sendMessage(colorString("&eModifier Permissions:"));
        player.sendMessage(colorString(" &7- Permission: &cchatcolor.modifier.<modifier>"));
        player.sendMessage(colorString(" &7- Example: &cchatcolor.modifier.k"));
        player.sendMessage(colorString("&eNote: No words may be used."));
        player.sendMessage("");
        player.sendMessage(colorString("&eOther Permissions:"));
        player.sendMessage(colorString(" &7- Change Own Color: &cchatcolor.change.self"));
        player.sendMessage(colorString(" &7- Change Other's Color: &cchatcolor.change.others"));
    }

    public void handleSet(String[] args, Player player) {
        switch(args[1]) {

            case "color-override": {
                boolean val = false;
                try {
                    val = Boolean.parseBoolean(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CCStrings.needbool);
                    return;
                }
                boolean override = MainClass.get().getConfig().getBoolean("settings.color-override");
                if (val == override) {
                    player.sendMessage(CCStrings.alreadyset);
                    return;
                }
                if (val) {
                    player.sendMessage(CCStrings.prefix + "§ccolor-override" + CCStrings.iscur + "§cFALSE");
                    player.sendMessage(CCStrings.tochng + "§aTRUE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "color-override", val);
                    return;
                } else {
                    player.sendMessage(CCStrings.prefix + "§ccolor-override" + CCStrings.iscur + "§aTRUE");
                    player.sendMessage(CCStrings.tochng + "§cFALSE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "color-override", val);
                }
            }

            case "notify-others": {
                boolean val = false;
                try {
                    val = Boolean.parseBoolean(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CCStrings.needbool);
                    return;
                }
                boolean notify = MainClass.get().getConfig().getBoolean("settings.notify-others");
                if (val == notify) {
                    player.sendMessage(CCStrings.alreadyset);
                    return;
                }
                if (val) {
                    player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + "§cFALSE");
                    player.sendMessage(CCStrings.tochng + "§aTRUE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "notify-others", val);
                    return;
                } else {
                    player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + "§aTRUE");
                    player.sendMessage(CCStrings.tochng + "§cFALSE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "notify-others", val);
                    return;
                }
            }

            case "join-message": {
                boolean val = false;
                try {
                    val = Boolean.parseBoolean(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CCStrings.needbool);
                    return;
                }
                boolean notify = MainClass.get().getConfig().getBoolean("settings.join-message");
                if (val == notify) {
                    player.sendMessage(CCStrings.alreadyset);
                    return;
                }
                if (val) {
                    player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + "§cFALSE");
                    player.sendMessage(CCStrings.tochng + "§aTRUE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "join-message", val);
                    return;
                } else {
                    player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + "§aTRUE");
                    player.sendMessage(CCStrings.tochng + "§cFALSE");
                    player.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(player, cs);
                    cs.confirm(player, "join-message", val);
                    return;
                }
            }

            case "confirm-timeout": {
                int val = 0;
                try {
                    val = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CCStrings.needint);
                    return;
                }
                player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + "§c" + MainClass.get().getConfig().getString("settings.confirm-timeout") + " seconds");
                player.sendMessage(CCStrings.tochng + "§c" + val);
                player.sendMessage(CCStrings.confirm);
                ConfirmScheduler cs = new ConfirmScheduler();
                MainClass.get().addConfirmee(player, cs);
                cs.confirm(player, "confirm-timeout", val);
            }

            case "default-color": {
                String color = getColor(args[2]);
                if (color == null) {
                    player.sendMessage(CCStrings.invcol);
                    return;
                }
                player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + MainClass.get().getConfig().getString("settings.default-color").replace("&", "§") + CCStrings.colthis);
                player.sendMessage(CCStrings.tochng + color);
                player.sendMessage(CCStrings.confirm);
                ConfirmScheduler cs = new ConfirmScheduler();
                MainClass.get().addConfirmee(player, cs);
                cs.confirm(player, "default-color", color);
                return;
            }

            case "rainbow-sequence": {
                String seq = args[2];
                if (!verifyRainbowSequence(seq, false)) {
                    player.sendMessage(CCStrings.prefix + "§e" + args[2] + " §cis an invalid color sequence!");
                    player.sendMessage(CCStrings.help);
                    return;
                }
                String[] ss = MainClass.get().getConfig().getString("settings.rainbow-sequence").split("");
                StringBuilder sb = new StringBuilder();
                for (String st : ss) {
                    sb.append("§" + st + st);
                }
                String rc = sb.toString();
                String[] sqs = args[2].split("");
                StringBuilder sq = new StringBuilder();
                for (String sts : sqs) {
                    sq.append("§" + sts + sts);
                }
                String rcs = sq.toString();
                player.sendMessage(CCStrings.prefix + "§c" + args[1] + CCStrings.iscur + rc);
                player.sendMessage(CCStrings.tochng + rcs);
                player.sendMessage(CCStrings.confirm);
                ConfirmScheduler cs = new ConfirmScheduler();
                MainClass.get().addConfirmee(player, cs);
                cs.confirm(player, "rainbow-sequence", seq);
                return;
            }

            case "command-name": {
                String cmd = args[2];
                for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                    if (p.getDescription().getCommands() != null) {
                        if (p.getDescription().getCommands().containsKey(cmd)) {
                            player.sendMessage(CCStrings.cmdexst);
                            return;
                        }
                    }
                }
                player.sendMessage(CCStrings.prefix + colorString("&c" + args[1]) + CCStrings.iscur + colorString("&c/") + MainClass.get().getConfig().getString("settings.command-name"));
                player.sendMessage(CCStrings.tochng + colorString("&c/" + cmd));
                player.sendMessage(CCStrings.confirm);
                ConfirmScheduler cs = new ConfirmScheduler();
                MainClass.get().addConfirmee(player, cs);
                cs.confirm(player, "command-name", cmd);
            }

        }
    }

    public String colorString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public boolean verifyRainbowSequence(String seq, boolean replace) {

        boolean verify = true;
        List<String> cols = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        String[] chars = seq.split("");
        for (String s : chars) {
            if (!cols.contains(s)) {
                verify = false;
            }
        }
        if (replace && !verify) {
            MainClass.get().getConfig().set("rainbow-sequence", "abcde");
            MainClass.get().saveConfig();
        }
        return verify;
    }

    public String getColor(String str) {
        String s = str.toLowerCase();
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
                    return "&" + i.toString();
                }
            }
            if (s.equalsIgnoreCase("light.green")) {
                return "&a";
            }
            else if (s.equalsIgnoreCase("aqua")) {
                return "&b";
            }
            else if (s.equalsIgnoreCase("light.red")) {
                return "&c";
            }
            else if (s.equalsIgnoreCase("magenta")) {
                return "&d";
            }
            else if (s.equalsIgnoreCase("yellow")) {
                return "&e";
            }
            else if (s.equalsIgnoreCase("white")) {
                return "&f";
            }
        }
        List<String> other = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        if (other.contains(s)) {
            return "&" + s;
        }
        else {
            return null;
        }
    }

    public String getModifier(String str) {
        String s = str.toLowerCase();
        if (s.equalsIgnoreCase("obfuscated")) {
            return "&k";
        }
        else if (s.equalsIgnoreCase("bold")) {
            return "&l";
        }
        else if (s.equalsIgnoreCase("strikethrough")) {
            return "&m";
        }
        else if (s.equalsIgnoreCase("underlined")) {
            return "&n";
        }
        else if (s.equalsIgnoreCase("italic")) {
            return "&o";
        }
        else {
            List<String> other = Arrays.asList("k", "l", "m", "n", "o");
            if (other.contains(s)) {
                return "&" + s;
            }
            return null;
        }
    }

}
