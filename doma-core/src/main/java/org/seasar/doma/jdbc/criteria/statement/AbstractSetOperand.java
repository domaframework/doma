package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public abstract class AbstractSetOperand<ELEMENT> implements SetOperand<ELEMENT> {

  @Override
  public SetOperand<ELEMENT> union(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    return union(other, SetOperationContext.Union::new);
  }

  @Override
  public SetOperand<ELEMENT> unionAll(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    return union(other, SetOperationContext.UnionAll::new);
  }

  private SetOperand<ELEMENT> union(
      SetOperand<ELEMENT> other,
      BiFunction<
              SetOperationContext<ELEMENT>,
              SetOperationContext<ELEMENT>,
              SetOperationContext<ELEMENT>>
          newSetOperation) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> context = newSetOperation.apply(getContext(), other.getContext());
    return new NativeSqlSetIntermediate<>(context);
  }
}
