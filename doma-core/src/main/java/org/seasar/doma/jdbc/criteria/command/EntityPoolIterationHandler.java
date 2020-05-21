package org.seasar.doma.jdbc.criteria.command;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.seasar.doma.internal.jdbc.command.AbstractIterationHandler;
import org.seasar.doma.internal.jdbc.command.ResultListCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;
import org.seasar.doma.jdbc.criteria.metamodel.PropertyMetamodel;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityPoolIterationHandler
    extends AbstractIterationHandler<EntityPool, List<EntityPool>> {
  private final Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> projectionEntityMetamodels;

  public EntityPoolIterationHandler(
      Map<EntityMetamodel<?>, List<PropertyMetamodel<?>>> projectionEntityMetamodels) {
    super(new ResultListCallback<>());
    this.projectionEntityMetamodels = Objects.requireNonNull(projectionEntityMetamodels);
  }

  @Override
  protected ObjectProvider<EntityPool> createObjectProvider(SelectQuery query) {
    return new EntityPoolProvider(projectionEntityMetamodels, query);
  }
}
