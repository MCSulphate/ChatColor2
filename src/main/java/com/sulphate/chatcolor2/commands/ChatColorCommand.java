package com.sulphate.chatcolor2.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.main.MainClass;
import com.sulphate.chatcolor2.utils.CCStrings;
import com.sulphate.chatcolor2.utils.ColorUtils;

public class ChatColorCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(CCStrings.notplayer);
            return true;
        }

        Player s = (Player) sender;

        if (!s.hasPermission("chatcolor.use") && (!s.hasPermission("chatcolor.*"))) {
            s.sendMessage(CCStrings.noperms);
            return true;
        }

        int argsno = args.length;

        switch(argsno) {

            case 0: {
                String color = ColorUtils.getColor(s.getName());
                if (color.contains("rainbow")) {
                    String mods = color.replace("rainbow", "");
                    String rseq = MainClass.get().getConfig().getString("settings.rainbow-sequence");
                    if (!verifyRainbowSequence(rseq)) {
                        MainClass.get().getConfig().set("rainbow-sequence", "abcde");
                        MainClass.get().saveConfig();
                    }
                    String rs = MainClass.get().getConfig().getString("settings.rainbow-sequence");
                    String[] rss = rs.split("");
                    StringBuilder sb = new StringBuilder();
                    for (String st : rss) {
                        sb.append("§" + st + st);
                    }
                    String end = sb.toString();
                    s.sendMessage(CCStrings.yourcol + end);
                    return true;
                } else {
                    s.sendMessage(CCStrings.yourcol + ColorUtils.getColor(s.getName()) + CCStrings.colthis);
                }
                return true;
            }

            case 1: {

                if (args[0].equalsIgnoreCase("help")) {
                    s.sendMessage(CCStrings.prefix + "Help for ChatColor 2:");
                    s.sendMessage(" §7- §eMain command: §c/chatcolor <color> [modifiers]");
                    s.sendMessage("");
                    s.sendMessage("§eOther commands:");
                    if (s.hasPermission("chatcolor.gui") || s.hasPermission("chatcolor.*")) {
                        s.sendMessage(" §7- §eSelector GUI: §c/chatcolor gui");
                    }
                    if (s.hasPermission("chatcolor.*") || s.hasPermission("chatcolor.admin.*") || s.hasPermission("chatcolor.admin.reload")) {
                        s.sendMessage(" §7- §eReload config: §c/chatcolor reload");
                    }
                    if (s.hasPermission("chatcolor.*") || s.hasPermission("chatcolor.admin.*") || s.hasPermission("chatcolor.admin.reset")) {
                        s.sendMessage(" §7- §eReset config: §c/chatcolor reset");
                    }
                    if (s.hasPermission("chatcolor.*") || s.hasPermission("chatcolor.admin.*") || s.hasPermission("chatcolor.admin.set")) {
                        s.sendMessage(" §7- §eChange settings: §c/chatcolor set <setting> <value>");
                        s.sendMessage("§eValid settings: §bcolor-override, notify-others, join-message, confirm-timeout, default-color, rainbow-sequence");
                    }
                    s.sendMessage(" §7- §eHelp command: §c/chatcolor help");
                    s.sendMessage("");
                    s.sendMessage("§eValid colors are as follows:");
                    s.sendMessage("§00, §11, §22, §33, §44, §55, §66, §77, §88, §99");
                    s.sendMessage("§aa, §bb, §cc, §dd, §ee, §ff");
                    s.sendMessage("§eThese can be used as well:");
                    s.sendMessage("§0black, §1dark.blue, §2green, §3dark.aqua, §4red, §5purple, §6gold, §7gray, §8dark.grey, §9blue");
                    s.sendMessage("§alight.green, §baqua, §clight.red, §dmagenta, §eyellow, §fwhite");
                    s.sendMessage("");
                    s.sendMessage("§eValid modifiers:");
                    s.sendMessage("§e§kk§r§ek, §ll§e, §mm§e, §nn§e, §oo");
                    s.sendMessage("§eThese can be used as well:");
                    s.sendMessage("§eobfuscated, §lbold§e, §munderlined§e, §nstrikethrough§e, §oitalic");
                    return true;
                } else if (args[0].equalsIgnoreCase("reset")) {
                    if (!s.hasPermission("chatcolor.admin.reset") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                        s.sendMessage(CCStrings.noperms);
                        return true;
                    }
                    s.sendMessage(CCStrings.confirm);
                    ConfirmScheduler cs = new ConfirmScheduler();
                    MainClass.get().addConfirmee(s, cs);
                    cs.confirm(s, "reset", null);
                    return true;
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (!s.hasPermission("chatcolor.admin.reload") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                        s.sendMessage(CCStrings.noperms);
                        return true;
                    }
                    MainClass.get().reloadConfig();
                    s.sendMessage(CCStrings.relconfig);
                    return true;
                } else if (args[0].equalsIgnoreCase("set")) {
                    if (!s.hasPermission("chatcolor.*") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.admin.set")) {
                        s.sendMessage(CCStrings.noperms);
                        return true;
                    }
                    s.sendMessage(CCStrings.invset);
                    s.sendMessage("§eValid settings: §bcolor-override, notify-others, confirm-timeout, default-color, rainbow-sequence");
                    return true;
                }

                if (args[0].length() > 1 && !args[0].equalsIgnoreCase("rainbow")) {
                    s.sendMessage(CCStrings.invcom);
                    return true;
                }

                if (!s.hasPermission("chatcolor.*") && !s.hasPermission("chatcolor.change.*") && !s.hasPermission("chatcolor.change.self")) {
                    s.sendMessage(CCStrings.noperms);
                    return true;
                }

                String color = getColor(args[0]);
                if (color == null) {
                    s.sendMessage(CCStrings.invcol);
                    return true;
                }
                if (!checkPermissions(Arrays.asList(color), s)) {
                    s.sendMessage(CCStrings.nocolperm);
                    return true;
                } else {
                    ColorUtils.setColor(s.getName(), color);
                    if (color.contains("rainbow")) {
                        s.sendMessage(CCStrings.setownc + "§ar§ba§ci§dn§eb§ao§bw§e!");
                        return true;
                    }
                    s.sendMessage(CCStrings.setownc + color + CCStrings.colthis);
                    return true;
                }

            }

            case 2: {

                if (args[0].length() > 1 && args[1].length() > 1) {
                    s.sendMessage(CCStrings.invcom);
                    return true;
                }

                if (args[0].length() > 1 && args[1].length() == 1 && !args[0].equalsIgnoreCase("rainbow")) {
                    if (!s.hasPermission("chatcolor.*") && !s.hasPermission("chatcolor.change.*") && !s.hasPermission("chatcolor.change.others")) {
                        s.sendMessage(CCStrings.noperms);
                        return true;
                    }
                    if (Bukkit.getPlayer(args[0]) == null) {
                        s.sendMessage(CCStrings.notonline);
                        return true;
                    }
                    Player t = Bukkit.getPlayer(args[0]);
                    String color = getColor(args[1]);
                    if (color == null) {
                        s.sendMessage(CCStrings.invcol);
                        return true;
                    }
                    if (!checkPermissions(Arrays.asList(color), s)) {
                        s.sendMessage(CCStrings.nocolperm);
                        return true;
                    }
                    ColorUtils.setColor(t.getName(), color);
                    s.sendMessage(CCStrings.setothc.replace("[player]", t.getName()) + color + CCStrings.colthis);
                    if (MainClass.get().getConfig().getBoolean("settings.notify-others")) {
                        t.sendMessage(CCStrings.setyourc.replace("[player]", s.getName()) + color + CCStrings.colthis);
                    }
                    return true;
                }

                if (!s.hasPermission("chatcolor.change.self") && !s.hasPermission("chatcolor.change.*") && !s.hasPermission("chatcolor.*")) {
                    s.sendMessage(CCStrings.noperms);
                    return true;
                }

                String color = getColor(args[0]);
                String modifier = getModifier(args[1]);
                if (color == null) {
                    s.sendMessage(CCStrings.invcol);
                    return true;
                } else if (modifier == null) {
                    s.sendMessage(CCStrings.invmod);
                    return true;
                }
                if (!checkPermissions(Arrays.asList(color, modifier), s)) {
                    s.sendMessage(CCStrings.nocmperm);
                    return true;
                } else {
                    if (color.equals("rainbow")) {
                        String ths = CCStrings.colthis;
                        String[] thss = ths.split("");
                        StringBuilder sb = new StringBuilder();
                        List<String> colors = Arrays.asList("§a", "§b", "§c", "§d", "§e");
                        int cn = 0;
                        for (int i = 0; i < thss.length; i++) {
                            if (cn == 5) {
                                cn = 0;
                            }
                            String col = colors.get(cn);
                            String message = col + modifier + thss[i];
                            sb.append(message);
                            cn++;
                        }
                        String end = sb.toString();
                        s.sendMessage(CCStrings.setownc + end);
                        return true;
                    }
                    String full = color + modifier;
                    ColorUtils.setColor(s.getName(), full);
                    s.sendMessage(CCStrings.setownc + full + CCStrings.colthis);
                    return true;
                }

            }

            case 3: {

                if (args[0].equalsIgnoreCase("set")) {

                    if (!s.hasPermission("chatcolor.admin.set") && !s.hasPermission("chatcolor.admin.*") && !s.hasPermission("chatcolor.*")) {
                        s.sendMessage(CCStrings.noperms);
                        return true;
                    }

                    switch(args[1]) {

                        case "color-override": {
                            boolean val = false;
                            try {
                                val = Boolean.parseBoolean(args[2]);
                            } catch (Exception e) {
                                s.sendMessage(CCStrings.needbool);
                                return true;
                            }
                            if (MainClass.get().getConfirmees().containsKey(s)) {
                                s.sendMessage(CCStrings.alreadycon);
                                return true;
                            }
                            boolean override = MainClass.get().getConfig().getBoolean("settings.color-override");
                            if (val == override) {
                                s.sendMessage(CCStrings.alreadyset);
                                return true;
                            }
                            if (val) {
                                s.sendMessage(CCStrings.prefix + "§ccolor-override §eis currently §cFALSE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "color-override", val);
                                return true;
                            } else {
                                s.sendMessage(CCStrings.prefix + "§ccolor-override §eis currently §aTRUE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "color-override", val);
                            }
                        }

                        case "notify-others": {
                            boolean val = false;
                            try {
                                val = Boolean.parseBoolean(args[2]);
                            } catch (Exception e) {
                                s.sendMessage(CCStrings.needbool);
                                return true;
                            }
                            if (MainClass.get().getConfirmees().containsKey(s)) {
                                s.sendMessage(CCStrings.alreadycon);
                                return true;
                            }
                            boolean notify = MainClass.get().getConfig().getBoolean("settings.notify-others");
                            if (val == notify) {
                                s.sendMessage(CCStrings.alreadyset);
                                return true;
                            }
                            if (val) {
                                s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently §cFALSE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "notify-others", val);
                                return true;
                            } else {
                                s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently §aTRUE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "notify-others", val);
                                return true;
                            }
                        }

                        case "join-message": {
                            boolean val = false;
                            try {
                                val = Boolean.parseBoolean(args[2]);
                            } catch (Exception e) {
                                s.sendMessage(CCStrings.needbool);
                                return true;
                            }
                            if (MainClass.get().getConfirmees().containsKey(s)) {
                                s.sendMessage(CCStrings.alreadycon);
                                return true;
                            }
                            boolean notify = MainClass.get().getConfig().getBoolean("settings.join-message");
                            if (val == notify) {
                                s.sendMessage(CCStrings.alreadyset);
                                return true;
                            }
                            if (val) {
                                s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently §cFALSE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "join-message", val);
                                return true;
                            } else {
                                s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently §aTRUE");
                                s.sendMessage(CCStrings.confirm);
                                ConfirmScheduler cs = new ConfirmScheduler();
                                MainClass.get().addConfirmee(s, cs);
                                cs.confirm(s, "join-message", val);
                                return true;
                            }
                        }

                        case "confirm-timeout": {
                            int val = 0;
                            try {
                                val = Integer.parseInt(args[2]);
                            } catch (Exception e) {
                                s.sendMessage(CCStrings.needint);
                                return true;
                            }
                            s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently §c" + MainClass.get().getConfig().getString("settings.confirm-timeout") + " seconds");
                            s.sendMessage(CCStrings.confirm);
                            ConfirmScheduler cs = new ConfirmScheduler();
                            MainClass.get().addConfirmee(s, cs);
                            cs.confirm(s, "confirm-timeout", val);
                        }

                        case "default-color": {
                            String color = getColor(args[2]);
                            if (color == null) {
                                s.sendMessage(CCStrings.invcol);
                                return true;
                            }
                            s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently " + MainClass.get().getConfig().getString("settings.default-color") + "this");
                            s.sendMessage(CCStrings.confirm);
                            ConfirmScheduler cs = new ConfirmScheduler();
                            MainClass.get().addConfirmee(s, cs);
                            cs.confirm(s, "default-color", color);
                            return true;
                        }

                        case "rainbow-sequence": {
                            String seq = args[2];
                            if (!verifyRainbowSequence(seq)) {
                                s.sendMessage(CCStrings.prefix + "§e" + args[2] + " §cis an invalid color sequence!");
                                s.sendMessage(CCStrings.help);
                                return true;
                            }
                            String[] ss = MainClass.get().getConfig().getString("settings.rainbow-sequence").split("");
                            StringBuilder sb = new StringBuilder();
                            for (String st : ss) {
                                sb.append("§" + st + st);
                            }
                            String rc = sb.toString();
                            s.sendMessage(CCStrings.prefix + "§c" + args[1] + " §eis currently " + rc);
                            s.sendMessage(CCStrings.confirm);
                            ConfirmScheduler cs = new ConfirmScheduler();
                            MainClass.get().addConfirmee(s, cs);
                            cs.confirm(s, "rainbow-sequence", seq);
                            return true;
                        }

                    }

                }

            }

        }

        s.sendMessage(CCStrings.invcom);
        return true;

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

    public boolean checkPermissions(List<String> args, Player player) {
        if (player.hasPermission("chatcolor.*")) {
            return true;
        }
        for (String s : args) {
            if (s.equalsIgnoreCase("rainbow")) {
                if (player.hasPermission("chatcolor.color.*")) {
                    return true;
                }
                if (!player.hasPermission("chatcolor.color.rainbow")) {
                    return false;
                }
            }
            List<String> colors = Arrays.asList("0", "1", "2", "3", "3", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
            List<String> modifiers = Arrays.asList("k", "l", "m", "n", "o");
            String lc = s.toLowerCase();
            if (colors.contains(lc)) {
                if (player.hasPermission("chatcolor.color.*")) {
                    return true;
                }
                String full = "chatcolor.color." + s;
                if (!player.hasPermission(full)) {
                    return false;
                }
            }
            else if (modifiers.contains(lc)) {
                if (player.hasPermission("chatcolor.modifier.*")) {
                    return true;
                }
                String full = "chatcolor.modifier." + s;
                if (!player.hasPermission(full)) {
                    return false;
                }
            }
        }
        return true;
    }

}
