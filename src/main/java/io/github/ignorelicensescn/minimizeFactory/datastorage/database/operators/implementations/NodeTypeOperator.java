package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations.NodeTypeGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations.NodeTypeSetter;
import io.github.ignorelicensescn.minimizeFactory.utils.machinenetwork.NodeType;

import static io.github.ignorelicensescn.minimizeFactory.datastorage.database.types.Column.KEY_COLUMN;
import static io.github.ignorelicensescn.minimizeFactory.datastorage.database.types.Column.VALUE_NODE_TYPE_INT;

public class NodeTypeOperator extends LocationBasedSimpleOperator<NodeType,NodeType> {
    public static NodeTypeOperator INSTANCE = new NodeTypeOperator();
    private NodeTypeOperator() {
        super(KEY_COLUMN.columnInnerName,
                new OperateItem<>(VALUE_NODE_TYPE_INT.columnInnerName,
                        NodeTypeSetter.INSTANCE,
                        NodeTypeGetter.INSTANCE)
        );
    }
}
