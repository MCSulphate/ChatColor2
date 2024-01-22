package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class YamlStorageImpl extends PlayerDataStore {

    private final AutoSaveScheduler saveScheduler;

    private final ConfigsManager configsManager;
    private final Messages M;

    public YamlStorageImpl(ConfigsManager configsManager, ConfigUtils configUtils, int saveInterval, Messages M) {
        super(configUtils);

        this.configsManager = configsManager;
        this.M = M;
        saveScheduler = new AutoSaveScheduler(saveInterval);
    }

    public void updateSaveInterval(int saveInterval) {
        saveScheduler.setSaveInterval(saveInterval);
    }

    @Override
    public void loadPlayerData(UUID uuid, Callback<Boolean> callback) {
        configsManager.loadPlayerConfig(uuid);
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        if (config == null) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_LOAD_PLAYER_FILE);

            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));
            callback.callback(true);
            return;
        }

        dataMap.put(uuid, new PlayerData(
            uuid,
            config.getString("color"),
            config.getLong("default-code")
        ));

        callback.callback(true);
    }

    @Override
    public void savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        // Don't try to save temporary data.
        if (data.isTemporary() || !data.isDirty()) {
            return;
        }

        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        config.set("color", data.getColour());
        config.set("default-code", data.getDefaultCode());

        saveScheduler.saveConfigWithDelay("players" + File.separator + uuid + ".yml", config);
        data.markClean();
    }

    @Override
    public void shutdown() {
        saveScheduler.stop();
    }

}
