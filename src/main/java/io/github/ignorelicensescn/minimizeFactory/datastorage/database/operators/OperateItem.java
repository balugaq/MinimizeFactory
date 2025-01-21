package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators;

import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueGetter;
import io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations.PreparedStatementValueSetter;

import javax.annotation.Nonnull;

public record OperateItem<InType,OutType>(
        @Nonnull String valueName,
        @Nonnull PreparedStatementValueSetter<InType> setter,
        @Nonnull PreparedStatementValueGetter<OutType> getter)  {
}
