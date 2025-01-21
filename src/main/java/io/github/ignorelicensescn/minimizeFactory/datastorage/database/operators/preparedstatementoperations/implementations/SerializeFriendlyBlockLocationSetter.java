package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SerializeFriendlyBlockLocationSetter implements PreparedStatementValueSetter<SerializeFriendlyBlockLocation> {
    public static final SerializeFriendlyBlockLocationSetter INSTANCE = new SerializeFriendlyBlockLocationSetter();
    private SerializeFriendlyBlockLocationSetter(){};

    @Override
    public void setValue(PreparedStatement statement, int paramIndex, SerializeFriendlyBlockLocation in) throws SQLException {
        statement.setString(paramIndex,in.toString());
    }
}
