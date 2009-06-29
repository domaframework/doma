package org.seasar.doma.jdbc;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author taedium
 * 
 */
public interface JdbcType<T> {

    T getValue(ResultSet resultSet, int index) throws SQLException;

    void setValue(PreparedStatement preparedStatement, int index, T value)
            throws SQLException;

    void registerOutParameter(CallableStatement callableStatement, int index)
            throws SQLException;

    T getValue(CallableStatement callableStatement, int index)
            throws SQLException;

}
