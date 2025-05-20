package com.sulphate.chatcolor2.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.mariadb.jdbc.Driver;
import org.mariadb.jdbc.plugin.AuthenticationPlugin;

import java.util.Properties;
import java.util.ServiceLoader;

import javax.sql.DataSource;

public class DatabaseConnectionSettings {

    public static final String TABLE_NAME = "chatcolor_players";
    private static final int MAX_CONNECTIONS = 10;

    private final String address;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public DatabaseConnectionSettings(ConfigurationSection section) {
        address = section.getString("address");
        port = section.getInt("port");
        database = section.getString("name");
        user = section.getString("user");
        password = section.getString("password");
    }

    public String getDatabaseName() {
        return database;
    }

    private Properties getDatabaseProperties() {
        Properties properties = new Properties();
        String url = String.format("jdbc:mariadb://%s:%d/%s", address, port, database);

        properties.setProperty("dataSourceClassName", "com.sulphate.chatcolor2.lib.org.mariadb.jdbc.MariaDbDataSource");
        properties.setProperty("dataSource.url", url);
        properties.setProperty("dataSource.user", user);
        properties.setProperty("dataSource.password", password);

        return properties;
    }

    public DataSource createDataSource() {
        HikariConfig config = new HikariConfig(getDatabaseProperties());
        config.setMaximumPoolSize(MAX_CONNECTIONS);

        return new HikariDataSource(config);
    }

}
