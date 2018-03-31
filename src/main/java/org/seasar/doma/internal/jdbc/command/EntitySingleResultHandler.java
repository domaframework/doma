package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.jdbc.entity.EntityDesc;

public class EntitySingleResultHandler<ENTITY> extends AbstractSingleResultHandler<ENTITY> {

  public EntitySingleResultHandler(EntityDesc<ENTITY> entityDesc) {
    super(new EntityIterationHandler<>(entityDesc, new SingleResultCallback<>()));
  }
}
