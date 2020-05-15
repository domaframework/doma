package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityPoolIterationHandler
    extends AbstractIterationHandler<EntityPool, List<EntityPool>> {
  private final List<EntityMetamodel<?>> entityMetamodels;

  public EntityPoolIterationHandler(List<EntityMetamodel<?>> entityMetamodels) {
    super(new ResultListCallback<>());
    this.entityMetamodels = Objects.requireNonNull(entityMetamodels);
  }

  @Override
  protected ObjectProvider<EntityPool> createObjectProvider(SelectQuery query) {
    return new EntityPoolProvider(entityMetamodels, query);
  }
}
