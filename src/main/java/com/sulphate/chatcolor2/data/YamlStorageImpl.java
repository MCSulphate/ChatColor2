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

    public YamlStorageImpl(ConfigsManager configsManager, ConfigUtils configUtils, int saveInterval) {
        super(configUtils);

        this.configsManager = configsManager;
        saveScheduler = new AutoSaveScheduler(saveInterval);
    }

    public void updateSaveInterval(int saveInterval) {
        saveScheduler.setSaveInterval(saveInterval);
    }

    @Override
    public boolean loadPlayerData(UUID uuid) {
        configsManager.loadPlayerConfig(uuid);
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        if (config == null) {
            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));
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

        // Don't try to save temporary data.
        if (data.isTemporary() || !data.isDirty()) {
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
