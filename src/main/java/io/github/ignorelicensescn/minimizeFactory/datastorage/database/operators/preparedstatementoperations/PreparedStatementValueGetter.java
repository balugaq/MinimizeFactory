package io.github.ignorelicensescn.minimizeFactory.datastorage.database.operators.preparedstatementoperations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface PreparedStatementValueGetter<OutType> {
    OutType getValue(ResultSet rs,String valueName) throws SQLException;
}
