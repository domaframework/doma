package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class NativeSqlSetStarting<ELEMENT> extends AbstractSetOperand<ELEMENT>
    implements Mappable<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;

  public NativeSqlSetStarting(Config config, SetOperationContext<ELEMENT> context) {
    super(Objects.requireNonNull(config));
    this.context = Objects.requireNonNull(context);
  }

  @Override
  public Collectable<ELEMENT> map(Function<Row, ELEMENT> mapper) {
    return new NativeSqlSetCollectable<>(config, context, mapper);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return context;
  }

  @Override
  public Sql<?> asSql() {
    NativeSqlSetCollectable<ELEMENT> terminate =
        new NativeSqlSetCollectable<>(config, context, row -> null);
    return terminate.asSql();
  }
}
