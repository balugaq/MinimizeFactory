package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators;

import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.ResultSetValueGetter;
import io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nonnull;

//maybe i should use mybatis
public record OperateItem<InType,OutType>(
        @Nonnull String valueName,
        @Nonnull PreparedStatementValueSetter<InType> setter,
        @Nonnull ResultSetValueGetter<OutType> getter)  {
}
