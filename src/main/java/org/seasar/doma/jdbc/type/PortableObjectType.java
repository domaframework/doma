package org.seasar.doma.jdbc.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * A JDBC type for {@link Types#OTHER}.
 * 
 * @see PreparedStatement#setObject(int, Object, int)
 */
public class PortableObjectType<T> extends AbstractJdbcType<T> {

    private final JdbcType<T> baseType;

    public PortableObjectType(JdbcType<T> baseType) {
        super(Types.OTHER);
        this.baseType = baseType;
    }

    @Override
    public T doGetValue(ResultSet resultSet, int index) throws SQLException {
        return baseType.getValue(resultSet, index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, T value)
            throws SQLException {
        preparedStatement.setObject(index, value, this.type);
    }

    @Override
    protected T doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        return baseType.getValue(callableStatement, index);
    }

    @Override
    protected String doConvertToLogFormat(T value) {
        return baseType.convertToLogFormat(value);
    }
}
