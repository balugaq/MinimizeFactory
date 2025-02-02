package io.github.ignorelicensescn.minimizefactory.datastorage.database;
import io.github.ignorelicensescn.minimizefactory.MinimizeFactory;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;

/**
 * <a href="https://www.spigotmc.org/threads/how-to-sqlite.56847/">from here</a>
 * <p>because</p>
 * <a href="https://github.com/SlimefunGuguProject/Slimefun4">An unofficial Slimefun translate team(do they have a english name?)</a>
 * <p>changed BlockStorage.I have to make some changes.Options are:</p>
 * <p>A.endless compatibilities and compile two plugins.</p>
 * <p>B.create new Data storage system</p>
 * <p>I choose B.</p>
 * <p>我才不做兼容</p>
 */
public class SQLiteBlockDataStorageManager extends Database{
    public final String dbname;
    public static final String TABLE_NAME = "minimizefactory_machine_database";
    public SQLiteBlockDataStorageManager(MinimizeFactory instance){
        super(instance);
        dbname = plugin.getConfig().getString("minimizefactory_database_name", "minimizefactory_database"); // Set the table name here e.g player_kills
    }

    public static String SQLiteCreateTokensTableStatement = null;
    public static void initializeSQLiteCreateTokensTable(){
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (");
        for (Column c:Column.values()){
            sb.append(c.columnStringForInitialize(","));
        }
        sb.append("PRIMARY KEY (`").append(KEY_COLUMN.columnInnerName).append("`)")
                .append(");");
        SQLiteCreateTokensTableStatement = sb.toString();

//        what we want↓
//        SQLiteCreateTokensTable =
//            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
//            "`"+KEY_LOCATION+"` VARCHAR(80) NOT NULL," +
//            "`"+ VALUE_NODE_TYPE_INT +"` INTEGER(5)," +
//            "`"+VALUE_DATA_BLOB+"` BLOB," +
//            "`"+VALUE_CORE_LOCATION_VARCHAR+"` VARCHAR(80)," +
//            "`"+ VALUE_LOCK_STATUS_BOOLEAN +"` BOOLEAN," +
//            "PRIMARY KEY (`"+KEY_LOCATION+"`)" +
//            ");";
    }
    // SQL creation stuff, You can leave the blow stuff untouched.
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
        if (!dataFolder.exists()){
            try {
                if (!dataFolder.createNewFile()){
                    throw new IOException("failed to create database file!" + dataFolder.getCanonicalPath());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
            }
        }
        try {
            if(connection!=null
                    &&!connection.isClosed()){
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
        if (SQLiteCreateTokensTableStatement == null){
            initializeSQLiteCreateTokensTable();
        }
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTableStatement);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
