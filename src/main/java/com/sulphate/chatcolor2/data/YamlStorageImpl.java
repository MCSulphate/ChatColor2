package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class YamlStorageImpl implements PlayerDataStore {

    private final AutoSaveScheduler saveScheduler;
    private final Map<UUID, PlayerData> dataMap;

    private final ConfigsManager configsManager;
    private final ConfigUtils configUtils;

    public YamlStorageImpl(ConfigsManager configsManager, ConfigUtils configUtils, int saveInterval) {
        this.configsManager = configsManager;
        this.configUtils = configUtils;

        saveScheduler = new AutoSaveScheduler(saveInterval);
        dataMap = new HashMap<>();
    }

    public void updateSaveInterval(int saveInterval) {
        saveScheduler.setSaveInterval(saveInterval);
    }

    @Override
    public boolean loadPlayerData(String name) {
        UUID uuid = configUtils.getUUIDFromName(name);

        if (uuid == null) {
            return false;
        }

        return loadPlayerData(uuid);
    }

    @Override
    public boolean loadPlayerData(UUID uuid) {
        configsManager.loadPlayerConfig(uuid);
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        if (config == null) {
            return false;
        }

        dataMap.put(uuid, new PlayerData(
                uuid,
                config.getString("colour"),
                config.getInt("default-code")
        ));

        return true;
    }

    @Override
    public String getColour(UUID uuid) {
        return dataMap.get(uuid).getColour();
    }

    @Override
    public void setColour(UUID uuid, String colour) {
        dataMap.get(uuid).setColour(colour);
    }

    @Override
    public int getDefaultCode(UUID uuid) {
        return dataMap.get(uuid).getDefaultCode();
    }

    @Override
    public void setDefaultCode(UUID uuid, int defaultCode) {
        dataMap.get(uuid).setDefaultCode(defaultCode);
    }

    @Override
    public boolean savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        if (!data.isDirty()) {
            return true;
        }

        YamlConfiguration config = configsManager.getPlayerConfig(uuid);
        saveScheduler.saveConfigWithDelay("players" + File.separator + uuid + ".yml", config);
        data.markClean();

        return true;
    }

    public boolean saveAllData() {
        for (UUID uuid : dataMap.keySet()) {
            savePlayerData(uuid);
        }

        return true;
    }

    @Override
    public void shutdown() {
        saveScheduler.stop();
    }

}
