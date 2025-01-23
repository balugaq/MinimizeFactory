package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizefactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;

//TODO:Rewrite machine initialization (with this).
public abstract class MultiValueOperator<KeyType> {

    public final String updateSQL;
    public final String selectSQL;
    public final String keyName;
    public final OperateItem[] operateItems;
    public final PreparedStatementValueSetter<KeyType> keySetter;
    public MultiValueOperator(
            String keyName,
            OperateItem[] operateItems,
            PreparedStatementValueSetter<KeyType> keySetter
    ){
        this.keySetter = keySetter;
        this.operateItems = operateItems;
        this.keyName = keyName;
        StringBuilder tempUpdateSQLSB = new StringBuilder("UPDATE "+TABLE_NAME+" "
                + "SET ");
        StringBuilder tempSelectSQLSB = new StringBuilder(
                "SELECT "
        );
        boolean first = true;
        for (OperateItem<?,?> operateItem:operateItems){
            tempUpdateSQLSB.append(operateItem.valueName()).append(" = ? ");
            if (!first){
                tempSelectSQLSB.append(",");
            }
            first = false;
            tempSelectSQLSB.append(operateItem);
        }
        tempUpdateSQLSB.append("WHERE ").append(keyName).append("=?");
        tempSelectSQLSB.append(" FROM " + TABLE_NAME + " WHERE ").append(keyName).append("=?");

        updateSQL = tempUpdateSQLSB.toString();
        selectSQL = tempSelectSQLSB.toString();
    }

    public void set(KeyType key,Object[] toSet){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        Connection conn = databaseInstance.getSQLConnection();
        try (
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            for (int i=0;i<operateItems.length;i++){
                OperateItem operateItem = operateItems[i];
                operateItem.setter().setValue(pstmt,i+1,toSet[i]);
            }
            keySetter.setValue(pstmt,operateItems.length + 1,key);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };
    @Nullable
    public Object[] get(KeyType key){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        Object[] result = new Object[operateItems.length];
        Arrays.fill(result,null);

        ResultSet rs = null;
        Connection conn = databaseInstance.getSQLConnection();
        try (
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        ){
            keySetter.setValue(pstmt,1, key);
            rs = pstmt.executeQuery();

            if (rs.next()){
                for (int i=0;i<operateItems.length;i++){
                    OperateItem<?,?> operateItem = operateItems[i];
                    result[i] = operateItem.getter().getValue(rs,operateItem.valueName());
                }
                return result;
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
}
