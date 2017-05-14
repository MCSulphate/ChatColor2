package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.ChatColor;

public class CCStrings {

    public static String prefix = "&5&l[&6Chat&aC&bo&cl&do&er&5&l] &e".replace('&', ChatColor.COLOR_CHAR);
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
    public static String reloadedconfig = prefix + MainClass.getUtils().getMessage("reloaded-config").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadyset = prefix + MainClass.getUtils().getMessage("already-set").replace('&', ChatColor.COLOR_CHAR);
    public static String iscurrently = MainClass.getUtils().getMessage("is-currently").replace('&', ChatColor.COLOR_CHAR);
    public static String tochange = prefix + MainClass.getUtils().getMessage("to-change").replace('&', ChatColor.COLOR_CHAR);
    public static String commandexists = prefix + MainClass.getUtils().getMessage("command-exists").replace('&', ChatColor.COLOR_CHAR);
    public static String internalerror = prefix + MainClass.getUtils().getMessage("internal-error").replace('&', ChatColor.COLOR_CHAR);
    public static String errordetails = prefix + MainClass.getUtils().getMessage("error-details").replace('&', ChatColor.COLOR_CHAR);
    public static String plugindisabled = prefix + MainClass.getUtils().getMessage("plugin-disabled").replace('&', ChatColor.COLOR_CHAR);
}
