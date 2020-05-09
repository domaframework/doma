package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public interface SetOperand<ELEMENT> extends Collectable<ELEMENT> {

  SetOperationContext<ELEMENT> getContext();

  SetOperand<ELEMENT> union(SetOperand<ELEMENT> other);

  SetOperand<ELEMENT> unionAll(SetOperand<ELEMENT> other);

  @Override
  SetOperand<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
