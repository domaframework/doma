package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.ObjectProvider;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.jdbc.query.SelectQuery;

public class EntityIterationHandler<ENTITY, RESULT>
    extends AbstractIterationHandler<ENTITY, RESULT> {

  protected final EntityDesc<ENTITY> entityDesc;

  public EntityIterationHandler(
      EntityDesc<ENTITY> entityDesc, IterationCallback<ENTITY, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(entityDesc);
    this.entityDesc = entityDesc;
  }

  @Override
  protected ObjectProvider<ENTITY> createObjectProvider(SelectQuery query) {
    return new EntityProvider<>(entityDesc, query, query.isResultMappingEnsured());
  }
}
