package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.NodeTypeGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.NodeTypeSetter;
import io.github.ignorelicensescn.minimizefactory.utils.machinenetwork.NodeType;

import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.VALUE_NODE_TYPE_INT;

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
