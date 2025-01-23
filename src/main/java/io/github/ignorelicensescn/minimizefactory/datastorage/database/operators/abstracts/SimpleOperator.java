package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;

@ParametersAreNonnullByDefault
public abstract class SimpleOperator<InType,OutType,KeyType> {

    private final String updateSQLStatement;
    private final String selectSQLStatement;
//    private final String keyName;
    private final PreparedStatementValueSetter<KeyType> keySetter;
    private final String valueName;
    private final PreparedStatementValueSetter<InType> setter;
    private final ResultSetValueGetter<OutType> getter;
    public SimpleOperator(
            String keyName,
            OperateItem<InType,OutType> operateItem,
            PreparedStatementValueSetter<KeyType> keySetter
            ){
        this.keySetter = keySetter;

//        this.keyName = keyName;
        this.valueName = operateItem.valueName();
        this.setter = operateItem.setter();
        this.getter = operateItem.getter();
        updateSQLStatement = "UPDATE "+TABLE_NAME+" "
                + "SET "
                + valueName + " = ? "
                + "WHERE "+keyName+"=?";
        selectSQLStatement = "SELECT "+valueName+" FROM "+TABLE_NAME+" WHERE "+keyName+"=?";
    }


    public void set(KeyType key,@Nullable InType in){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQLStatement)) {

            // set parameters
            setter.setValue(pstmt,1,in);
            keySetter.setValue(pstmt,2,key);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    @Nullable
    public OutType get(KeyType key){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        ResultSet rs = null;
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQLStatement);
        ){
            keySetter.setValue(pstmt,1,key);
            rs = pstmt.executeQuery();
            if (rs.next()){
                return getter.getValue(rs,valueName);
            }
            return null;
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
        return null;
    };

    public boolean has(KeyType key){
        return get(key) != null;
    }
}
