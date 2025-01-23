package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanGetter implements ResultSetValueGetter<Boolean> {
    public static final BooleanGetter INSTANCE = new BooleanGetter();
    private BooleanGetter(){}
    @Override
    public Boolean getValue(ResultSet rs, String valueName) throws SQLException {
        return rs.getBoolean(valueName);
    }
}
