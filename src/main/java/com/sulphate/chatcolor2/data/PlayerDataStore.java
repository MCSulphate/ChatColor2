package com.sulphate.chatcolor2.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerDataStore {

    protected final Map<UUID, PlayerData> dataMap;

    public PlayerDataStore() {
        dataMap = new HashMap<>();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return dataMap.get(uuid);
    }

    public abstract void loadPlayerData(UUID uuid, Callback<Boolean> callback);

    public String getColour(UUID uuid) {
        return dataMap.get(uuid).getColour();
    }

    public void setColour(UUID uuid, String colour) {
        dataMap.get(uuid).setColour(colour);
        savePlayerData(uuid);
    }

    public long getDefaultCode(UUID uuid) {
        return dataMap.get(uuid).getDefaultCode();
    }

    public void setDefaultCode(UUID uuid, long defaultCode) {
        dataMap.get(uuid).setDefaultCode(defaultCode);
        savePlayerData(uuid);
    }

    public abstract void savePlayerData(UUID uuid);

    public void saveAllData() {
         for (UUID uuid : dataMap.keySet()) {
             savePlayerData(uuid);
         }
     }

    public abstract void shutdown();

}
