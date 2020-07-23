package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;

public interface Statement<RESULT> extends Buildable<Statement<RESULT>> {

  RESULT execute();

  @Override
  default Statement<RESULT> peek(Consumer<Sql<?>> consumer) {
    return Buildable.super.peek(consumer);
  }
}
