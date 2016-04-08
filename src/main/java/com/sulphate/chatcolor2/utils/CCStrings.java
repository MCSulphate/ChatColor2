package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;

public class CCStrings {

    public static String prefix = "§5§l[§6Chat§aC§bo§cl§do§er§5§l] §e";
    public static String help = prefix + MainClass.get().getMessage("help").replace("&", "§");
    public static String notplayer = prefix + MainClass.get().getMessage("messages.players-only").replace("&", "§");
    public static String notonline = prefix + MainClass.get().getMessage("messages.player-not-online").replace("&", "§");
    public static String noperms = prefix + MainClass.get().getMessage("messages.no-permissions").replace("&", "§");
    public static String nocolperm = prefix + MainClass.get().getMessage("messages.no-color-perms").replace("&", "§");
    public static String nocmperm = prefix + MainClass.get().getMessage("messages.no-col-mod-perms").replace("&", "§");
    public static String invcol = prefix + MainClass.get().getMessage("messages.invalid-color").replace("&", "§");
    public static String invcom = prefix + MainClass.get().getMessage("messages.invalid-command").replace("&", "§");
    public static String invmod = prefix + MainClass.get().getMessage("messages.invalid-modifier").replace("&", "§");
    public static String invset = prefix + MainClass.get().getMessage("messages.invalid-setting").replace("&", "§");
    public static String needbool = prefix + MainClass.get().getMessage("messages.needs-boolean").replace("&", "§");
    public static String needint = prefix + MainClass.get().getMessage("messages.needs-number").replace("&", "§");
    public static String yourcol = prefix + MainClass.get().getMessage("messages.current-color").replace("&", "§");
    public static String setownc = prefix + MainClass.get().getMessage("messages.set-own-color").replace("&", "§");
    public static String setothc = prefix + MainClass.get().getMessage("messages.set-others-color").replace("&", "§");
    public static String setyourc = prefix + MainClass.get().getMessage("messages.player-set-your-color").replace("&", "§");
    public static String colthis = MainClass.get().getMessage("messages.this").replace("&", "§");
    public static String confirm = prefix + MainClass.get().getMessage("messages.confirm").replace("&", "§");
    public static String notconfirm = prefix + MainClass.get().getMessage("messages.did-not-confirm").replace("&", "§");
    public static String alreadycon = prefix + MainClass.get().getMessage("messages.already-confirming").replace("&", "§");
    public static String noconfirm = prefix + MainClass.get().getMessage("messages.nothing-to-confirm").replace("&", "§");
    public static String relconfig = prefix + MainClass.get().getMessage("messages.reloaded-config").replace("&", "§");
    public static String alreadyset = prefix + MainClass.get().getMessage("messages.already-set").replace("&", "§");
    public static String setdesc = prefix + MainClass.get().getMessage("messages.set-description").replace("&", "§");

}
