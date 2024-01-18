package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class YamlStorageImpl extends PlayerDataStore {

    private final AutoSaveScheduler saveScheduler;

    private final ConfigsManager configsManager;
    private final ConfigUtils configUtils;

    public YamlStorageImpl(ConfigsManager configsManager, ConfigUtils configUtils, int saveInterval) {
        super();

        this.configsManager = configsManager;
        this.configUtils = configUtils;

        saveScheduler = new AutoSaveScheduler(saveInterval);
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
                config.getLong("default-code")
        ));

        return true;
    }

    @Override
    public boolean savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        if (!data.isDirty()) {
            return true;
        }

        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        config.set("colour", data.getColour());
        config.set("default-code", data.getDefaultCode());

        saveScheduler.saveConfigWithDelay("players" + File.separator + uuid + ".yml", config);
        data.markClean();

        return true;
    }

    @Override
    public void shutdown() {
        saveScheduler.stop();
    }

}
