package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators;

import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizeFactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;

//TODO:Rewrite machine initialization (with this).
public class LocationBasedMultiValueOperator {

    public final String updateSQL;
    public final String selectSQL;
    public final String keyName;
    public final OperateItem[] operateItems;
    public LocationBasedMultiValueOperator(
            String keyName,
            OperateItem[] operateItems
    ){
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

    public void set(SerializeFriendlyBlockLocation location,Object[] toSet){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            for (int i=0;i<operateItems.length;i++){
                OperateItem operateItem = operateItems[i];
                operateItem.setter().setValue(pstmt,i+1,toSet[i]);
            }
            pstmt.setString(operateItems.length + 1, location.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };
    @Nullable
    public Object[] get(SerializeFriendlyBlockLocation location){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        Object[] result = new Object[operateItems.length];
        Arrays.fill(result,null);

        ResultSet rs = null;
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        ){
            pstmt.setString(1, location.toString());
            rs = pstmt.executeQuery();

            for (int i=0;i<operateItems.length;i++){
                OperateItem<?,?> operateItem = operateItems[i];
                result[i] = operateItem.getter().getValue(rs,operateItem.valueName());
            }
            return result;
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
    };
}
