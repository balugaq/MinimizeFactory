package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.BytesGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.BytesSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.types.Column;

import java.io.InputStream;
import java.sql.Blob;

public class BlockDataOperator extends LocationBasedSimpleOperator<byte[], byte[]> {
    public static BlockDataOperator INSTANCE = new BlockDataOperator();
    private BlockDataOperator() {
        super(
                Column.KEY_COLUMN.columnInnerName,
                new OperateItem<>(Column.VALUE_DATA_BLOB.columnInnerName,
                        BytesSetter.INSTANCE,
                        BytesGetter.INSTANCE)
        );
    }
}
