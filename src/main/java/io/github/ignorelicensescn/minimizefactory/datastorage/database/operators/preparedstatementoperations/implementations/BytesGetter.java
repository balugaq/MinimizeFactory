package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BytesGetter implements ResultSetValueGetter<byte[]> {
    public static final BytesGetter INSTANCE = new BytesGetter();
    private BytesGetter(){}
    @Override
    public byte[] getValue(ResultSet rs,String valueName) throws SQLException {
        return rs.getBytes(valueName);
    }
}
