package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.sql.CallableStatement;
import java.sql.SQLException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.type.JdbcType;
import org.seasar.doma.wrapper.Wrapper;

/** @author taedium */
public class JdbcOutParameterGetter implements JdbcMappingFunction {

  protected final CallableStatement callableStatement;

  protected final int index;

  public JdbcOutParameterGetter(CallableStatement callableStatement, int index) {
    assertNotNull(callableStatement);
    assertTrue(index > 0, index);
    this.callableStatement = callableStatement;
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
    V value = jdbcType.getValue(callableStatement, index);
    wrapper.set(value);
    return null;
  }
}
