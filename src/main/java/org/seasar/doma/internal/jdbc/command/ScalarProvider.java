package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.Constants.ROWNUMBER_COLUMN_NAME;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.function.Supplier;
import org.seasar.doma.internal.jdbc.scalar.Scalar;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.NonSingleColumnException;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.query.Query;

public class ScalarProvider<BASIC, CONTAINER> extends AbstractObjectProvider<CONTAINER> {

  protected final Supplier<Scalar<BASIC, CONTAINER>> supplier;

  protected final Query query;

  protected final JdbcMappingVisitor jdbcMappingVisitor;

  protected boolean columnCountValidated;

  /**
   * @param supplier
   * @param query
   */
  public ScalarProvider(Supplier<Scalar<BASIC, CONTAINER>> supplier, Query query) {
    assertNotNull(supplier, query);
    this.supplier = supplier;
    this.query = query;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
  }

  @Override
  public CONTAINER get(ResultSet resultSet) throws SQLException {
    if (!columnCountValidated) {
      validateColumnCount(resultSet);
    }
    Scalar<BASIC, CONTAINER> scalar = supplier.get();
    fetch(resultSet, scalar, 1, jdbcMappingVisitor);
    return scalar.get();
  }

  protected void validateColumnCount(ResultSet resultSet) throws SQLException {
    int columnCount = getColumnCount(resultSet);
    if (columnCount != 1) {
      Sql<?> sql = query.getSql();
      throw new NonSingleColumnException(query.getConfig().getExceptionSqlLogType(), sql);
    }
    columnCountValidated = true;
  }

  protected int getColumnCount(ResultSet resultSet) throws SQLException {
    ResultSetMetaData resultSetMeta = resultSet.getMetaData();
    int columnCount = resultSetMeta.getColumnCount();
    if (columnCount == 2) {
      String columnName = resultSetMeta.getColumnLabel(2).toLowerCase();
      return ROWNUMBER_COLUMN_NAME.equals(columnName) ? 1 : 2;
    }
    return columnCount;
  }
}
