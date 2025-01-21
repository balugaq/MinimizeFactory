package io.github.ignorelicensescn.minimizeFactory.datastorage.database;
import io.github.ignorelicensescn.minimizeFactory.MinimizeFactory;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.types.Column;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.logger;
import static io.github.ignorelicensescn.minimizeFactory.datastorage.database.types.Column.KEY_COLUMN;

/**
 * <a href="https://www.spigotmc.org/threads/how-to-sqlite.56847/">from here</a>
 * <p>because</p>
 * <a href="https://github.com/SlimefunGuguProject/Slimefun4">Slimefun Unofficial Translate Team(do they have a english name?)</a>
 * <p>changed BlockStorage.I have to make some changes.Options are:</p>
 * <p>A.endless compatibilities and compile two plugins.</p>
 * <p>B.create new Data storage system</p>
 * <p>I choose B.</p>
 * <p>我才不做兼容</p>
 */
public class SQLiteBlockDataStorageManager extends Database{
    public final String dbname;
    public static final String TABLE_NAME = "minimizefactory_database";
    public SQLiteBlockDataStorageManager(MinimizeFactory instance){
        super(instance);
        dbname = plugin.getConfig().getString("minimizefactory_database_name", "minimizefactory_database"); // Set the table name here e.g player_kills
    }

    public static String SQLiteCreateTokensTable = null;
    public static void initializeSQLiteCreateTokensTable(){
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (");
        for (Column c:Column.values()){
            sb.append(c.columnStringForInitialize(","));
        }
        sb.append("PRIMARY KEY (`").append(KEY_COLUMN.columnInnerName).append("`)")
                .append(");");
        SQLiteCreateTokensTable = sb.toString();
//        what we want↓
//        SQLiteCreateTokensTable =
//            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
//            "`"+KEY_LOCATION+"` VARCHAR(80) NOT NULL," +
//            "`"+ VALUE_NODE_TYPE_INT +"` INTEGER(5)," +
//            "`"+VALUE_DATA_BLOB+"` BLOB," +
//            "`"+VALUE_CORE_LOCATION_VARCHAR+"` VARCHAR(80)," +
//            "`"+ VALUE_LOCK_STATUS_BOOLEAN +"` BOOLEAN," +
//            "PRIMARY KEY (`"+KEY_LOCATION+"`)" +  Here we want to use player so
//            ");";
    }
    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        if (SQLiteCreateTokensTable == null){
            initializeSQLiteCreateTokensTable();
        }
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCoreLocation(SerializeFriendlyBlockLocation location, SerializeFriendlyBlockLocation coreLocation){
        String updateSQL = "UPDATE "+TABLE_NAME+" "
                + "SET "+ VALUE_CORE_LOCATION_VARCHAR +" = ? "
                + "WHERE "+KEY_LOCATION+"=?";
        try (Connection conn = getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            // set parameters
            pstmt.setString(1,coreLocation.toString());
            pstmt.setString(2, location.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public SerializeFriendlyBlockLocation getCoreLocation(SerializeFriendlyBlockLocation location){
        String selectSQL = "SELECT "+ VALUE_CORE_LOCATION_VARCHAR +" FROM "+TABLE_NAME+" WHERE "+KEY_LOCATION+"=?";
        ResultSet rs = null;
        try (Connection conn = getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        ){
            pstmt.setString(1, location.toString());
            rs = pstmt.executeQuery();
            return SerializeFriendlyBlockLocation.fromString(rs.getString(VALUE_CORE_LOCATION_VARCHAR));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setLockStatus(SerializeFriendlyBlockLocation location, boolean status){
        String updateSQL = "UPDATE "+TABLE_NAME+" "
                + "SET "+ VALUE_CORE_LOCATION_VARCHAR +" = ? "
                + "WHERE "+KEY_LOCATION+"=?";
        try (Connection conn = getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            // set parameters
            pstmt.setBoolean(1, status);
            pstmt.setString(2, location.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Boolean getLockStatus(SerializeFriendlyBlockLocation location){
        String selectSQL = "SELECT "+ VALUE_LOCK_STATUS_BOOLEAN +" FROM "+TABLE_NAME+" WHERE "+KEY_LOCATION+"=?";
        ResultSet rs = null;
        try (Connection conn = getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        ){
            pstmt.setString(1, location.toString());
            rs = pstmt.executeQuery();
            return rs.getBoolean(VALUE_LOCK_STATUS_BOOLEAN);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
