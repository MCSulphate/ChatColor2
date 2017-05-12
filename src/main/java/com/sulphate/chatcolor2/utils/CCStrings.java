package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.ChatColor;

public class CCStrings {

    public static String prefix = "&5&l[&6Chat&aC&bo&cl&do&er&5&l] &e".replace('&', ChatColor.COLOR_CHAR);
    public static String help = prefix + MainClass.get().getMessage("help").replace('&', ChatColor.COLOR_CHAR);
    public static String notargs = prefix + MainClass.get().getMessage("not-enough-args").replace('&', ChatColor.COLOR_CHAR);
    public static String plusargs = prefix + MainClass.get().getMessage("too-many-args").replace('&', ChatColor.COLOR_CHAR);
    public static String notjoin = prefix + MainClass.get().getMessage("player-not-joined").replace('&', ChatColor.COLOR_CHAR);
    public static String notplayer = prefix + MainClass.get().getMessage("players-only").replace('&', ChatColor.COLOR_CHAR);
    public static String noperms = prefix + MainClass.get().getMessage("no-permissions").replace('&', ChatColor.COLOR_CHAR);
    public static String nocolperm = prefix + MainClass.get().getMessage("no-color-perms").replace('&', ChatColor.COLOR_CHAR);
    public static String nomodperm = prefix + MainClass.get().getMessage("no-mod-perms").replace('&', ChatColor.COLOR_CHAR);
    public static String invcol = prefix + MainClass.get().getMessage("invalid-color").replace('&', ChatColor.COLOR_CHAR);
    public static String invcom = prefix + MainClass.get().getMessage("invalid-command").replace('&', ChatColor.COLOR_CHAR);
    public static String invmod = prefix + MainClass.get().getMessage("invalid-modifier").replace('&', ChatColor.COLOR_CHAR);
    public static String invset = prefix + MainClass.get().getMessage("invalid-setting").replace('&', ChatColor.COLOR_CHAR);
    public static String needbool = prefix + MainClass.get().getMessage("needs-boolean").replace('&', ChatColor.COLOR_CHAR);
    public static String needint = prefix + MainClass.get().getMessage("needs-number").replace('&', ChatColor.COLOR_CHAR);
    public static String yourcol = prefix + MainClass.get().getMessage("current-color").replace('&', ChatColor.COLOR_CHAR);
    public static String setownc = prefix + MainClass.get().getMessage("set-own-color").replace('&', ChatColor.COLOR_CHAR);
    public static String setothc = prefix + MainClass.get().getMessage("set-others-color").replace('&', ChatColor.COLOR_CHAR);
    public static String setyourc = prefix + MainClass.get().getMessage("player-set-your-color").replace('&', ChatColor.COLOR_CHAR);
    public static String colthis = MainClass.get().getMessage("this").replace('&', ChatColor.COLOR_CHAR);
    public static String confirm = prefix + MainClass.get().getMessage("confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String notconfirm = prefix + MainClass.get().getMessage("did-not-confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadycon = prefix + MainClass.get().getMessage("already-confirming").replace('&', ChatColor.COLOR_CHAR);
    public static String noconfirm = prefix + MainClass.get().getMessage("nothing-to-confirm").replace('&', ChatColor.COLOR_CHAR);
    public static String relconfig = prefix + MainClass.get().getMessage("reloaded-config").replace('&', ChatColor.COLOR_CHAR);
    public static String alreadyset = prefix + MainClass.get().getMessage("already-set").replace('&', ChatColor.COLOR_CHAR);
    public static String iscur = MainClass.get().getMessage("is-currently").replace('&', ChatColor.COLOR_CHAR);
    public static String tochng = prefix + MainClass.get().getMessage("to-change").replace('&', ChatColor.COLOR_CHAR);
    public static String cmdexst = prefix + MainClass.get().getMessage("command-exists").replace('&', ChatColor.COLOR_CHAR);
    public static String interr = prefix + MainClass.get().getMessages("internal-error").replace('&', ChatColor.COLOR_CHAR);
    public static String errdet = prefix + MainClass.get().getMessages("error-details").replace('&', ChatColor.COLOR_CHAR);

}
