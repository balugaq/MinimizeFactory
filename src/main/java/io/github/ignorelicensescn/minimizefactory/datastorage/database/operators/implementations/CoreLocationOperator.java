package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.SerializeFriendlyBlockLocationGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.SerializeFriendlyBlockLocationSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

public class CoreLocationOperator extends LocationBasedSimpleOperator<SerializeFriendlyBlockLocation,SerializeFriendlyBlockLocation> {
    public static CoreLocationOperator INSTANCE = new CoreLocationOperator();
    private CoreLocationOperator() {
        super(Column.KEY_COLUMN.columnInnerName, new OperateItem<>(
                Column.VALUE_CORE_LOCATION_VARCHAR.columnInnerName,
                SerializeFriendlyBlockLocationSetter.INSTANCE,
                SerializeFriendlyBlockLocationGetter.INSTANCE
                ));
    }
}
