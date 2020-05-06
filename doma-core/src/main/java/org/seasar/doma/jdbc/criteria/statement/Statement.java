package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

public interface Statement<RESULT> extends Buildable {

  RESULT execute();

  default Statement<RESULT> peek(Consumer<Sql<?>> consumer) {
    consumer.accept(asSql());
    return this;
  }
}
