package com.sulphate.chatcolor2.test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewChatColorCommand implements CommandExecutor {

    //This is a test class. It has not been implemented.
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        //This is a test for console commands.
        if (sender instanceof Player) {



        }
        else {



        }
        return true;

    }

    //This is a test for a universal permissions checker, to clean up the code.
    public boolean checkPermissions(String[] args, Player player) {

        
        return true;

    }

}
