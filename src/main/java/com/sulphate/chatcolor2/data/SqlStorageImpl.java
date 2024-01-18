package com.sulphate.chatcolor2.data;

import java.util.UUID;

public class SqlStorageImpl extends PlayerDataStore {

    public SqlStorageImpl() {
        super();
    }

    @Override
    public boolean loadPlayerData(String name) {
        return false;
    }

    @Override
    public boolean loadPlayerData(UUID uuid) {
        return false;
    }

    @Override
    public boolean savePlayerData(UUID uuid) {
        return false;
    }

    @Override
    public void shutdown() {

    }

}
