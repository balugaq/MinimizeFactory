package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.databaseInstance;
import static io.github.ignorelicensescn.minimizeFactory.MinimizeFactory.minimizeFactoryInstance;
import static io.github.ignorelicensescn.minimizeFactory.datastorage.database.SQLiteBlockDataStorageManager.TABLE_NAME;

@ParametersAreNonnullByDefault
public abstract class LocationBasedSimpleOperator<InType,OutType> {

    public final String updateSQL;
    public final String selectSQL;
    public final String keyName;
    public final String valueName;
    public final PreparedStatementValueSetter<InType> setter;
    public final PreparedStatementValueGetter<OutType> getter;
    public LocationBasedSimpleOperator(
            String keyName,
            OperateItem<InType,OutType> operateItem
            ){
        this.keyName = keyName;
        this.valueName = operateItem.valueName();
        this.setter = operateItem.setter();
        this.getter = operateItem.getter();
        updateSQL = "UPDATE "+TABLE_NAME+" "
                + "SET "
                + valueName + " = ? "
                + "WHERE "+keyName+"=?";
        selectSQL = "SELECT "+valueName+" FROM "+TABLE_NAME+" WHERE "+keyName+"=?";
    }


    public void set(SerializeFriendlyBlockLocation location, InType in){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {

            // set parameters
            setter.setValue(pstmt,1,in);
            pstmt.setString(2, location.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    };
    @Nullable
    public OutType get(SerializeFriendlyBlockLocation location){
        if (databaseInstance == null){
            minimizeFactoryInstance.getDatabaseInstance();
        }
        ResultSet rs = null;
        try (Connection conn = databaseInstance.getSQLConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        ){
            pstmt.setString(1, location.toString());
            rs = pstmt.executeQuery();
            return getter.getValue(rs,valueName);
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
