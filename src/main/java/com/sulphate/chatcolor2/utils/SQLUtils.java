package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.logging.Logger;

public class SQLUtils {

    private MainClass mc = MainClass.get();
    private Logger log = Bukkit.getLogger();

    public void checkTables() {

        Connection con = null;
        PreparedStatement pst = null;
        Statement st = null;
        ResultSet rs = null;

        String host = mc.getConfig().getString("backend.sql.host");
        String port = mc.getConfig().getString("backend.sql.port");
        String name = mc.getConfig().getString("backend.sql.dbname");

        String uri = "jdbc:mysql://" + host + ":" + port + "/" + name;
        String user = mc.getConfig().getString("backend.sql.user");
        String pass = mc.getConfig().getString("backend.sql.pass");

        try {
            con = DriverManager.getConnection(uri, user, pass);
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerList(Name VARCHAR(16), UniqueID VARCHAR")
        }
        catch (SQLException ex) {

        }
        finally {

        }

    }

}
