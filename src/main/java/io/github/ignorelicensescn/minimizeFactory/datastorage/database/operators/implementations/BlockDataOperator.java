package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.implementations;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.LocationBasedSimpleOperator;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations.BlobGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.implementations.BlobSetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.types.Column;
import io.github.ignorelicensescn.minimizeFactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

import java.io.InputStream;
import java.sql.Blob;

public class BlockDataOperator extends LocationBasedSimpleOperator<InputStream, Blob> {
    public static BlockDataOperator INSTANCE = new BlockDataOperator();
    private BlockDataOperator() {
        super(
                Column.KEY_COLUMN.columnInnerName,
                new OperateItem<>(Column.VALUE_DATA_BLOB.columnInnerName,
                        BlobSetter.INSTANCE,
                        BlobGetter.INSTANCE)
        );
    }
}
