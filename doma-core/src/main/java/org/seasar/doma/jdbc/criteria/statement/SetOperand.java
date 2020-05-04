package org.seasar.doma.jdbc.criteria.statement;

import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public interface SetOperand<ELEMENT> extends SelectIntermediate<ELEMENT> {

  SetOperationContext<ELEMENT> getContext();

  SetOperand<ELEMENT> union(SetOperand<ELEMENT> other);

  SetOperand<ELEMENT> unionAll(SetOperand<ELEMENT> other);
}
