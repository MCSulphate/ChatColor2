package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.listeners.ColorGUIListener;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.managers.ConfirmationsManager;
import com.sulphate.chatcolor2.schedulers.ConfirmScheduler;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ChatColorCommand implements CommandExecutor {

    private Messages M;
    private ConfigUtils configUtils;
    private ConfirmationsManager confirmationsManager;
    private ConfigsManager configsManager;

    public ChatColorCommand(Messages M, ConfigUtils configUtils, ConfirmationsManager confirmationsManager, ConfigsManager configsManager) {
        this.M = M;
        this.configUtils = configUtils;
        this.confirmationsManager = confirmationsManager;
        this.configsManager = configsManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        int argsno = args.length;

        if (sender instanceof Player) {

            Player s = (Player) sender;
            // Run the command check.
            // This checks: permissions, valid colours, errors, confirming restrictions, and anything else I missed.
            if (!checkCommand(args, s)) {
                return true;
            }

            List<String> cmds = Arrays.asList("help", "commandshelp", "permissionshelp", "settingshelp", "set", "reset", "reload", "available", "gui");
            if (cmds.contains(args[0].toLowerCase())) {
                switch (args[0].toLowerCase()) {
                    case "help":
                    case "commandshelp": {
                        handleCommandsHelp(s);
                        return true;
                    }

                    case "permissionshelp": {
                        handlePermissionsHelp(s);
                        return true;
                    }

                    case "settingshelp": {
                        handleSettingsHelp(s);
                        return true;
                    }

                    case "set": {
                        handleSet(args, s);
                        return true;
                    }

                    case "reset": {
                        s.sendMessage(M.PREFIX + M.CONFIRM);
                        ChatColor.getPlugin().createConfirmScheduler(s, "reset", null);
                        return true;
                    }

                    case "reload": {
                        configsManager.loadAllConfigs();
                        M.reloadMessages();
                        ColorGUIListener.reloadGUI(M, configUtils); // Reload the GUIs as well, to give up-to-date Strings.

                        s.sendMessage(M.PREFIX + M.RELOADED_MESSAGES);
                        return true;
                    }

                    case "available": {
                        String comma = "§7, ";
                        char[] cols = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
                        char[] mods = {'k', 'l', 'm', 'n', 'o'};

                        String colstring;
                        String modstring;

                        StringBuilder colbuilder = new StringBuilder();
                        for (int i = 0; i < cols.length; i++) {
                            if (s.isOp() || s.hasPermission("chatcolor.color." + cols[i])) {
                                colbuilder.append(org.bukkit.ChatColor.COLOR_CHAR).append(cols[i]).append(cols[i]);

                                if (i != cols.length - 1) {
                                    colbuilder.append(comma);
                                }
                            }
                        }
                        colstring = colbuilder.toString();

                        StringBuilder modbuilder = new StringBuilder();
                        for (int i = 0; i < mods.length; i++) {
                            if (s.isOp() || s.hasPermission("chatcolor.modifier." + mods[i])) {
                                modbuilder.append(org.bukkit.ChatColor.COLOR_CHAR).append("b").append(org.bukkit.ChatColor.COLOR_CHAR).append(mods[i]).append(mods[i]);

                                if (i != mods.length - 1) {
                                    modbuilder.append(comma);
                                }
                            }
                        }
                        modstring = modbuilder.toString();

                        s.sendMessage(M.PREFIX + M.AVAILABLE_COLORS);
                        s.sendMessage(GeneralUtils.colourise(" &7- &e" + M.PREFIX + M.COLORS + ": " + colstring));
                        s.sendMessage(GeneralUtils.colourise(" &7- &e" + M.PREFIX + M.MODIFIERS + ": " + modstring));
                        return true;
                    }

                    case "gui": {
                        ColorGUIListener.openGUI(s, M, configUtils);
                        return true;
                    }
                }
            }

            // Check if they are setting another player's name.
            UUID uuid = configUtils.getUUIDFromName(args[0]);
            if (uuid != null) {
                // If they are offline, we must load their config first.
                if (configsManager.getPlayerConfig(uuid) == null) {
                    configsManager.loadPlayerConfig(uuid);
                }

                String result = setColorFromArgs(uuid, Arrays.copyOfRange(args, 1, args.length), configUtils);

                // Notify the player, if necessary.
                if ((boolean) configUtils.getSetting("notify-others") && Bukkit.getPlayer(uuid) != null) {
                    Bukkit.getPlayer(args[0]).sendMessage(M.PREFIX + M.PLAYER_SET_YOUR_COLOR.replace("[player]", s.getName()) + GeneralUtils.colouriseMessage(result, M.THIS, false, configUtils));
                }

                s.sendMessage(M.PREFIX + M.SET_OTHERS_COLOR.replace("[player]", uuid.toString()) + GeneralUtils.colouriseMessage(result, M.THIS, false, configUtils));
            }
            // Otherwise, set their colour.
            else {
                String result = setColorFromArgs(s.getUniqueId(), args, configUtils);
                s.sendMessage(M.PREFIX + M.SET_OWN_COLOR + GeneralUtils.colouriseMessage(result, M.THIS, false, configUtils));
            }

            return true;
        }
        // Sent from console - no need for checking permissions.
        // Settings changes are not supported by the console, only from in-game.
        else {
            if (argsno < 2) {
                sender.sendMessage(M.PREFIX + M.NOT_ENOUGH_ARGS);
                return true;
            }

            if (argsno > 6) {
                sender.sendMessage(M.PREFIX + M.TOO_MANY_ARGS);
                return true;
            }

            UUID uuid = configUtils.getUUIDFromName(args[0]);
            if (uuid != null) {
                // If their config hasn't been loaded, we must load it.
                if (configsManager.getPlayerConfig(uuid) == null) {
                    configsManager.loadPlayerConfig(uuid);
                }

                String result = setColorFromArgs(uuid, Arrays.copyOfRange(args, 1, args.length), configUtils);

                if ((boolean) configUtils.getSetting("notify-others") && Bukkit.getPlayer(args[0]) != null) {
                    Bukkit.getPlayer(uuid).sendMessage(M.PREFIX + M.PLAYER_SET_YOUR_COLOR.replace("[player]", "CONSOLE") + GeneralUtils.colouriseMessage(result, M.THIS, false, configUtils));
                }
                sender.sendMessage(M.PREFIX + M.SET_OTHERS_COLOR.replace("[player]", args[0]) + GeneralUtils.colouriseMessage(result, M.THIS, false, configUtils));
            } else {
                sender.sendMessage(M.PREFIX + M.PLAYER_NOT_JOINED);
            }
        }

        return true;
    }

    public static String setColorFromArgs(UUID uuid, String[] args, ConfigUtils configUtils) {
        StringBuilder sb = new StringBuilder();
        String color;

        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i].length() > 0) {
                if (i == 0) {
                    sb.append(getColour(args[i]));
                    continue;
                }
                sb.append(getModifier(args[i]));
            }
        }
        color = sb.toString();

        configUtils.setColour(uuid, color);
        return color;
    }

    // Checks the command given, including any permissions / invalid commands.
    private boolean checkCommand(String[] args, Player player) {
        UUID uuid = player.getUniqueId();

        if (args.length == 0) {
            String colour = configUtils.getColour(uuid);

            if (colour.contains("rainbow")) {
                String thisMessage = GeneralUtils.colouriseMessage(colour, M.THIS, false, configUtils);
                player.sendMessage(M.PREFIX + M.CURRENT_COLOR + thisMessage);
                return false;
            }

            player.sendMessage(M.PREFIX + M.CURRENT_COLOR + GeneralUtils.colouriseMessage(colour, M.THIS, false, configUtils));
            return false;
        }

        if (!player.isOp() && !player.hasPermission("chatcolor.use")) {
            player.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
            return false;
        }

        // args is at least 1 in length.
        List<String> cmds = Arrays.asList("set", "reload", "reset", "help", "permissionshelp", "commandshelp", "settingshelp", "available");
        if (cmds.contains(args[0])) {
            if (args[0].equalsIgnoreCase("set") && args.length < 3) {
                player.sendMessage(M.PREFIX + M.NOT_ENOUGH_ARGS);
                return false;
            }

            List<String> settings = Arrays.asList("auto-save", "save-interval", "color-override", "notify-others", "join-message", "confirm-timeout", "default-color", "rainbow-sequence", "command-name");

            if (args[0].equalsIgnoreCase("set") && !settings.contains(args[1])) {
                player.sendMessage(M.PREFIX + M.INVALID_SETTING + args[1]);
                return false;
            }

            if ((args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("reset")) && confirmationsManager.isConfirming(player)) {
                player.sendMessage(M.PREFIX + M.ALREADY_CONFIRMING);
                return false;
            }

            if (!player.isOp() && !player.hasPermission("chatcolor.admin." + args[0]) && !(args[0].equals("commandshelp") || args[0].equals("available"))) {
                player.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
                return false;
            }

            return true;
        }

        // Check if they want to use the GUI, and if they can.
        if (args[0].equals("gui")) {
            if (player.isOp() || player.hasPermission("chatcolor.gui")) {
                return true;
            }
            else {
                player.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
                return false;
            }
        }

        UUID targetUUID = configUtils.getUUIDFromName(args[0]);
        if (targetUUID != null) {
            if (!player.isOp() && !player.hasPermission("chatcolor.change.others")) {
                player.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
                return false;
            }

            if (args.length > 7) {
                player.sendMessage(M.PREFIX + M.TOO_MANY_ARGS);
                return false;
            }

            if (args.length < 2) {
                player.sendMessage(M.PREFIX + M.NOT_ENOUGH_ARGS);
                return false;
            }

            String colour = getColour(args[1]);
            if (colour != null) {
                for (int i = 1; i < args.length; i++) {
                    if (i == 1) {
                        if (!player.isOp() && !player.hasPermission("chatcolor.color." + args[1])) {
                            player.sendMessage(M.PREFIX + M.NO_COLOR_PERMS + GeneralUtils.colouriseMessage(colour, args[1], false, configUtils));
                            return false;
                        }

                        continue;
                    }

                    String mod = getModifier(args[i]);
                    if (getModifier(args[i]) == null) {
                        player.sendMessage(M.PREFIX + M.INVALID_MODIFIER + args[i]);
                        return false;
                    }

                    if (!player.isOp() && !player.hasPermission("chatcolor.modifier." + args[i])) {
                        player.sendMessage(M.PREFIX + M.NO_MOD_PERMS + GeneralUtils.colouriseMessage(mod, args[i], false, configUtils));
                        return false;
                    }
                }
                return true;
            }
            if (args[1].length() == 1) {
                player.sendMessage(M.PREFIX + M.INVALID_COLOR + args[1]);
            }
            player.sendMessage(M.PREFIX + M.INVALID_COMMAND);
            return false;
        }

        String colour = getColour(args[0]);
        if (colour != null) {
            if (!player.isOp() && !player.hasPermission("chatcolor.change.self")) {
                player.sendMessage(M.PREFIX + M.NO_PERMISSIONS);
                return false;
            }

            if (args.length > 6) {
                player.sendMessage(M.PREFIX + M.TOO_MANY_ARGS);
                return false;
            }

            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    if (!player.isOp() && !player.hasPermission("chatcolor.color." + args[0])) {
                        player.sendMessage(M.PREFIX + M.NO_COLOR_PERMS + GeneralUtils.colouriseMessage(colour, args[0], false, configUtils));
                        return false;
                    }

                    continue;
                }

                String mod = getModifier(args[i]);
                if (mod == null) {
                    player.sendMessage(M.PREFIX + M.INVALID_MODIFIER + args[i]);
                    return false;
                }

                if (!player.isOp() && !player.hasPermission("chatcolor.modifier." + args[i])) {
                    player.sendMessage(M.PREFIX + M.NO_MOD_PERMS + GeneralUtils.colouriseMessage(mod, args[i], false, configUtils));
                    return false;
                }
            }
            return true;
        }

        if (args[0].length() == 1) {
            player.sendMessage(M.PREFIX + M.INVALID_COLOR.replace("[color]", args[1]));
            return false;
        }

        player.sendMessage(M.PREFIX + M.INVALID_COLOR);
        return false;

    }

    //This is how the help command will be handled.
    // TODO: Make these all configurable messages when I regain the will to live.
    private void handleCommandsHelp(Player player) {
        player.sendMessage(M.PREFIX + "Displaying command help!");
        player.sendMessage(GeneralUtils.colourise(" &7- &eMain Command: &c/chatcolor <color> [modifiers]"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &eOther Commands:"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eCommands Help: &c/chatcolor commandshelp"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eSee Available Colors: &c/chatcolor available"));

        if (player.isOp() || player.hasPermission("chatcolor.admin.permissionshelp")) {
            player.sendMessage(GeneralUtils.colourise(" &7- &ePermissions Help: &c/chatcolor permissionshelp"));
        }

        if (player.isOp() || player.hasPermission("chatcolor.admin.settingshelp")) {
            player.sendMessage(GeneralUtils.colourise(" &7- &eSettings Help: &c/chatcolor settingshelp"));
        }

        if (player.isOp() || player.hasPermission("chatcolor.admin.reload")) {
            player.sendMessage(GeneralUtils.colourise(" &7- &eReload Configs: &c/chatcolor reload"));
        }

        if (player.isOp() || player.hasPermission("chatcolor.admin.reset")) {
            player.sendMessage(GeneralUtils.colourise(" &7- &eReset Config: &c/chatcolor reset"));
        }

        if (player.isOp() || player.hasPermission("chatcolor.admin.set")) {
            player.sendMessage(GeneralUtils.colourise(" &7- &eSet Settings: &c/chatcolor set <setting> <value>"));
        }

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eValid Colors:"));
        player.sendMessage(GeneralUtils.colourise("&00&11&22&33&44&55&66&77&88&99"));
        player.sendMessage(GeneralUtils.colourise("&aa&bb&cc&dd&ee&ff"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eAlternatively:"));
        player.sendMessage(GeneralUtils.colourise("&0black, &1dark.blue, &2green, &3dark.aqua,"));
        player.sendMessage(GeneralUtils.colourise("&4red, &5purple, &6gold, &7grey, &8dark.grey, &9blue"));
        player.sendMessage(GeneralUtils.colourise("&alight.green, &baqua, &clight.red, &dmagenta, &eyellow, &fwhite"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eValid modifiers:"));
        player.sendMessage(GeneralUtils.colourise("&ck, &c&ll&r, &c&mm&r, &c&nn&r, &c&oo"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eAlternatively:"));
        player.sendMessage(GeneralUtils.colourise("&cobfuscated, &c&lbold&r, &c&mstrikethrough&r, &c&nunderlined&r, &c&oitalic"));

        // Send the author messages.
        player.sendMessage("");
        player.sendMessage(M.AUTHOR_MESSAGE_1);
        player.sendMessage(M.AUTHOR_MESSAGE_2);
    }

    private void handlePermissionsHelp(Player player) {
        player.sendMessage(M.PREFIX + "Displaying permissions help!");
        player.sendMessage(GeneralUtils.colourise(" &7- &eMain Permission: &cchatcolor.use"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eAll Perms: &cchatcolor.*"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eAdmin Permissions:"));
        player.sendMessage(GeneralUtils.colourise(" &7- &ePermissions Help: &cchatcolor.admin.permissionshelp"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eReload Messages: &cchatcolor.admin.reloadmessages"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eReset Config: &cchatcolor.admin.reset"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eSet Settings: &cchatcolor.admin.set"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eEnable Plugin: &cchatcolor.admin.enable"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eAll Admin Perms: &cchatcolor.admin.*"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eColor Permissions:"));
        player.sendMessage(GeneralUtils.colourise(" &7- &ePermission: &cchatcolor.color.<color>"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eExample: &cchatcolor.color.a"));
        player.sendMessage(GeneralUtils.colourise("&eNote: &cchatcolor.color.rainbow &ecan be used, but no other words."));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eModifier Permissions:"));
        player.sendMessage(GeneralUtils.colourise(" &7- &ePermission: &cchatcolor.modifier.<modifier>"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eExample: &cchatcolor.modifier.k"));
        player.sendMessage(GeneralUtils.colourise("&eNote: No words may be used."));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise("&eOther Permissions:"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eChange Own Color: &cchatcolor.change.self"));
        player.sendMessage(GeneralUtils.colourise(" &7- &eChange Other's Color: &cchatcolor.change.others"));

        // Send the author messages.
        player.sendMessage("");
        player.sendMessage(M.AUTHOR_MESSAGE_1);
        player.sendMessage(M.AUTHOR_MESSAGE_2);
    }

    private void handleSettingsHelp(Player player) {
        player.sendMessage(M.PREFIX + "Displaying settings help!");
        player.sendMessage(GeneralUtils.colourise(" &7- &eauto-save: &cAuto save data every 5 minutes."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set auto-save <true/false>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &esave-interval: &cSets the time between saves, in minutes."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set save-interval <time>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &ecolor-override: &cOverride '&' symbols in chat."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set color-override <true/false>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &econfirm-timeout: &cSet time for confirming settings."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set confirm-timeout <seconds>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &edefault-color: &cChange the default color."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set default-color <color> <modifiers..>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &ejoin-message: &cTell players their color on join."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set join-message <true/false>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &enotify-others: &cTell others if you change their color."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set notify-others <true/false>"));

        player.sendMessage("");
        player.sendMessage(GeneralUtils.colourise(" &7- &erainbow-sequence: &cChange the rainbow chatcolor sequence."));
        player.sendMessage(GeneralUtils.colourise("   &eUsage: &b/chatcolor set rainbow-sequence <sequence>"));

        // Send the author messages.
        player.sendMessage("");
        player.sendMessage(M.AUTHOR_MESSAGE_1);
        player.sendMessage(M.AUTHOR_MESSAGE_2);
    }

    private void handleSet(String[] args, Player player) {
        String trueVal = GeneralUtils.colourise("&aTRUE");
        String falseVal = GeneralUtils.colourise("&cFALSE");

        String setting = args[1];
        String currentValueString = "";
        String valueString = "";
        Object value = null;

        switch (args[1]) {

            case "auto-save": {
                boolean val;

                try {
                    val = Boolean.parseBoolean(args[2]);
                }
                catch (Exception e) {
                    player.sendMessage(M.PREFIX + M.NEEDS_BOOLEAN);
                    return;
                }

                boolean autosave = (boolean) configUtils.getSetting("auto-save");

                if (val == autosave) {
                    player.sendMessage(M.PREFIX + M.ALREADY_SET);
                    return;
                }

                currentValueString = val ? falseVal : trueVal;
                valueString = val ? trueVal : falseVal;
                value = val;
                break;
            }

            case "save-interval": {
                int val;

                try {
                    val = Integer.parseInt(args[2]);
                }
                catch (NumberFormatException ex) {
                    player.sendMessage(M.PREFIX + M.NEEDS_NUMBER);
                    return;
                }

                int saveInterval = (int) configUtils.getSetting("save-interval");

                if (val == saveInterval) {
                    player.sendMessage(M.PREFIX + M.ALREADY_SET);
                    return;
                }

                currentValueString = saveInterval + "";
                valueString = val + "";
                value = val;
            }

            case "color-override": {
                boolean val;

                try {
                    val = Boolean.parseBoolean(args[2]);
                }
                catch (Exception e) {
                    player.sendMessage(M.PREFIX + M.NEEDS_BOOLEAN);
                    return;
                }

                boolean override = (boolean) configUtils.getSetting("color-override");

                if (val == override) {
                    player.sendMessage(M.PREFIX + M.ALREADY_SET);
                    return;
                }

                currentValueString = val ? falseVal : trueVal;
                valueString = val ? trueVal : falseVal;
                value = val;
                break;
            }

            case "notify-others": {
                boolean val;

                try {
                    val = Boolean.parseBoolean(args[2]);
                }
                catch (Exception e) {
                    player.sendMessage(M.PREFIX + M.NEEDS_BOOLEAN);
                    return;
                }

                boolean notify = (boolean) configUtils.getSetting("notify-others");

                if (val == notify) {
                    player.sendMessage(M.PREFIX + M.ALREADY_SET);
                    return;
                }

                currentValueString = val ? falseVal : trueVal;
                valueString = val ? trueVal : falseVal;
                value = val;
                break;
            }

            case "join-message": {
                boolean val;

                try {
                    val = Boolean.parseBoolean(args[2]);
                }
                catch (Exception e) {
                    player.sendMessage(M.PREFIX + M.NEEDS_BOOLEAN);
                    return;
                }

                boolean notify = (boolean) configUtils.getSetting("join-message");

                if (val == notify) {
                    player.sendMessage(M.PREFIX + M.ALREADY_SET);
                    return;
                }

                currentValueString = val ? falseVal : trueVal;
                valueString = val ? trueVal : falseVal;
                value = val;
                break;
            }

            case "confirm-timeout": {
                int val;

                try {
                    val = Integer.parseInt(args[2]);
                }
                catch (Exception e) {
                    player.sendMessage(M.PREFIX + M.NEEDS_NUMBER);
                    return;
                }

                currentValueString = (int) configUtils.getSetting("confirm-timeout") + "";
                valueString = val + "";
                value = val;
                break;
            }

            case "default-color": {
                String colour = getColour(args[2]);

                if (colour == null) {
                    player.sendMessage(M.PREFIX + M.INVALID_COLOR);
                    return;
                }

                StringBuilder builder = new StringBuilder(colour);

                for (int i = 3; i < args.length; i++) {
                    String mod = getModifier(args[i]);

                    if (mod == null) {
                        player.sendMessage(M.PREFIX + M.INVALID_COLOR);
                        return;
                    }
                    else {
                        builder.append(mod);
                    }
                }

                String fullColour = builder.toString();

                currentValueString = (String) configUtils.getSetting("default-color");
                valueString = GeneralUtils.colouriseMessage(fullColour, M.THIS, false, configUtils);
                value = fullColour;
                break;
            }

            case "rainbow-sequence": {
                String seq = args[2];

                if (!GeneralUtils.verifyRainbowSequence(seq, configUtils)) {
                    player.sendMessage(GeneralUtils.colourise(M.PREFIX + M.INVALID_SEQUENCE.replace("[sequence]", seq)));
                    player.sendMessage(M.PREFIX + M.HELP);
                    return;
                }

                String currentSequence = (String) configUtils.getSetting("rainbow-sequence");
                char[] newSequence = args[2].toCharArray();

                // Only need to manually colour the new one.
                StringBuilder builder = new StringBuilder();

                for (char c : newSequence) {
                    builder.append("&").append(c).append(c);
                }

                String rainbowText = GeneralUtils.colourise(builder.toString());

                currentValueString = GeneralUtils.colouriseMessage("rainbow", currentSequence, false, configUtils);
                valueString = rainbowText;
                value = newSequence;
                break;
            }

            case "command-name": {
                String cmd = args[2];

                // Make sure no other plugin has this command set.
                for (Plugin p : Bukkit.getPluginManager().getPlugins()) {
                    if (p.getDescription().getCommands() != null) {

                        if (p.getDescription().getCommands().containsKey(cmd)) {
                            player.sendMessage(M.PREFIX + M.COMMAND_EXISTS);
                            return;
                        }

                    }
                }

                currentValueString = (String) configUtils.getSetting("command-name");
                value = valueString = cmd;
                break;
            }
        }

        player.sendMessage(M.PREFIX + M.IS_CURRENTLY.replace("[setting]", setting).replace("[value]", currentValueString));
        player.sendMessage(M.PREFIX + M.TO_CHANGE.replace("[value]", valueString));
        player.sendMessage(M.PREFIX + M.CONFIRM);

        ChatColor.getPlugin().createConfirmScheduler(player, setting, value);
    }

    public static String getColour(String str) {
        String s = str.toLowerCase();

        if (s.equalsIgnoreCase("rainbow")) {
            return s;
        }

        List<String> words = Arrays.asList("black", "dark.blue", "green", "dark.aqua", "red", "purple", "gold", "gray", "dark.grey", "blue", "light.green", "aqua", "light.red", "magenta", "yellow", "white");

        if (words.contains(s)) {

            for (int i = 0; i < words.size(); i++) {
                if (i == 10) {
                    break;
                }

                String st = words.get(i);

                if (s.equalsIgnoreCase(st)) {
                    return "&" + i;
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

    private static String getModifier(String str) {
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
