package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NodeTypeSetter implements PreparedStatementValueSetter<NodeType> {
    public static final NodeTypeSetter INSTANCE = new NodeTypeSetter();
    private NodeTypeSetter(){}

    @Override
    public void setValue(PreparedStatement statement, int paramIndex, NodeType in) throws SQLException {
        statement.setInt(paramIndex,in.ordinal());
    }
}
