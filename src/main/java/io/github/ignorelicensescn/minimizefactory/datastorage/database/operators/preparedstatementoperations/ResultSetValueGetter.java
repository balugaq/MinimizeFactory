package io.github.ignorelicensescn.minimizefactory.datastorage.database.operators.preparedstatementoperations;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetValueGetter<OutType> {
    OutType getValue(ResultSet rs,String valueName) throws SQLException;
}
