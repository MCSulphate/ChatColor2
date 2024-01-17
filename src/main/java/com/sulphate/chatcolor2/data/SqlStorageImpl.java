package com.sulphate.chatcolor2.data;

import java.util.UUID;

public class SqlStorageImpl implements PlayerDataStore {

    @Override
    public boolean loadPlayerData(String name) {
        return false;
    }

    @Override
    public boolean loadPlayerData(UUID uuid) {
        return false;
    }

    @Override
    public String getColour(UUID uuid) {
        return null;
    }

    @Override
    public void setColour(UUID uuid, String colour) {

    }

    @Override
    public long getDefaultCode(UUID uuid) {
        return 0;
    }

    @Override
    public void setDefaultCode(UUID uuid, long defaultCode) {

    }

    @Override
    public boolean savePlayerData(UUID uuid) {
        return false;
    }

    @Override
    public boolean saveAllData() {
        return false;
    }

    @Override
    public void shutdown() {

    }

}
