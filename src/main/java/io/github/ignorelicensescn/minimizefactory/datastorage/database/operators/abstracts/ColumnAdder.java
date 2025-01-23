package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;

public class ColumnAdder<InType> {
    private final String newRowStatement;
    private final String checkRowStatement;
    private final PreparedStatementValueSetter<InType> keySetter;
    public ColumnAdder(
            String keyName,
            PreparedStatementValueSetter<InType> keySetter
    ){
        this.newRowStatement = "INSERT INTO " + TABLE_NAME + "(" + KEY_COLUMN.columnInnerName + ") VALUES (?)";
        this.checkRowStatement = "SELECT * FROM " + TABLE_NAME + " WHERE " + keyName + "=?";
        this.keySetter = keySetter;
    }
    public boolean checkExistence(InType key){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        ResultSet rs = null;
        Connection conn = databaseInstance.getSQLConnection();
        try (
             PreparedStatement pstmt = conn.prepareStatement(checkRowStatement);
        ){
            keySetter.setValue(pstmt,1,key);
            rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
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
        return false;
    }
    public boolean addIfNotExist(InType key){
        if (!checkExistence(key)){
            return add(key);
        }
        return false;
    }
    public boolean add(InType key){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        ResultSet rs = null;
        Connection conn = databaseInstance.getSQLConnection();
        try (
             PreparedStatement pstmt = conn.prepareStatement(newRowStatement);
        ){
            keySetter.setValue(pstmt,1,key);
            pstmt.execute();
            return true;
        } catch (Exception e) {
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
        return false;
    }
}
