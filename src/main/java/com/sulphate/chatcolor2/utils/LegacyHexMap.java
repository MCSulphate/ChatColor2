package com.sulphate.chatcolor2.utils;

import java.util.HashMap;
import java.util.Map;

public class LegacyHexMap {

    private static final Map<Character, String> HEX_MAP = new HashMap<>();

    static {
        HEX_MAP.put('0', "#000000");
        HEX_MAP.put('1', "#0000aa");
        HEX_MAP.put('2', "#00aa00");
        HEX_MAP.put('3', "#00aaaa");
        HEX_MAP.put('4', "#aa0000");
        HEX_MAP.put('5', "#aa00aa");
        HEX_MAP.put('6', "#ffaa00");
        HEX_MAP.put('7', "#aaaaaa");
        HEX_MAP.put('8', "#555555");
        HEX_MAP.put('9', "#5555ff");
        HEX_MAP.put('a', "#55ff55");
        HEX_MAP.put('b', "#55ffff");
        HEX_MAP.put('c', "#ff5555");
        HEX_MAP.put('d', "#ff55ff");
        HEX_MAP.put('e', "#ffff55");
        HEX_MAP.put('f', "#ffffff");
    }

    public static String getHexForLegacyColour(char colour) {
        return HEX_MAP.get(colour);
    }

}
