package org.seasar.doma.internal.jdbc.command;

import java.util.Map;
import java.util.Optional;
import org.seasar.doma.MapKeyNamingType;

public class OptionalMapSingleResultHandler
    extends AbstractSingleResultHandler<Optional<Map<String, Object>>> {

  public OptionalMapSingleResultHandler(MapKeyNamingType keyNamingType) {
    super(new MapIterationHandler<>(keyNamingType, new OptionalSingleResultCallback<>()));
  }
}
