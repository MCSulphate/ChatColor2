package com.sulphate.chatcolor2.utils;

import java.util.HashMap;
import java.util.Map;

public class StaticMaps {

    private static final Map<String, String> STATIC_COLOUR_NAMES_MAP;
    private static final Map<String, String> STATIC_MODIFIER_NAMES_MAP;

    static {
        STATIC_COLOUR_NAMES_MAP = new HashMap<>();

        STATIC_COLOUR_NAMES_MAP.put("0", "black");
        STATIC_COLOUR_NAMES_MAP.put("1", "dark.blue");
        STATIC_COLOUR_NAMES_MAP.put("2", "dark.green");
        STATIC_COLOUR_NAMES_MAP.put("3", "dark.aqua");
        STATIC_COLOUR_NAMES_MAP.put("4", "dark.red");
        STATIC_COLOUR_NAMES_MAP.put("5", "dark.purple");
        STATIC_COLOUR_NAMES_MAP.put("6", "gold");
        STATIC_COLOUR_NAMES_MAP.put("7", "gray");
        STATIC_COLOUR_NAMES_MAP.put("8", "dark.gray");
        STATIC_COLOUR_NAMES_MAP.put("9", "blue");
        STATIC_COLOUR_NAMES_MAP.put("a", "green");
        STATIC_COLOUR_NAMES_MAP.put("b", "aqua");
        STATIC_COLOUR_NAMES_MAP.put("c", "red");
        STATIC_COLOUR_NAMES_MAP.put("d", "light.purple");
        STATIC_COLOUR_NAMES_MAP.put("e", "yellow");
        STATIC_COLOUR_NAMES_MAP.put("f", "white");

        STATIC_MODIFIER_NAMES_MAP = new HashMap<>();

        STATIC_MODIFIER_NAMES_MAP.put("k", "obfuscated");
        STATIC_MODIFIER_NAMES_MAP.put("l", "bold");
        STATIC_MODIFIER_NAMES_MAP.put("m", "strikethrough");
        STATIC_MODIFIER_NAMES_MAP.put("n", "underline");
        STATIC_MODIFIER_NAMES_MAP.put("o", "italic");
    }

    public static String getColourName(String colour) {
        return STATIC_COLOUR_NAMES_MAP.getOrDefault(colour, "invalid-color");
    }

    public static String getModifierName(String modifier) {
        return STATIC_MODIFIER_NAMES_MAP.getOrDefault(modifier, "invalid-modifier");
    }

    public static String getVerbosePermission(String shortPermission) {
        String lastPart = shortPermission.substring(shortPermission.lastIndexOf(".") + 1);

        if (STATIC_COLOUR_NAMES_MAP.containsKey(lastPart)) {
            return "chatcolor.color." + STATIC_COLOUR_NAMES_MAP.get(lastPart);
        }
        else if (STATIC_MODIFIER_NAMES_MAP.containsKey(lastPart)) {
            return "chatcolor.modifier." + STATIC_MODIFIER_NAMES_MAP.get(lastPart);
        }
        else {
            return shortPermission;
        }
    }

}
