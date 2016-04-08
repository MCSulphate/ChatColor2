package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;

public class CCStrings {

    public static String prefix = "§5§l[§6Chat§aC§bo§cl§do§er§5§l] §e";
    public static String notplayer = prefix + MainClass.get().getConfig().getString("messages.players-only").replace("&", "§");
    public static String notonline = prefix + MainClass.get().getConfig().getString("messages.player-not-online").replace("&", "§");
    public static String noperms = prefix + MainClass.get().getConfig().getString("messages.no-permissions").replace("&", "§");
    public static String nocolperm = prefix + MainClass.get().getConfig().getString("messages.no-color-perms").replace("&", "§");
    public static String nocmperm = prefix + MainClass.get().getConfig().getString("messages.no-col-mod-perms").replace("&", "§");
    public static String invcol = prefix + MainClass.get().getConfig().getString("messages.invalid-color").replace("&", "§");
    public static String invcom = prefix + MainClass.get().getConfig().getString("messages.invalid-command").replace("&", "§");
    public static String invmod = prefix + MainClass.get().getConfig().getString("messages.invalid-modifier").replace("&", "§");
    public static String invset = prefix + MainClass.get().getConfig().getString("messages.invalid-setting").replace("&", "§");
    public static String needbool = prefix + MainClass.get().getConfig().getString("messages.needs-boolean").replace("&", "§");
    public static String needint = prefix + MainClass.get().getConfig().getString("messages.needs-number").replace("&", "§");
    public static String yourcol = prefix + MainClass.get().getConfig().getString("messages.current-color").replace("&", "§");
    public static String setownc = prefix + MainClass.get().getConfig().getString("messages.set-own-color").replace("&", "§");
    public static String setothc = prefix + MainClass.get().getConfig().getString("messages.set-others-color").replace("&", "§");
    public static String setyourc = prefix + MainClass.get().getConfig().getString("messages.player-set-your-color").replace("&", "§");
    public static String colthis = MainClass.get().getConfig().getString("messages.this").replace("&", "§");
    public static String confirm = prefix + MainClass.get().getConfig().getString("messages.confirm").replace("&", "§");
    public static String notconfirm = prefix + MainClass.get().getConfig().getString("messages.did-not-confirm").replace("&", "§");
    public static String alreadycon = prefix + MainClass.get().getConfig().getString("messages.already-confirming").replace("&", "§");
    public static String noconfirm = prefix + MainClass.get().getConfig().getString("messages.nothing-to-confirm").replace("&", "§");
    public static String relconfig = prefix + MainClass.get().getConfig().getString("messages.reloaded-config").replace("&", "§");
    public static String alreadyset = prefix + MainClass.get().getConfig().getString("messages.already-set").replace("&", "§");
    public static String setdesc = prefix + MainClass.get().getConfig().getString("messages.set-description").replace("&", "§");

}
