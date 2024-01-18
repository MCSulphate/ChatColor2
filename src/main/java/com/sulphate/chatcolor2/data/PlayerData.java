package com.sulphate.chatcolor2.data;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String colour;
    private long defaultCode;
    private boolean isTemporary;
    private boolean dirty;

    public PlayerData(UUID uuid, String colour, long defaultCode) {
        this.uuid = uuid;
        this.colour = colour;
        this.defaultCode = defaultCode;
    }

    // The null colour and negative default code cause the default to be set.
    public static PlayerData createTemporaryData(UUID uuid) {
        return new PlayerData(uuid, null, -1);
    }

    public boolean isTemporary() {
        return isTemporary;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
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
