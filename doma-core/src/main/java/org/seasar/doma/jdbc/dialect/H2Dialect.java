package org.seasar.doma.jdbc.dialect;

import org.seasar.doma.expr.ExpressionFunctions;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.SqlLogFormattingVisitor;

/** A dialect for H2 version 1.4.200 and above. */
public class H2Dialect extends H214199Dialect {

  public H2Dialect() {
    this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(), new H2ExpressionFunctions());
  }

  public H2Dialect(JdbcMappingVisitor jdbcMappingVisitor) {
    this(jdbcMappingVisitor, new H2SqlLogFormattingVisitor(), new H2ExpressionFunctions());
  }

  public H2Dialect(SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(new H2JdbcMappingVisitor(), sqlLogFormattingVisitor, new H2ExpressionFunctions());
  }

  public H2Dialect(ExpressionFunctions expressionFunctions) {
    this(new H2JdbcMappingVisitor(), new H2SqlLogFormattingVisitor(), expressionFunctions);
  }

  public H2Dialect(
      JdbcMappingVisitor jdbcMappingVisitor, SqlLogFormattingVisitor sqlLogFormattingVisitor) {
    this(jdbcMappingVisitor, sqlLogFormattingVisitor, new H2ExpressionFunctions());
  }

  public H2Dialect(
      JdbcMappingVisitor jdbcMappingVisitor,
      SqlLogFormattingVisitor sqlLogFormattingVisitor,
      ExpressionFunctions expressionFunctions) {
    super(jdbcMappingVisitor, sqlLogFormattingVisitor, expressionFunctions);
  }

  @Override
  public boolean includesIdentityColumn() {
    return false;
  }

  public static class H2JdbcMappingVisitor extends H214199JdbcMappingVisitor {}

  public static class H2SqlLogFormattingVisitor extends H214199SqlLogFormattingVisitor {}

  public static class H2ExpressionFunctions extends H214199ExpressionFunctions {

    public H2ExpressionFunctions() {
      super();
    }

    public H2ExpressionFunctions(char[] wildcards) {
      super(wildcards);
    }

    protected H2ExpressionFunctions(char escapeChar, char[] wildcards) {
      super(escapeChar, wildcards);
    }
  }
}
