package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class BytesSetter implements PreparedStatementValueSetter<byte[]> {
    public static final BytesSetter INSTANCE = new BytesSetter();
    private BytesSetter(){}

    @Override
    public void setValue(PreparedStatement statement, int paramIndex,@Nullable byte[] in) throws SQLException {
        if (in == null){
            statement.setNull(paramIndex, Types.BLOB);
        }else {
            statement.setBytes(paramIndex,in);
        }
    }
}
