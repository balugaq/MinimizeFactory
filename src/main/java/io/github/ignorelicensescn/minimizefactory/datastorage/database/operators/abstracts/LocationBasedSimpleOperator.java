package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.SerializeFriendlyBlockLocationSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

public abstract class LocationBasedSimpleOperator<InType,OutType> extends SimpleOperator<InType,OutType, SerializeFriendlyBlockLocation> {
    public LocationBasedSimpleOperator(String keyName, OperateItem<InType, OutType> operateItem) {
        super(keyName, operateItem, SerializeFriendlyBlockLocationSetter.INSTANCE);
    }
}
