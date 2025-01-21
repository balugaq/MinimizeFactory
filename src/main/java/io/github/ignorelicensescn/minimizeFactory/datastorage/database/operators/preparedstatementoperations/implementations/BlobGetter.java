package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobGetter implements PreparedStatementValueGetter<Blob> {
    public static final BlobGetter INSTANCE = new BlobGetter();
    private BlobGetter(){}
    @Override
    public Blob getValue(ResultSet rs,String valueName) throws SQLException {
        return rs.getBlob(valueName);
    }
}
