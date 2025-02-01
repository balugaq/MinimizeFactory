package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;

public class ColumnRemover<InType> {
    private final String removeStatement;
    private final PreparedStatementValueSetter<InType> keySetter;
    public ColumnRemover(
            String keyName,
            PreparedStatementValueSetter<InType> keySetter
    ){
        this.removeStatement = "DELETE FROM "+TABLE_NAME+" WHERE "+keyName+"=?";
        this.keySetter = keySetter;
    }
    public boolean remove(InType key){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        ResultSet rs = null;
        Connection conn = databaseInstance.getSQLConnection();
        try (
             PreparedStatement pstmt = conn.prepareStatement(removeStatement)
        ){
            keySetter.setValue(pstmt,1,key);
            pstmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
