package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.abstracts;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.OperateItem;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.implementations.SerializeFriendlyBlockLocationSetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.machinenetwork.SerializeFriendlyBlockLocation;

//TODO:Rewrite machine initialization (with this).(maybe)
public abstract class LocationBasedMultiValueOperator extends MultiValueOperator<SerializeFriendlyBlockLocation> {
    public LocationBasedMultiValueOperator(String keyName, OperateItem[] operateItems) {
        super(keyName, operateItems, SerializeFriendlyBlockLocationSetter.INSTANCE);
    }
}
