package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementValueSetter<InType> {
    void setValue(PreparedStatement statement,int paramIndex,InType in) throws SQLException;
}
