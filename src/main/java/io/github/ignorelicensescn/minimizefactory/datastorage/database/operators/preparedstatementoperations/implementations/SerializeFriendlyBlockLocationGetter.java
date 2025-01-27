package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerializeFriendlyBlockLocationGetter implements ResultSetValueGetter<SerializeFriendlyBlockLocation> {
    public static final SerializeFriendlyBlockLocationGetter INSTANCE = new SerializeFriendlyBlockLocationGetter();
    private SerializeFriendlyBlockLocationGetter(){}

    @Override
    public SerializeFriendlyBlockLocation getValue(ResultSet rs, String valueName) throws SQLException {
        String locationString = rs.getString(valueName);
        if (locationString == null){return null;}
        return SerializeFriendlyBlockLocation.fromString(rs.getString(valueName));
    }
}
