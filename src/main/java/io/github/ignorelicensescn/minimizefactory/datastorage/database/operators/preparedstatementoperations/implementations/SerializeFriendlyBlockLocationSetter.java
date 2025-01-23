package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class SerializeFriendlyBlockLocationSetter implements PreparedStatementValueSetter<SerializeFriendlyBlockLocation> {
    public static final SerializeFriendlyBlockLocationSetter INSTANCE = new SerializeFriendlyBlockLocationSetter();
    private SerializeFriendlyBlockLocationSetter(){}

    @Override
    public void setValue(PreparedStatement statement, int paramIndex,@Nullable SerializeFriendlyBlockLocation in) throws SQLException {
        if (in == null){
            statement.setNull(paramIndex, Types.VARCHAR);
        }else {
            statement.setString(paramIndex,in.toString());
        }
    }
}
