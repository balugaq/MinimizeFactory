package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerializeFriendlyBlockLocationGetter implements PreparedStatementValueGetter<SerializeFriendlyBlockLocation> {
    public static final SerializeFriendlyBlockLocationGetter INSTANCE = new SerializeFriendlyBlockLocationGetter();
    private SerializeFriendlyBlockLocationGetter(){};
    @Override
    public SerializeFriendlyBlockLocation getValue(ResultSet rs, String valueName) throws SQLException {
        return SerializeFriendlyBlockLocation.fromString(rs.getString(valueName));
    }
}
