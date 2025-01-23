package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations;

import javax.annotation.Nullable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementValueSetter<InType> {
    void setValue(PreparedStatement statement,int paramIndex,@Nullable InType in) throws SQLException;
}
