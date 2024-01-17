package com.sulphate.chatcolor2.data;

import java.util.UUID;

public interface PlayerDataStore {

    boolean loadPlayerData(String name);

    boolean loadPlayerData(UUID uuid);

    String getColour(UUID uuid);

    void setColour(UUID uuid, String colour);

    long getDefaultCode(UUID uuid);

    void setDefaultCode(UUID uuid, long defaultCode);

    boolean savePlayerData(UUID uuid);

    boolean saveAllData();

    void shutdown();

}
