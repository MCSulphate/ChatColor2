package com.sulphate.chatcolor2.data;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerData {

    private final UUID uuid;
    private String colour;
    private Set<Character> modifiers;
    private long defaultCode;
    private boolean isTemporary;
    private boolean dirty;

    public PlayerData(UUID uuid, String colour, long defaultCode) {
        this.uuid = uuid;
        this.defaultCode = defaultCode;

        this.colour = colour == null ? "" : getColourName(colour);
        modifiers = colour == null ? new HashSet<>() : getModifiers(colour);
    }

    // The null colour and negative default code cause the default to be set.
    public static PlayerData createTemporaryData(UUID uuid) {
        PlayerData data = new PlayerData(uuid, null, -1);
        data.setTemporary();

        return data;
    }

    private static Set<Character> getModifiers(String colour) {
        if (colour == null || colour.isEmpty()) {
            return new HashSet<>();
        }

        int secondIndex = colour.substring(1).indexOf('&');

        if (secondIndex == -1) {
            return new HashSet<>();
        }
        else {
            return Arrays.stream(colour
                            .substring(secondIndex + 1)
                            .replace("&", "")
                            .split(""))
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toSet());
        }
    }

    private static String getColourName(String colour) {
        if (colour == null) {
            return "";
        }

        if (colour.startsWith("%") || colour.isEmpty()) {
            return colour;
        }

        int secondIndex = colour.substring(1).indexOf('&');

        if (secondIndex == -1) {
            return colour.substring(1);
        }
        else {
            return colour.substring(1, secondIndex + 1);
        }
    }

    private void setTemporary() {
        isTemporary = true;
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setColourName(String colour) {
        this.colour = colour;
        dirty = true;
    }

    public String getColourName() {
        return colour;
    }

    public void addModifier(char modifier) {
        modifiers.add(modifier);
        dirty = true;
    }

    public void removeModifier(char modifier) {
        modifiers.remove(modifier);
        dirty = true;
    }

    public Set<Character> getModifiers() {
        return modifiers;
    }

    public String getColour() {
        if (colour.isEmpty()) {
            return colour;
        }

        String prefix = colour.startsWith("%") ? "" : "&";
        return prefix + colour + modifiers.stream().map(m -> "&" + m).collect(Collectors.joining());
    }

    public void setColour(String colour) {
        this.colour = getColourName(colour);
        this.modifiers = getModifiers(colour);

        dirty = true;
    }

    public long getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(long defaultCode) {
        this.defaultCode = defaultCode;
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markClean() {
        dirty = false;
    }

}
