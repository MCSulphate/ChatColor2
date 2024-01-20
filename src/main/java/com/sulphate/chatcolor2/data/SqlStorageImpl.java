package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import com.sulphate.chatcolor2.utils.Messages;

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
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_INITIALISE_DB);

            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                    con = null;
                }
            }
            catch (SQLException sqlEx) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CLOSE_CONNECTION.replace("[error]", sqlEx.getMessage()));
            }
        }
    }

    private boolean initialiseDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String databaseName = settings.getDatabaseName();

        try {
            con = DriverManager.getConnection(settings.getConnectionString());
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CONNECT_TO_DB);
            con = null;
            return false;
        }

        if (!databaseExists()) {
            boolean success = con.prepareStatement("CREATE DATABASE " + databaseName + ";").execute();

            if (!success) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CREATE_DB);

                con.close();
                con = null;

                return false;
            }
        }

        con.setCatalog(databaseName);

        if (!tableExists()) {
            boolean success = con.prepareStatement(
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "UUID VARCHAR(255) PRIMARY KEY," +
                    "Colour VARCHAR(255)," +
                    "DefaultCode BIGINT" +
                ");"
            ).execute();

            if (!success) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CREATE_TABLE);

                con.close();
                con = null;

                return false;
            }
        }

        GeneralUtils.sendConsoleMessage(M.PREFIX + M.DB_INITIALISED_SUCCESSFULLY);
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

    @Override
    public boolean loadPlayerData(UUID uuid) {
        // If the connection failed, use temporary data to prevent further errors.
        if (con == null) {
            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));
            return true;
        }

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

            return true;
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_LOAD_PLAYER_DATA.replace("[error]", ex.getMessage()));
            dataMap.put(uuid, PlayerData.createTemporaryData(uuid));

            return false;
        }
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
    public boolean savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        if (data.isTemporary() || !data.isDirty()) {
            return true;
        }

        try {
            int count = con.prepareStatement(
                String.format("UPDATE %s SET UUID='%s', Colour='%s', DefaultCode=%d WHERE UUID='%s';", TABLE_NAME, uuid, data.getColour(), data.getDefaultCode(), uuid)
            ).executeUpdate();

            if (count == 0) {
                GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_SAVE_PLAYER_DATA.replace("[error]", "No player data found to update."));
                return false;
            }
        }
        catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_SAVE_PLAYER_DATA.replace("[error]", ex.getMessage()));
            return false;
        }

        data.markClean();

        return true;
    }

    @Override
    public void shutdown() {
        saveAllData();

        try {
            con.close();
        } catch (SQLException ex) {
            GeneralUtils.sendConsoleMessage(M.PREFIX + M.FAILED_TO_CLOSE_CONNECTION.replace("[error]", ex.getMessage()));
        }
    }

}
