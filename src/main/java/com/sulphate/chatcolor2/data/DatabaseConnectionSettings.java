package com.sulphate.chatcolor2.data;

import org.bukkit.configuration.ConfigurationSection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DatabaseConnectionSettings {

    public static final String TABLE_NAME = "chatcolor_players";

    private final String databaseType;
    private final String address;
    private final int port;
    private final String databaseName;
    private final String databaseUser;
    private final String databasePassword;

    public DatabaseConnectionSettings(ConfigurationSection section) {
        databaseType = section.getString("type");
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
        try {
            String user = URLEncoder.encode(databaseUser, "UTF-8").replace("+", "%20");
            String password = URLEncoder.encode(databasePassword, "UTF-8").replace("+", "%20");

            return String.format("jdbc:%s://%s:%d?user=%s&password=%s", databaseType, address, port, user, password);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
