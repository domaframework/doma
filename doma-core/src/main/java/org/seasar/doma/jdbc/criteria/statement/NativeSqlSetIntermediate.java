package org.seasar.doma.jdbc.criteria.statement;

import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;

public class NativeSqlSetIntermediate<ELEMENT> extends AbstractSetOperand<ELEMENT>
    implements SelectIntermediate<ELEMENT> {

  private final SetOperationContext<ELEMENT> context;

  public NativeSqlSetIntermediate(SetOperationContext<ELEMENT> context) {
    this.context = context;
  }

  @Override
  public NativeSqlSetTerminate<ELEMENT> map(Function<Row, ELEMENT> mapper) {
    return new NativeSqlSetTerminate<>(context, mapper);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return context;
  }

  @Override
  public Sql<?> asSql(Config config) {
    NativeSqlSetTerminate<ELEMENT> terminate = new NativeSqlSetTerminate<>(context, row -> null);
    return terminate.asSql(config);
  }
}
