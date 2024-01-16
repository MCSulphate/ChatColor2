package com.sulphate.chatcolor2.data;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String colour;
    private int defaultCode;
    private boolean dirty;

    public PlayerData(UUID uuid, String colour, int defaultCode) {
        this.uuid = uuid;
        this.colour = colour;
        this.defaultCode = defaultCode;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(int defaultCode) {
        this.defaultCode = defaultCode;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markClean() {
        dirty = false;
    }

}
