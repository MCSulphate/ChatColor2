package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.logging.Logger;

public class SQLUtils {

    private MainClass mc = MainClass.get();
    private Logger log = Bukkit.getLogger();
    private Connection con;
    private String uri;
    private String user;
    private String pass;

    public boolean connectToDataBase() {
        String host = mc.getConfig().getString("backend.sql.host");
        String port = mc.getConfig().getString("backend.sql.port");
        String name = mc.getConfig().getString("backend.sql.dbname");

        uri = "jdbc:mysql://" + host + ":" + port + "/" + name;
        user = mc.getConfig().getString("backend.sql.user");
        pass = mc.getConfig().getString("backend.sql.pass");

        try {
            con = DriverManager.getConnection(uri, user, pass);
        }
        catch (SQLException ex) {
            return false;
        }
        return true;
    }

    public void checkTables() {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerList(Name VARCHAR(16), UniqueID VARCHAR(36)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Color(UniqueID VARCHAR(36), Color VARCHAR(50)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS DefaultColor(Color VARCHAR(50), DefID INT");
        }
        catch (SQLException ex) {
            log.severe("SQL Database Error Creating Tables!");
            log.severe(ex.getMessage());
        }
        finally {
            try {
                if (st != null) {
                    st.close();
                }
            }
            catch (SQLException ex) {
                log.warning(ex.getMessage());
            }
        }
    }

    public String getSQLColor(String playername) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String uuid;

        try {

            pst = con.prepareStatement("SELECT Name, UniqueID FROM PlayerList WHERE Name=?");
            pst.setString(1, playername);
            rs = pst.executeQuery();
            if (rs.next()) {
                uuid = rs.getString(2);
            }
            else {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                }
                catch (SQLException ex) {
                    log.warning(ex.getMessage());
                }
                return null;
            }

            pst = con.prepareStatement("SELECT UniqueID, Color FROM Color WHERE UniqueID=?");
            pst.setString(1, uuid);
            if (rs.next()) {
                return rs.getString(2);
            }
            else {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                }
                catch (SQLException ex) {
                    log.warning(ex.getMessage());
                }
                return null;
            }

        }
        catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch (SQLException ex) {
                log.warning(ex.getMessage());
            }
        }
        return null;
    }

    public void setSQLColor(String playername) {

    }

    public Connection getConnection() {
        return con;
    }

}
