package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class NodeTypeSetter implements PreparedStatementValueSetter<NodeType> {
    public static final NodeTypeSetter INSTANCE = new NodeTypeSetter();
    private NodeTypeSetter(){}

    @Override
    public void setValue(PreparedStatement statement, int paramIndex,@Nullable NodeType in) throws SQLException {
        if (in == null){
            statement.setNull(paramIndex, Types.INTEGER);
        }else {
            statement.setInt(paramIndex,in.ordinal());
        }
    }
}
