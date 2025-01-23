package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.SerializeFriendlyBlockLocationSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;

public class LocationBasedColumnAdder extends ColumnAdder<SerializeFriendlyBlockLocation> {
    public static final LocationBasedColumnAdder INSTANCE = new LocationBasedColumnAdder();
    private LocationBasedColumnAdder() {
        super(KEY_COLUMN.columnInnerName, SerializeFriendlyBlockLocationSetter.INSTANCE);
    }
}
