package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BlobSetter implements PreparedStatementValueSetter<InputStream> {
    public static final BlobSetter INSTANCE = new BlobSetter();
    private BlobSetter(){}

    @Override
    public void setValue(PreparedStatement statement, int paramIndex, InputStream in) throws SQLException {
        statement.setBlob(paramIndex,in);
    }
}
