package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.utils.ConfigUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerDataStore {

    private final ConfigUtils configUtils;

    protected final Map<UUID, PlayerData> dataMap;

    public PlayerDataStore(ConfigUtils configUtils) {
        this.configUtils = configUtils;
        dataMap = new HashMap<>();
    }

    public boolean loadPlayerData(String name) {
        UUID uuid = configUtils.getUUIDFromName(name);

        if (uuid == null) {
            return false;
        }

        return loadPlayerData(uuid);
    }

    public abstract boolean loadPlayerData(UUID uuid);

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

    public abstract boolean savePlayerData(UUID uuid);

    public boolean saveAllData() {
         boolean allSucceeded = true;

         for (UUID uuid : dataMap.keySet()) {
             if (!savePlayerData(uuid)) {
                 allSucceeded = false;
             }
         }

         return allSucceeded;
     }

    public abstract void shutdown();

}
