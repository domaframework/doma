package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents the SQL statement built by the Criteria API.
 *
 * @param <RESULT> the result of the execution
 */
public interface Statement<RESULT> extends Buildable<Statement<RESULT>> {

  /**
   * Executes the SQL statement.
   *
   * @return the result
   */
  RESULT execute();

  @Override
  default Statement<RESULT> peek(Consumer<Sql<?>> consumer) {
    return Buildable.super.peek(consumer);
  }
}
