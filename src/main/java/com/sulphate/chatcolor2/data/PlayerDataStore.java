package com.sulphate.chatcolor2.data;

import java.util.UUID;

public interface PlayerDataStore {

    boolean loadPlayerData(String name);

    boolean loadPlayerData(UUID uuid);

    String getColour(UUID uuid);

    void setColour(UUID uuid, String colour);

    int getDefaultCode(UUID uuid);

    void setDefaultCode(UUID uuid, int defaultCode);

    boolean savePlayerData(UUID uuid);

    boolean saveAllData();

}
