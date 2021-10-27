package org.seasar.doma.it;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.dialect.SqliteDialect.SqliteJdbcMappingVisitor;
import org.seasar.doma.wrapper.BigDecimalWrapper;
import org.seasar.doma.wrapper.IntegerWrapper;

public class CustomSqliteJdbcMappingVisitor extends SqliteJdbcMappingVisitor {

  @Override
  public Void visitBigDecimalWrapper(
      BigDecimalWrapper wrapper, JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
    BigDecimal decimal = wrapper.get();
    IntegerWrapper intergerWrapper;
    if (decimal == null) {
      intergerWrapper = new IntegerWrapper(null);
    } else {
      intergerWrapper = new IntegerWrapper(decimal.intValue());
    }
    super.visitIntegerWrapper(intergerWrapper, p, q);
    wrapper.set(intergerWrapper.get());
    return null;
  }
}
