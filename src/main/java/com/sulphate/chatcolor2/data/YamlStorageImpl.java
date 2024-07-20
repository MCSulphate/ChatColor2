package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.commands.Setting;
import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.managers.ConfigsManager;
import com.sulphate.chatcolor2.schedulers.AutoSaveScheduler;
import com.sulphate.chatcolor2.utils.Config;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class YamlStorageImpl extends PlayerDataStore {

    private final AutoSaveScheduler saveScheduler;

    private final ConfigsManager configsManager;
    private final Messages M;

    public YamlStorageImpl(ConfigsManager configsManager, int saveInterval, Messages M) {
        super();

        this.configsManager = configsManager;
        this.M = M;
        saveScheduler = new AutoSaveScheduler(saveInterval);

        File playersFolder = new File(ChatColor.getPlugin().getDataFolder(), "players");

        if (playersFolder.mkdirs()) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + "Created player data folder.");
        }
    }

    public void updateSaveInterval(int saveInterval) {
        saveScheduler.setSaveInterval(saveInterval);
    }

    @Override
    public void loadPlayerData(UUID uuid, Callback<Boolean> callback) {
        if (dataMap.containsKey(uuid)) {
            callback.callback(true);
            return;
        }

        configsManager.loadPlayerConfig(uuid);
        YamlConfiguration config = configsManager.getPlayerConfig(uuid);

        if (config == null) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_LOAD_PLAYER_FILE);

            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));
            callback.callback(true);
            return;
        }
        // New player joined! Values will be updated on chat, if applicable.
        else if (!config.contains("color")) {
            config.set("color", "");
            config.set("default-code", -1);
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
