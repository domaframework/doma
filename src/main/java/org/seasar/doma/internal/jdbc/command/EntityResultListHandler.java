package org.seasar.doma.internal.jdbc.command;

import org.seasar.doma.jdbc.entity.EntityType;

/** @author taedium */
public class EntityResultListHandler<ENTITY> extends AbstractResultListHandler<ENTITY> {

  public EntityResultListHandler(EntityType<ENTITY> entityType) {
    super(new EntityIterationHandler<>(entityType, new ResultListCallback<ENTITY>()));
  }
}
