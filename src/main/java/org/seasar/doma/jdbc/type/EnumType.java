package org.seasar.doma.jdbc.type;

import java.sql.*;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.message.Message;

/**
 * A JDBC type for {@link Types#VARCHAR} and {@link Enum}.
 *
 * @param <E> the enum subclass
 */
public class EnumType<E extends Enum<E>> extends AbstractJdbcType<E> {

  protected final Class<E> enumClass;

  /**
   * Creates an instance.
   *
   * @param enumClass the {@link Enum} class
   * @throws DomaNullPointerException thrown, if {@code enumClass} is {@code null}
   */
  public EnumType(Class<E> enumClass) {
    super(Types.VARCHAR);
    if (enumClass == null) {
      throw new DomaNullPointerException("enumClass");
    }
    this.enumClass = enumClass;
  }

  @Override
  protected E doGetValue(ResultSet resultSet, int index) throws SQLException {
    String value = resultSet.getString(index);
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, value);
    } catch (IllegalArgumentException e) {
      throw new JdbcException(Message.DOMA2040, enumClass.getName(), value);
    }
  }

  @Override
  protected void doSetValue(PreparedStatement preparedStatement, int index, E value)
      throws SQLException {
    preparedStatement.setString(index, value.name());
  }

  @Override
  protected E doGetValue(CallableStatement callableStatement, int index) throws SQLException {
    String value = callableStatement.getString(index);
    if (value == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, value);
    } catch (IllegalArgumentException e) {
      throw new JdbcException(Message.DOMA2040, enumClass.getName(), value);
    }
  }

  @Override
  protected String doConvertToLogFormat(E value) {
    return "'" + value.name() + "'";
  }
}
