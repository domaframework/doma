package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.BiFunction;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public abstract class AbstractSetOperand<ELEMENT> implements SetOperand<ELEMENT> {

  protected final Config config;

  protected AbstractSetOperand(Config config) {
    this.config = Objects.requireNonNull(config);
  }

  @Override
  public Mappable<ELEMENT> union(Mappable<ELEMENT> other) {
    Objects.requireNonNull(other);
    return union(other, SetOperationContext.Union::new);
  }

  @Override
  public Mappable<ELEMENT> unionAll(Mappable<ELEMENT> other) {
    Objects.requireNonNull(other);
    return union(other, SetOperationContext.UnionAll::new);
  }

  private Mappable<ELEMENT> union(
      Mappable<ELEMENT> other,
      BiFunction<
              SetOperationContext<ELEMENT>,
              SetOperationContext<ELEMENT>,
              SetOperationContext<ELEMENT>>
          newSetOperation) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> context = newSetOperation.apply(getContext(), other.getContext());
    return new NativeSqlSetStarting<>(config, context);
  }
}
