package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;

public class CCStrings {

    public static String prefix;
    public static String authormessage1 = CC2Utils.colourise("&eThis plugin was developed by &bSulphate&e! You can view the plugin page here:");
    public static String authormessage2 = CC2Utils.colourise("&e> &bhttps://dev.bukkit.org/projects/chatcolor-s &e<");
    public static String help;
    public static String notenoughargs;
    public static String toomanyargs;
    public static String playernotjoined;
    public static String playersonly;
    public static String nopermissions;
    public static String nocolorperms;
    public static String nomodperms;
    public static String invalidcolor;
    public static String invalidcommand;
    public static String invalidmodifier;
    public static String invalidsetting;
    public static String needsboolean;
    public static String needsinteger;
    public static String currentcolor;
    public static String setowncolor;
    public static String setotherscolor;
    public static String playersetyourcolor;
    public static String colthis;
    public static String confirm;
    public static String didnotconfirm;
    public static String alreadyconfirming;
    public static String nothingtoconfirm;
    public static String reloadedmessages;
    public static String alreadyset;
    public static String iscurrently;
    public static String tochange;
    public static String commandexists;
    public static String internalerror;
    public static String errordetails;
    public static String plugindisabled;
    public static String failedtoenable;
    public static String successfullyenabled;
    public static String alreadyenabled;
    
    public static void reloadMessages() {
        CC2Utils utils = MainClass.getUtils();

        prefix = CC2Utils.colourise(utils.getMessage("prefix"));
        help = prefix + CC2Utils.colourise(utils.getMessage("help"));
        notenoughargs = prefix + CC2Utils.colourise(utils.getMessage("not-enough-args"));
        toomanyargs = prefix + CC2Utils.colourise(utils.getMessage("too-many-args"));
        playernotjoined = prefix + CC2Utils.colourise(utils.getMessage("player-not-joined"));
        playersonly = prefix + CC2Utils.colourise(utils.getMessage("players-only"));
        nopermissions = prefix + CC2Utils.colourise(utils.getMessage("no-permissions"));
        nocolorperms = prefix + CC2Utils.colourise(utils.getMessage("no-color-perms"));
        nomodperms = prefix + CC2Utils.colourise(utils.getMessage("no-mod-perms"));
        invalidcolor = prefix + CC2Utils.colourise(utils.getMessage("invalid-color"));
        invalidcommand = prefix + CC2Utils.colourise(utils.getMessage("invalid-command"));
        invalidmodifier = prefix + CC2Utils.colourise(utils.getMessage("invalid-modifier"));
        invalidsetting = prefix + CC2Utils.colourise(utils.getMessage("invalid-setting"));
        needsboolean = prefix + CC2Utils.colourise(utils.getMessage("needs-boolean"));
        needsinteger = prefix + CC2Utils.colourise(utils.getMessage("needs-number"));
        currentcolor = prefix + CC2Utils.colourise(utils.getMessage("current-color"));
        setowncolor = prefix + CC2Utils.colourise(utils.getMessage("set-own-color"));
        setotherscolor = prefix + CC2Utils.colourise(utils.getMessage("set-others-color"));
        playersetyourcolor = prefix + CC2Utils.colourise(utils.getMessage("player-set-your-color"));
        colthis = CC2Utils.colourise(utils.getMessage("this"));
        confirm = prefix + CC2Utils.colourise(utils.getMessage("confirm"));
        didnotconfirm = prefix + CC2Utils.colourise(utils.getMessage("did-not-confirm"));
        alreadyconfirming = prefix + CC2Utils.colourise(utils.getMessage("already-confirming"));
        nothingtoconfirm = prefix + CC2Utils.colourise(utils.getMessage("nothing-to-confirm"));
        reloadedmessages = prefix + CC2Utils.colourise(utils.getMessage("reloaded-messages"));
        alreadyset = prefix + CC2Utils.colourise(utils.getMessage("already-set"));
        iscurrently = CC2Utils.colourise(utils.getMessage("is-currently"));
        tochange = prefix + CC2Utils.colourise(utils.getMessage("to-change"));
        commandexists = prefix + CC2Utils.colourise(utils.getMessage("command-exists"));
        internalerror = prefix + CC2Utils.colourise(utils.getMessage("internal-error"));
        errordetails = prefix + CC2Utils.colourise(utils.getMessage("error-details"));
        plugindisabled = prefix + CC2Utils.colourise(utils.getMessage("plugin-disabled"));
        failedtoenable = prefix + CC2Utils.colourise(utils.getMessage("failed-to-enable"));
        successfullyenabled = prefix + CC2Utils.colourise(utils.getMessage("successfully-enabled"));
        alreadyenabled = prefix + CC2Utils.colourise(utils.getMessage("already-enabled"));
    }
}
