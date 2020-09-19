package org.seasar.doma.internal.jdbc.command;

import java.util.Optional;
import org.seasar.doma.jdbc.entity.EntityType;

public class OptionalEntitySingleResultHandler<ENTITY>
    extends AbstractSingleResultHandler<Optional<ENTITY>> {

  public OptionalEntitySingleResultHandler(EntityType<ENTITY> entityType) {
    super(new EntityIterationHandler<>(entityType, new OptionalSingleResultCallback<>()));
  }
}
