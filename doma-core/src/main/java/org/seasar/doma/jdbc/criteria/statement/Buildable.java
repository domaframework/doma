package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

public interface Buildable<BUILDABLE extends Buildable<BUILDABLE>> {

  Sql<?> asSql();

  @SuppressWarnings("unchecked")
  default BUILDABLE peek(Consumer<Sql<?>> consumer) {
    Objects.requireNonNull(consumer);
    consumer.accept(asSql());
    return (BUILDABLE) this;
  }
}
