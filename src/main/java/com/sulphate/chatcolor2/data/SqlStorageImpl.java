package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.main.ChatColor;
import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

import static com.sulphate.chatcolor2.data.DatabaseConnectionSettings.TABLE_NAME;

public class SqlStorageImpl extends PlayerDataStore {

    private final DatabaseConnectionSettings settings;
    private final Messages M;

    private Connection con;

    public SqlStorageImpl(DatabaseConnectionSettings settings, ConfigUtils configUtils, Messages M) {
        super(configUtils);

        this.settings = settings;
        this.M = M;


        try {
            boolean success = initialiseDatabase();

            if (!success) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_INITIALISE_DB);
            }
        }
        catch (ClassNotFoundException | SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_INITIALISE_DB + " Error: " + ex.getMessage());
            tryCloseConnection();
        }
    }

    private void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(ChatColor.getPlugin(), runnable);
    }

    private void tryCloseConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                con = null;
            }
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CLOSE_CONNECTION.replace("[error]", ex.getMessage()));
        }
    }

    private boolean initialiseDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.sulphate.chatcolor2.lib.com.mysql.cj.jdbc.Driver");
        String databaseName = settings.getDatabaseName();

        try {
            con = DriverManager.getConnection(settings.getConnectionString());
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CONNECT_TO_DB.replace("[error]", ex.getMessage()));
            con = null;
            return false;
        }

        try {
            if (!databaseExists()) {
                con.prepareStatement("CREATE DATABASE " + databaseName + ";").executeUpdate();
            }
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CREATE_DB.replace("[error]", ex.getMessage()));
            tryCloseConnection();
            return false;
        }

        try {
            con.setCatalog(databaseName);

            if (!tableExists()) {
                con.prepareStatement(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        "UUID VARCHAR(255) PRIMARY KEY," +
                        "Colour VARCHAR(255)," +
                        "DefaultCode BIGINT" +
                    ");"
                ).executeUpdate();
            }

            GeneralUtils.sendConsoleMessage(M.PREFIX + M.DB_INITIALISED_SUCCESSFULLY);
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CREATE_TABLE.replace("[error]", ex.getMessage()));
            tryCloseConnection();
            return false;
        }

        return true;
    }

    private boolean databaseExists() throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet results = metaData.getCatalogs();

        while (results.next()) {
            if (results.getString(1).equals(settings.getDatabaseName())) {
                return true;
            }
        }

        return false;
    }

    private boolean tableExists() throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet results = metaData.getTables(settings.getDatabaseName(), null, null, new String[] { "TABLE" });

        while (results.next()) {
            String name = results.getString("TABLE_NAME");

            if (name.equals(TABLE_NAME)) {
                return true;
            }
        }

        return false;
    }

    // The return value passed to the callback is not whether the fetch was successful, but if there is *some* data
    // that can now be used for the player, including temporary data. It only returns false if the database has not
    // been initialised.
    @Override
    public void loadPlayerData(UUID uuid, Callback<Boolean> callback) {
        // If the connection failed, use temporary data to prevent further errors.
        if (con == null) {
            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));
            callback.callback(true);
            return;
        }

        runAsync(() -> {
            try {
                ResultSet results = con.prepareStatement(
                        "SELECT * FROM " + TABLE_NAME + " WHERE UUID='" + uuid + "';"
                ).executeQuery();

                if (results == null || !results.next()) {
                    dataMap.put(uuid, new PlayerData(uuid, null, -1));
                    insertNewPlayer(uuid);
                }
                else {
                    // Note that we don't call results.next() here as we expect one result and we already called it to check
                    // that there is a result^^.
                    String colour = results.getString("Colour");
                    long defaultCode = results.getLong("DefaultCode");

                    dataMap.put(uuid, new PlayerData(uuid, colour, defaultCode));
                }

                callback.callback(true);
            }
            catch (SQLException ex) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_LOAD_PLAYER_DATA.replace("[error]", ex.getMessage()));
                dataMap.put(uuid, PlayerData.createTemporaryData(uuid));

                callback.callback(true);
            }
        });
    }

    private void insertNewPlayer(UUID uuid) throws SQLException {
            boolean success = con.prepareStatement(
            "INSERT INTO " + TABLE_NAME + " VALUES ('" + uuid + "', '', -1);"
            ).execute();

            if (!success) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CREATE_NEW_PLAYER.replace("[player]", uuid.toString()));
            }
    }

    @Override
    public void savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        if (data.isTemporary() || !data.isDirty()) {
            return;
        }

        data.markClean();

        runAsync(() -> {
            try {
                int count = con.prepareStatement(
                        String.format("UPDATE %s SET UUID='%s', Colour='%s', DefaultCode=%d WHERE UUID='%s';", TABLE_NAME, uuid, data.getColour(), data.getDefaultCode(), uuid)
                ).executeUpdate();

                if (count == 0) {
                    GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_SAVE_PLAYER_DATA.replace("[error]", "No player data found to update."));
                }
            }
            catch (SQLException ex) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_SAVE_PLAYER_DATA.replace("[error]", ex.getMessage()));
            }
        });
    }

    // We don't need to save data to the db as this is done immediately when updating a player.
    @Override
    public void shutdown() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CLOSE_CONNECTION.replace("[error]", ex.getMessage()));
            }
        }
    }

}
