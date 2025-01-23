package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeTypeGetter implements ResultSetValueGetter<NodeType> {
    public static final NodeTypeGetter INSTANCE = new NodeTypeGetter();
    private NodeTypeGetter(){}
    @Override
    public NodeType getValue(ResultSet rs, String valueName) throws SQLException {
        return NodeType.values()[rs.getInt(valueName)];
    }
}
