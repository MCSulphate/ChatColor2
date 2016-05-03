package com.sulphate.chatcolor2.utils;

import com.sulphate.chatcolor2.main.MainClass;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SQLUtils {

    private MainClass mc = MainClass.get();
    private Logger log = Bukkit.getLogger();
    private Connection con;
    private String uri;
    private String user;
    private String pass;
    private ConcurrentHashMap<String, String> colorcache = new ConcurrentHashMap<>();
    private String[] defaultdata = new String[2];

    public boolean connectToDataBase() {
        String host = mc.getConfig().getString("backend.sql.host");
        String port = mc.getConfig().getString("backend.sql.port");
        String name = mc.getConfig().getString("backend.sql.dbname");

        uri = "jdbc:mysql://" + host + ":" + port + "/" + name;
        user = mc.getConfig().getString("backend.sql.user");
        pass = mc.getConfig().getString("backend.sql.pass");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            log.severe(ex.getMessage());
            return false;
        }
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
            st.executeUpdate("CREATE TABLE IF NOT EXISTS PlayerList(Name VARCHAR(16), UniqueID VARCHAR(36))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS Colors(UniqueID VARCHAR(36), Color VARCHAR(50))");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS DefaultColor(Color VARCHAR(50), DefID VARCHAR(6)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS DefaultColorList(String UniqueID, DefID VARCHAR(6)");
            st.executeUpdate("ALTER TABLE Colors ADD UNIQUE(UniqueID)");
            st.executeUpdate("ALTER TABLE DefaultColorList ADD UNIQUE(UniqueID)");
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

    public void updatePlayer(String playername, String uuid) {
        checkTables();
        PreparedStatement pst = null;

        try {
            if (getPlayerName(uuid) != null && getUUID(playername) == null) {
                pst = con.prepareStatement("DELETE FROM PlayerList WHERE UniqueID=?");
                pst.setString(1, uuid);
                pst.executeUpdate();
                pst = con.prepareStatement("INSERT INTO PlayerList(Name, UniqueID) VALUES(?, ?)");
                pst.setString(1, playername);
                pst.setString(2, uuid);
                pst.executeUpdate();
            }
            else if (getUUID(playername) == null) {
                pst = con.prepareStatement("INSERT INTO PlayerList(Name, UniqueID) VALUES(?, ?)");
                pst.setString(1, playername);
                pst.setString(2, uuid);
                pst.executeUpdate();
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
            }
            catch (SQLException ex) {
                log.warning(ex.getMessage());
            }
        }
    }

    public String getPlayerName(String uuid) {
        checkTables();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = null;

        try {
            pst = con.prepareStatement("SELECT Name FROM PlayerList WHERE UniqueID=?");
            pst.setString(1, uuid);
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
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
        return result;
    }

    public String getUUID(String playername) {
        checkTables();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = null;

        try {
            pst = con.prepareStatement("SELECT UniqueID FROM PlayerList WHERE Name=?");
            pst.setString(1, playername);
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
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
        return result;
    }

    public String getSQLColor(String playername, boolean forceupdate) {
        checkTables();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String uuid = getUUID(playername);
        String result = null;

        if (isCached(playername) && !forceupdate) {
            return colorcache.get(playername);
        }

        try {

            pst = con.prepareStatement("SELECT UniqueID, Color FROM Colors WHERE UniqueID=?");
            pst.setString(1, uuid);
            if (rs.next()) {
                result = rs.getString(2);
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
        if (!isCached(playername) || forceupdate) {
            if (isCached(playername)) {
                colorcache.remove(playername);
            }
            colorcache.put(playername, result);
        }
        return result;
    }

    public void setSQLColor(String playername, String color, boolean removefromcache) {
        checkTables();
        PreparedStatement pst = null;
        String uuid = getUUID(playername);
        try {
            pst = con.prepareStatement("INSERT INTO Colors(UniqueID, Color) VALUES(?, ?) ON DUPLICATE KEY UPDATE UniqueID=VALUES(UniqueID), Color=VALUES(Color)");
            pst.setString(1, uuid);
            pst.setString(2, color);
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            }
            catch (SQLException ex) {
                log.warning(ex.getMessage());
            }
        }
        if (removefromcache && isCached(playername)) {
            colorcache.remove(playername);
        }
    }

    public void checkDefaultSQLColor(String playername) {

    }

    public String getSQLDefaultColorOrCode(boolean color, boolean forceupdate) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = null;

        if (defaultdata[0] != null && !forceupdate) {
            if (color) {
                return defaultdata[1];
            }
            return defaultdata[0];
        }

        try {
            pst = con.prepareStatement("SELECT Color, DefID FROM DefaultColor");
            rs = pst.executeQuery();
            if (rs.next()) {
                defaultdata[0] = rs.getString(2);
                defaultdata[1] = rs.getString(1);
                if (color) {
                    result = rs.getString(1);
                }
                else {
                    result = rs.getString(2);
                }
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
        return result;
    }

    public void setSQLDefaultColor(String code, String color) {
        PreparedStatement pst = null;

        try {
            if (getSQLDefaultColorOrCode(false, true) != null) {
                pst = con.prepareStatement("DELETE FROM DefaultCode WHERE DefID=?");
                pst.setString(1, getSQLDefaultColorOrCode(false, true));
                pst.executeUpdate();
            }
            pst = con.prepareStatement("INSERT INTO DefaultColor(DefID, Color) VALUES(?, ?)");
            pst.setString(1, code);
            pst.setString(2, color);
            pst.executeUpdate();
        }
        catch (SQLException ex) {
            log.severe(ex.getMessage());
        }
        finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            }
            catch (SQLException ex) {
                log.warning(ex.getMessage());
            }
        }
    }

    public String getSQLPlayerDefaultColorCode(String playername) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String result = null;
        String uuid = getUUID(playername);

        try {
            pst = con.prepareStatement("SELECT DefID FROM DefaultColorList WHERE UniqueID=?");
            pst.setString(1, uuid);
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
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
        return result;
    }

    public boolean isCached(String playername) {
        if (colorcache.containsKey(playername)) {
            return true;
        }
        return false;
    }

    public Connection getConnection() {
        return con;
    }

}
