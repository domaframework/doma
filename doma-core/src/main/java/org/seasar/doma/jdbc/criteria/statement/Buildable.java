package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

/**
 * Represents that the implementation can build an SQL.
 *
 * @param <BUILDABLE> the subtype
 */
public interface Buildable<BUILDABLE extends Buildable<BUILDABLE>> {

  /**
   * Returns the built SQL.
   *
   * @return the built sql
   */
  Sql<?> asSql();

  /**
   * Peeks the built SQL.
   *
   * @param consumer the SQL handler
   * @return this instance
   */
  @SuppressWarnings("unchecked")
  default BUILDABLE peek(Consumer<Sql<?>> consumer) {
    Objects.requireNonNull(consumer);
    consumer.accept(asSql());
    return (BUILDABLE) this;
  }
}
