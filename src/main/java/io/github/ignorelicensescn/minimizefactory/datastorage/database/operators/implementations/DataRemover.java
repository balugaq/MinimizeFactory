package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedColumnRemover;

import static io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column.KEY_COLUMN;

public class DataRemover extends LocationBasedColumnRemover {
    public static final DataRemover INSTANCE = new DataRemover();
    private DataRemover() {
        super(KEY_COLUMN.columnInnerName);
    }
}
