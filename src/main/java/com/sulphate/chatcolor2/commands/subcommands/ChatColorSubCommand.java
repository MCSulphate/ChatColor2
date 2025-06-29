package com.sulphate.chatcolor2.commands.subcommands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ChatColorSubCommand {

    private final boolean isConsoleSupported;

    protected ChatColorSubCommand(boolean isConsoleSupported) {
        this.isConsoleSupported = isConsoleSupported;
    }

    public boolean isConsoleSupported() {
        return isConsoleSupported;
    }

    abstract void executeCommand(CommandSender sender, List<String> args);

}
