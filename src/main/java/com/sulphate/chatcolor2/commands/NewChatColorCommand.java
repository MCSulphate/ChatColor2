package com.sulphate.chatcolor2.commands;

import com.sulphate.chatcolor2.commands.subcommands.ChatColorSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NewChatColorCommand implements CommandExecutor {

    private static final Map<String, ChatColorSubCommand> SUB_COMMAND_MAP;
    private static final Set<String> SUB_COMMANDS;

    static {
        SUB_COMMAND_MAP = new HashMap<>();
        SUB_COMMANDS = new HashSet<>(Arrays.asList("clear", "confirm", "help", "set", "reset", "reload", "available", "gui", "add", "remove", "group", "custom", "pause"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandName, @NotNull String[] argsArray) {
        List<String> args = new ArrayList<>(Arrays.asList(argsArray));
        String subCommand;

        if (args.isEmpty()) {
            subCommand = "";
        }
        else {
            String firstArg = args.get(0);

            if (SUB_COMMANDS.contains(firstArg)) {
                subCommand = firstArg;
                args.remove(0);
            }
            else if (Bukkit.getPlayer(firstArg) != null) {
                subCommand = "set-other-player";
            }
            else {
                subCommand = "set-self";
            }
        }

        return true;
    }

}
