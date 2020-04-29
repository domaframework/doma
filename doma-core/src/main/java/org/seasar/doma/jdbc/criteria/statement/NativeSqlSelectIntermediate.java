package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;

public class NativeSqlSelectIntermediate<ELEMENT> extends AbstractSetOperand<ELEMENT>
    implements SelectIntermediate<ELEMENT> {

  private final SelectFromDeclaration declaration;

  public NativeSqlSelectIntermediate(SelectFromDeclaration declaration) {
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  public NativeSqlSelectTerminate<ELEMENT> map(Function<Row, ELEMENT> mapper) {
    declaration.map(mapper);
    return new NativeSqlSelectTerminate<>(declaration);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return new SetOperationContext.Select<>(declaration.getContext());
  }

  @Override
  public Sql<?> asSql(Config config) {
    NativeSqlSelectTerminate<ELEMENT> terminate = new NativeSqlSelectTerminate<>(declaration);
    return terminate.asSql(config);
  }
}
