package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.def.EntityDef;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityPoolIterationHandler
    extends AbstractIterationHandler<EntityPool, List<EntityPool>> {
  private final List<EntityDef<?>> entityDefs;

  public EntityPoolIterationHandler(List<EntityDef<?>> entityDefs) {
    super(new ResultListCallback<>());
    Objects.requireNonNull(entityDefs);
    this.entityDefs = entityDefs;
  }

  @Override
  protected ObjectProvider<EntityPool> createObjectProvider(SelectQuery query) {
    return new EntityPoolProvider(entityDefs, query);
  }
}
