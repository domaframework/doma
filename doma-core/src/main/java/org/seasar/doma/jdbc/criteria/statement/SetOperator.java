package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Consumer;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.declaration.OrderByIndexDeclaration;

public interface SetOperator<ELEMENT> extends SetOperand<ELEMENT> {

  SetOperand<ELEMENT> orderBy(Consumer<OrderByIndexDeclaration> block);

  @Override
  SetOperator<ELEMENT> peek(Consumer<Sql<?>> consumer);
}
