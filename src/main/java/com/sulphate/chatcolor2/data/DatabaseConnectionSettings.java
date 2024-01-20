package com.sulphate.chatcolor2.data;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseConnectionSettings {

    public static final String TABLE_NAME = "players";

    private final String address;
    private final int port;
    private final String databaseName;
    private final String databaseUser;
    private final String databasePassword;

    public DatabaseConnectionSettings(ConfigurationSection section) {
        address = section.getString("address");
        port = section.getInt("port");
        databaseName = section.getString("name");
        databaseUser = section.getString("user");
        databasePassword = section.getString("password");
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getConnectionString() {
        return String.format("jdbc:mysql://%s:%d?user=%s&password=%s", address, port, databaseUser, databasePassword);
    }

}
