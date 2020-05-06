package org.seasar.doma.jdbc.criteria.statement;

import java.util.Objects;
import java.util.function.Function;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.criteria.context.SetOperationContext;
import org.seasar.doma.jdbc.criteria.declaration.SelectFromDeclaration;

public class NativeSqlSelectMappable<ELEMENT> extends AbstractSetOperand<ELEMENT>
    implements Mappable<ELEMENT> {

  private final SelectFromDeclaration declaration;

  public NativeSqlSelectMappable(Config config, SelectFromDeclaration declaration) {
    super(Objects.requireNonNull(config));
    Objects.requireNonNull(declaration);
    this.declaration = declaration;
  }

  @Override
  public Collectable<ELEMENT> map(Function<Row, ELEMENT> mapper) {
    return new NativeSqlSelectCollectable<>(config, declaration, mapper);
  }

  @Override
  public SetOperationContext<ELEMENT> getContext() {
    return new SetOperationContext.Select<>(declaration.getContext());
  }

  @Override
  public Sql<?> asSql() {
    NativeSqlSelectCollectable<ELEMENT> collectable =
        new NativeSqlSelectCollectable<>(config, declaration, row -> null);
    return collectable.asSql();
  }
}
