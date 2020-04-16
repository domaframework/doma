package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.jdbc.entity.EntityType;

public class EntitySingleResultHandler<ENTITY> extends AbstractSingleResultHandler<ENTITY> {

  public EntitySingleResultHandler(EntityType<ENTITY> entityType) {
    super(new EntityIterationHandler<>(entityType, new SingleResultCallback<ENTITY>()));
  }
}
