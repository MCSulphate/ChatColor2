package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.ChatColor;

public class CCStrings {

    public static String prefix = MainClass.getUtils().getMessage("prefix").replace('&', ChatColor.COLOR_CHAR);
    public static String authormessage1 = "&eThis plugin was developed by &bSulphate&e! You can view the plugin page here:".replace('&', ChatColor.COLOR_CHAR);
    public static String authormessage2 = "&e> &bhttps://dev.bukkit.org/projects/chatcolor-s &e<".replace('&', ChatColor.COLOR_CHAR);
    public static String help = prefix + MainClass.getUtils().getMessage("help").replace('&', ChatColor.COLOR_CHAR);
    public static String notenoughargs = prefix + MainClass.getUtils().getMessage("not-enough-args").replace('&', ChatColor.COLOR_CHAR);
    public static String toomanyargs = prefix + MainClass.getUtils().getMessage("too-many-args").replace('&', ChatColor.COLOR_CHAR);
    public static String playernotjoined = prefix + MainClass.getUtils().getMessage("player-not-joined").replace('&', ChatColor.COLOR_CHAR);
    public static String playersonly = prefix + MainClass.getUtils().getMessage("players-only").replace('&', ChatColor.COLOR_CHAR);
    public static String nopermissions = prefix + MainClass.getUtils().getMessage("no-permissions").replace('&', ChatColor.COLOR_CHAR);
    public static String nocolorperms = prefix + MainClass.getUtils().getMessage("no-color-perms").replace('&', ChatColor.COLOR_CHAR);
    public static String nomodperms = prefix + MainClass.getUtils().getMessage("no-mod-perms").replace('&', ChatColor.COLOR_CHAR);
    public static String invalidcolor = prefix + MainClass.getUtils().getMessage("invalid-color").replace('&', ChatColor.COLOR_CHAR);
    public static String invalidcommand = prefix + MainClass.getUtils().getMessage("invalid-command").replace('&', ChatColor.COLOR_CHAR);
    public static String invalidmodifier = prefix + MainClass.getUtils().getMessage("invalid-modifier").replace('&', ChatColor.COLOR_CHAR);
    public static String invalidsetting = prefix + MainClass.getUtils().getMessage("invalid-setting").replace('&', ChatColor.COLOR_CHAR);
    public static String needsboolean = prefix + MainClass.getUtils().getMessage("needs-boolean").replace('&', ChatColor.COLOR_CHAR);
    public static String needsinteger = prefix + MainClass.getUtils().getMessage("needs-number").replace('&', ChatColor.COLOR_CHAR);
    public static String currentcolor = prefix + MainClass.getUtils().getMessage("current-color").replace('&', ChatColor.COLOR_CHAR);
    public static String setowncolor = prefix + MainClass.getUtils().getMessage("set-own-color").replace('&', ChatColor.COLOR_CHAR);
    public static String setotherscolor = prefix + MainClass.getUtils().getMessage("set-others-color").replace('&', ChatColor.COLOR_CHAR);
    public static String playersetyourcolor = prefix + MainClass.getUtils().getMessage("player-set-your-color").replace('&', ChatColor.COLOR_CHAR);
    public static String colthis = MainClass.getUtils().getMessage("this").replace('&', ChatColor.COLOR_CHAR);
    public static String confirm = prefix + MainClass.getUtils().getMessage("confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String didnotconfirm = prefix + MainClass.getUtils().getMessage("did-not-confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadyconfirming = prefix + MainClass.getUtils().getMessage("already-confirming").replace('&', ChatColor.COLOR_CHAR);
    public static String nothingtoconfirm = prefix + MainClass.getUtils().getMessage("nothing-to-confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String reloadedmessages = prefix + MainClass.getUtils().getMessage("reloaded-messages").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadyset = prefix + MainClass.getUtils().getMessage("already-set").replace('&', ChatColor.COLOR_CHAR);
    public static String iscurrently = MainClass.getUtils().getMessage("is-currently").replace('&', ChatColor.COLOR_CHAR);
    public static String tochange = prefix + MainClass.getUtils().getMessage("to-change").replace('&', ChatColor.COLOR_CHAR);
    public static String commandexists = prefix + MainClass.getUtils().getMessage("command-exists").replace('&', ChatColor.COLOR_CHAR);
    public static String internalerror = prefix + MainClass.getUtils().getMessage("internal-error").replace('&', ChatColor.COLOR_CHAR);
    public static String errordetails = prefix + MainClass.getUtils().getMessage("error-details").replace('&', ChatColor.COLOR_CHAR);
    public static String plugindisabled = prefix + MainClass.getUtils().getMessage("plugin-disabled").replace('&', ChatColor.COLOR_CHAR);
    public static String failedtoenable = prefix + MainClass.getUtils().getMessage("failed-to-enable").replace('&', ChatColor.COLOR_CHAR);
    public static String successfullyenabled = prefix + MainClass.getUtils().getMessage("successfully-enabled").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadyenabled = prefix + MainClass.getUtils().getMessage("already-enabled").replace('&', ChatColor.COLOR_CHAR);
    
    public static void reloadMessages() {
        prefix = MainClass.getUtils().getMessage("prefix").replace('&', ChatColor.COLOR_CHAR);
        help = prefix + MainClass.getUtils().getMessage("help").replace('&', ChatColor.COLOR_CHAR);
        notenoughargs = prefix + MainClass.getUtils().getMessage("not-enough-args").replace('&', ChatColor.COLOR_CHAR);
        toomanyargs = prefix + MainClass.getUtils().getMessage("too-many-args").replace('&', ChatColor.COLOR_CHAR);
        playernotjoined = prefix + MainClass.getUtils().getMessage("player-not-joined").replace('&', ChatColor.COLOR_CHAR);
        playersonly = prefix + MainClass.getUtils().getMessage("players-only").replace('&', ChatColor.COLOR_CHAR);
        nopermissions = prefix + MainClass.getUtils().getMessage("no-permissions").replace('&', ChatColor.COLOR_CHAR);
        nocolorperms = prefix + MainClass.getUtils().getMessage("no-color-perms").replace('&', ChatColor.COLOR_CHAR);
        nomodperms = prefix + MainClass.getUtils().getMessage("no-mod-perms").replace('&', ChatColor.COLOR_CHAR);
        invalidcolor = prefix + MainClass.getUtils().getMessage("invalid-color").replace('&', ChatColor.COLOR_CHAR);
        invalidcommand = prefix + MainClass.getUtils().getMessage("invalid-command").replace('&', ChatColor.COLOR_CHAR);
        invalidmodifier = prefix + MainClass.getUtils().getMessage("invalid-modifier").replace('&', ChatColor.COLOR_CHAR);
        invalidsetting = prefix + MainClass.getUtils().getMessage("invalid-setting").replace('&', ChatColor.COLOR_CHAR);
        needsboolean = prefix + MainClass.getUtils().getMessage("needs-boolean").replace('&', ChatColor.COLOR_CHAR);
        needsinteger = prefix + MainClass.getUtils().getMessage("needs-number").replace('&', ChatColor.COLOR_CHAR);
        currentcolor = prefix + MainClass.getUtils().getMessage("current-color").replace('&', ChatColor.COLOR_CHAR);
        setowncolor = prefix + MainClass.getUtils().getMessage("set-own-color").replace('&', ChatColor.COLOR_CHAR);
        setotherscolor = prefix + MainClass.getUtils().getMessage("set-others-color").replace('&', ChatColor.COLOR_CHAR);
        playersetyourcolor = prefix + MainClass.getUtils().getMessage("player-set-your-color").replace('&', ChatColor.COLOR_CHAR);
        colthis = MainClass.getUtils().getMessage("this").replace('&', ChatColor.COLOR_CHAR);
        confirm = prefix + MainClass.getUtils().getMessage("confirm").replace('&', ChatColor.COLOR_CHAR);
        didnotconfirm = prefix + MainClass.getUtils().getMessage("did-not-confirm").replace('&', ChatColor.COLOR_CHAR);
        alreadyconfirming = prefix + MainClass.getUtils().getMessage("already-confirming").replace('&', ChatColor.COLOR_CHAR);
        nothingtoconfirm = prefix + MainClass.getUtils().getMessage("nothing-to-confirm").replace('&', ChatColor.COLOR_CHAR);
        reloadedmessages = prefix + MainClass.getUtils().getMessage("reloaded-messages").replace('&', ChatColor.COLOR_CHAR);
        alreadyset = prefix + MainClass.getUtils().getMessage("already-set").replace('&', ChatColor.COLOR_CHAR);
        iscurrently = MainClass.getUtils().getMessage("is-currently").replace('&', ChatColor.COLOR_CHAR);
        tochange = prefix + MainClass.getUtils().getMessage("to-change").replace('&', ChatColor.COLOR_CHAR);
        commandexists = prefix + MainClass.getUtils().getMessage("command-exists").replace('&', ChatColor.COLOR_CHAR);
        internalerror = prefix + MainClass.getUtils().getMessage("internal-error").replace('&', ChatColor.COLOR_CHAR);
        errordetails = prefix + MainClass.getUtils().getMessage("error-details").replace('&', ChatColor.COLOR_CHAR);
        plugindisabled = prefix + MainClass.getUtils().getMessage("plugin-disabled").replace('&', ChatColor.COLOR_CHAR);
        failedtoenable = prefix + MainClass.getUtils().getMessage("failed-to-enable").replace('&', ChatColor.COLOR_CHAR);
        successfullyenabled = prefix + MainClass.getUtils().getMessage("successfully-enabled").replace('&', ChatColor.COLOR_CHAR);
        alreadyenabled = prefix + MainClass.getUtils().getMessage("already-enabled").replace('&', ChatColor.COLOR_CHAR);
    }
}
