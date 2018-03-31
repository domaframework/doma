package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

public class JdbcValueGetter implements JdbcMappingFunction {

  protected final ResultSet resultSet;

  protected final int index;

  public JdbcValueGetter(ResultSet resultSet, int index) {
    assertNotNull(resultSet);
    assertTrue(index > 0, index);
    this.resultSet = resultSet;
    this.index = index;
  }

  @Override
  public <R, V> R apply(Wrapper<V> wrapper, JdbcType<V> jdbcType) throws SQLException {
    if (wrapper == null) {
      throw new DomaNullPointerException("wrapper");
    }
    if (jdbcType == null) {
      throw new DomaNullPointerException("jdbcType");
    }
    var value = jdbcType.getValue(resultSet, index);
    wrapper.set(value);
    return null;
  }
}
