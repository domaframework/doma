package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityType;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityIterationHandler<ENTITY, RESULT>
    extends AbstractIterationHandler<ENTITY, RESULT> {

  protected final EntityType<ENTITY> entityType;

  public EntityIterationHandler(
      EntityType<ENTITY> entityType, IterationCallback<ENTITY, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(entityType);
    this.entityType = entityType;
  }

  @Override
  protected ObjectProvider<ENTITY> createObjectProvider(SelectQuery query) {
    return new EntityProvider<>(entityType, query, query.isResultMappingEnsured());
  }
}
