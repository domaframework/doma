package org.seasar.doma.jdbc.type;

import java.sql.*;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;

/**
 * A skeletal implementation for the {@link JdbcType} interface .
 *
 * @param <T> the basic type
 */
public abstract class AbstractJdbcType<T> implements JdbcType<T> {

  /**
   * the SQL type
   *
   * @see Types
   */
  protected final int type;

  /**
   * Creates an instance.
   *
   * @param type the SQL type
   */
  protected AbstractJdbcType(int type) {
    this.type = type;
  }

  @Override
  public T getValue(ResultSet resultSet, int index) throws SQLException {
    if (resultSet == null) {
      throw new DomaNullPointerException("resultSet");
    }
    if (index < 1) {
      throw new DomaIllegalArgumentException("index", "index < 1");
    }
    T result = doGetValue(resultSet, index);
    if (resultSet.wasNull()) {
      return null;
    }
    return result;
  }

  @Override
  public void setValue(PreparedStatement preparedStatement, int index, T value)
      throws SQLException {
    if (preparedStatement == null) {
      throw new DomaNullPointerException("preparedStatement");
    }
    if (index < 1) {
      throw new DomaIllegalArgumentException("index", "index < 1");
    }
    if (value == null) {
      preparedStatement.setNull(index, type);
    } else {
      doSetValue(preparedStatement, index, value);
    }
  }

  @Override
  public void registerOutParameter(CallableStatement callableStatement, int index)
      throws SQLException {
    if (callableStatement == null) {
      throw new DomaNullPointerException("callableStatement");
    }
    if (index < 1) {
      throw new DomaIllegalArgumentException("index", "index < 1");
    }
    callableStatement.registerOutParameter(index, type);
  }

  @Override
  public T getValue(CallableStatement callableStatement, int index) throws SQLException {
    if (callableStatement == null) {
      throw new DomaNullPointerException("callableStatement");
    }
    if (index < 1) {
      throw new DomaIllegalArgumentException("index", "index < 1");
    }
    T result = doGetValue(callableStatement, index);
    if (callableStatement.wasNull()) {
      return null;
    }
    return result;
  }

  @Override
  public String convertToLogFormat(T value) {
    if (value == null) {
      return "null";
    }
    return doConvertToLogFormat(value);
  }

  protected abstract T doGetValue(ResultSet resultSet, int index) throws SQLException;

  protected abstract void doSetValue(PreparedStatement preparedStatement, int index, T value)
      throws SQLException;

  protected abstract T doGetValue(CallableStatement callableStatement, int index)
      throws SQLException;

  protected abstract String doConvertToLogFormat(T value);
}
