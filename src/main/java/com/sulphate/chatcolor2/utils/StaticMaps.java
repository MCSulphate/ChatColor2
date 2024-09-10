package com.sulphate.chatcolor2.utils;

import java.util.HashMap;
import java.util.Map;

public class StaticMaps {

    private static final Map<String, String> STATIC_COLOUR_NAMES_MAP;
    private static final Map<String, String> STATIC_MODIFIER_NAMES_MAP;

    static {
        STATIC_COLOUR_NAMES_MAP = new HashMap<>();

        STATIC_COLOUR_NAMES_MAP.put("0", "black");
        STATIC_COLOUR_NAMES_MAP.put("1", "");
        STATIC_COLOUR_NAMES_MAP.put("2", "");
        STATIC_COLOUR_NAMES_MAP.put("3", "");
        STATIC_COLOUR_NAMES_MAP.put("4", "");
        STATIC_COLOUR_NAMES_MAP.put("5", "");
        STATIC_COLOUR_NAMES_MAP.put("6", "");
        STATIC_COLOUR_NAMES_MAP.put("7", "");
        STATIC_COLOUR_NAMES_MAP.put("8", "");
        STATIC_COLOUR_NAMES_MAP.put("9", "");
        STATIC_COLOUR_NAMES_MAP.put("a", "");
        STATIC_COLOUR_NAMES_MAP.put("b", "");
        STATIC_COLOUR_NAMES_MAP.put("c", "");
        STATIC_COLOUR_NAMES_MAP.put("d", "");
        STATIC_COLOUR_NAMES_MAP.put("e", "");
        STATIC_COLOUR_NAMES_MAP.put("f", "");

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

}
