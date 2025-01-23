package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.BooleanGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.BooleanSetter;

import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;
import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.VALUE_LOCK_STATUS_BOOLEAN;

public class LockStatusOperator extends LocationBasedSimpleOperator<Boolean,Boolean> {
    public static final LockStatusOperator INSTANCE = new LockStatusOperator();
    private LockStatusOperator() {
        super(KEY_COLUMN.columnInnerName, new OperateItem<>(
                VALUE_LOCK_STATUS_BOOLEAN.columnInnerName,
                BooleanSetter.INSTANCE,
                BooleanGetter.INSTANCE
        ));
    }
}
