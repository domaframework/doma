package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public interface SetOperand<ELEMENT> {

  SetOperationContext<ELEMENT> getContext();

  Mappable<ELEMENT> union(Mappable<ELEMENT> other);

  Mappable<ELEMENT> unionAll(Mappable<ELEMENT> other);
}
