package com.sulphate.chatcolor2.data;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseConnectionSettings {

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

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

}
