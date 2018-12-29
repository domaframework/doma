package org.seasar.doma.internal.jdbc.command;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.seasar.doma.jdbc.JdbcMappable;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.wrapper.Wrapper;

/**
 * @author nakamura-to
 * @param <TARGET> 提供する型
 */
public abstract class AbstractObjectProvider<TARGET> implements ObjectProvider<TARGET> {

  protected <BASIC> void fetch(
      ResultSet resultSet,
      JdbcMappable<BASIC> mappable,
      int index,
      JdbcMappingVisitor jdbcMappingVisitor)
      throws SQLException {
    Wrapper<?> wrapper = mappable.getWrapper();
    wrapper.accept(jdbcMappingVisitor, new JdbcValueGetter(resultSet, index), mappable);
  }
}
