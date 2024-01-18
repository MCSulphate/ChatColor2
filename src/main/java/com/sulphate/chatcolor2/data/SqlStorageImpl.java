package com.sulphate.chatcolor2.data;

import com.sulphate.chatcolor2.utils.ConfigUtils;
import com.sulphate.chatcolor2.utils.GeneralUtils;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

import static com.sulphate.chatcolor2.data.DatabaseConnectionSettings.DATABASE_NAME;
import static com.sulphate.chatcolor2.data.DatabaseConnectionSettings.TABLE_NAME;

public class SqlStorageImpl extends PlayerDataStore {

    // TODO: Remove this! DB user password: asd35qwadas
    private Connection con;

    public SqlStorageImpl(DatabaseConnectionSettings settings, ConfigUtils configUtils) {
        super(configUtils);

        try {
            initialiseDatabase(settings);
        }
        catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            // TODO: Print error message
        }
    }

    private void initialiseDatabase(DatabaseConnectionSettings settings) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(settings.getConnectionString());

        if (!databaseExists()) {
            boolean success = con.prepareStatement("CREATE DATABASE " + DATABASE_NAME + ";").execute();

            if (!success) {
                // TODO: Print error message and shut down the plugin
                GeneralUtils.sendConsoleMessage("Cock? The database didn't create?");
                return;
            }
        }

        con.setCatalog(DATABASE_NAME);

        if (!tableExists()) {
            boolean success = con.prepareStatement(
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "UUID VARCHAR(255) PRIMARY KEY," +
                    "Colour VARCHAR(255)," +
                    "DefaultCode BIGINT" +
                ");"
            ).execute();

            if (!success) {
                // TODO: Same as above TODO
                GeneralUtils.sendConsoleMessage("Uhh the table didnt create guys thats not good please fix it rn");
                return;
            }
        }

        GeneralUtils.sendConsoleMessage("Successfully connected to SQL database.");
    }

    private boolean databaseExists() throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet results = metaData.getCatalogs();

        while (results.next()) {
            if (results.getString(1).equals(DATABASE_NAME)) {
                return true;
            }
        }

        return false;
    }

    private boolean tableExists() throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        ResultSet results = metaData.getTables(DATABASE_NAME, null, null, new String[] { "TABLE" });

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
            // TODO: Print proper error.
            ex.printStackTrace();
            return false;
        }
    }

    private void insertNewPlayer(UUID uuid) throws SQLException {
            boolean success = con.prepareStatement(
            "INSERT INTO " + TABLE_NAME + " VALUES ('" + uuid + "', '', -1);"
            ).execute();

            if (!success) {
                // TODO: Print error again...
                GeneralUtils.sendConsoleMessage("NOOOOO IT DIDNT INSERT A NEW PLAYER WTF MAN WHYY");
            }
    }

    @Override
    public boolean savePlayerData(UUID uuid) {
        PlayerData data = dataMap.get(uuid);

        try {
            int count = con.prepareStatement(
                    String.format("UPDATE %s SET UUID='%s', Colour='%s', DefaultCode=%d WHERE UUID='%s';", TABLE_NAME, uuid, data.getColour(), data.getDefaultCode(), uuid)
            ).executeUpdate();

            if (count == 0) {
                // TODO: You know the drill.
                GeneralUtils.sendConsoleMessage("Wanker! Didn't update the player as asked, even asked nicely..");
                return false;
            }
        }
        catch (SQLException ex) {
            // TODO: Yeah
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void shutdown() {
        saveAllData();

        try {
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
