package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class BooleanSetter implements PreparedStatementValueSetter<Boolean> {
    public static final BooleanSetter INSTANCE = new BooleanSetter();
    private BooleanSetter(){}
    @Override
    public void setValue(PreparedStatement statement, int paramIndex,@Nullable Boolean in) throws SQLException {
        if (in == null){
            statement.setNull(paramIndex, Types.BOOLEAN);
        }else {
            statement.setBoolean(paramIndex,in);
        }
    }
}
