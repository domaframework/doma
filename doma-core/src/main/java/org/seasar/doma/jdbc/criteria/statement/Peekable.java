package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

public interface Peekable<PEEKABLE extends Peekable<PEEKABLE>> extends Buildable {

  @SuppressWarnings("unchecked")
  default PEEKABLE peek(Consumer<Sql<?>> consumer) {
    consumer.accept(asSql());
    return (PEEKABLE) this;
  }
}
