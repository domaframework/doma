package org.seasar.doma.jdbc.criteria.statement;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.query.SelectQuery;

public abstract class AbstractSetOperand<STMT extends AbstractSetOperand<STMT, ELEMENT>, ELEMENT>
    extends AbstractStatement<STMT, List<ELEMENT>> implements SetOperand<ELEMENT> {

  protected final Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory;

  protected AbstractSetOperand(
      Config config, Function<SelectQuery, ObjectProvider<ELEMENT>> objectProviderFactory) {
    super(Objects.requireNonNull(config));
    this.objectProviderFactory = Objects.requireNonNull(objectProviderFactory);
  }

  @Override
  public SetOperator<ELEMENT> union(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> newContext =
        new SetOperationContext.Union<>(getContext(), other.getContext());
    return new NativeSqlSetStarting<>(config, newContext, objectProviderFactory);
  }

  @Override
  public SetOperator<ELEMENT> unionAll(SetOperand<ELEMENT> other) {
    Objects.requireNonNull(other);
    SetOperationContext<ELEMENT> newContext =
        new SetOperationContext.UnionAll<>(getContext(), other.getContext());
    return new NativeSqlSetStarting<>(config, newContext, objectProviderFactory);
  }
}
