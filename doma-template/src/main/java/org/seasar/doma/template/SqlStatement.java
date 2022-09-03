package org.seasar.doma.template;

import java.util.List;
import java.util.Objects;

/** Represents a SQL statement. */
public class SqlStatement {
  private final String rawSql;
  private final String formattedSql;
  private final List<SqlArgument> arguments;

  /**
   * @param rawSql the raw SQL string. Must not be null.
   * @param formattedSql the formatted SQL string. Must not be null.
   * @param arguments the SQL arguments. Must not be null.
   */
  public SqlStatement(String rawSql, String formattedSql, List<SqlArgument> arguments) {
    this.rawSql = Objects.requireNonNull(rawSql);
    this.formattedSql = Objects.requireNonNull(formattedSql);
    this.arguments = Objects.requireNonNull(arguments);
  }

  /**
   * Returns the raw SQL string.
   *
   * <p>The bind variables are displayed as {@code ?}.
   *
   * @return the raw SQL string. Must not be null.
   */
  public String getRawSql() {
    return rawSql;
  }

  /**
   * Returns the formatted SQL string.
   *
   * <p>The bind variables are replaced with the string representations of the arguments.
   *
   * @return the formatted SQL string. Must not be null.
   */
  public String getFormattedSql() {
    return formattedSql;
  }

  /**
   * Returns the SQL arguments.
   *
   * @return the SQL arguments. Must not be null.
   */
  public List<SqlArgument> getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return rawSql;
  }
}
