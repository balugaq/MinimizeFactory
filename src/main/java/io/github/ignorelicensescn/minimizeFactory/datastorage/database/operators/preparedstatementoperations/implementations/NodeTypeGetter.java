package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeTypeGetter implements PreparedStatementValueGetter<NodeType> {
    public static final NodeTypeGetter INSTANCE = new NodeTypeGetter();
    private NodeTypeGetter(){}
    @Override
    public NodeType getValue(ResultSet rs, String valueName) throws SQLException {
        return NodeType.values()[rs.getInt(valueName)];
    }
}
