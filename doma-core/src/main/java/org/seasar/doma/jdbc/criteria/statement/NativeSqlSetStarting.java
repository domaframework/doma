package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class NativeSqlSetStarting<ELEMENT> extends AbstractSetOperand<ELEMENT>
    implements Mappable<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;

  public NativeSqlSetStarting(SetOperationContext<ELEMENT> context) {
    this.context = context;
  }

  @Override
  public Collectable<ELEMENT> map(Function<Row, ELEMENT> mapper) {
    return new NativeSqlSetCollectable<>(context, mapper);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return context;
  }

  @Override
  public Sql<?> asSql(Config config) {
    NativeSqlSetCollectable<ELEMENT> terminate =
        new NativeSqlSetCollectable<>(context, row -> null);
    return terminate.asSql(config);
  }
}
