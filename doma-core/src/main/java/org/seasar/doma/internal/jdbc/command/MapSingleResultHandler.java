package org.seasar.doma.internal.jdbc.command;

import java.util.Map;
import org.seasar.doma.MapKeyNamingType;

public class MapSingleResultHandler extends AbstractSingleResultHandler<Map<String, Object>> {

  public MapSingleResultHandler(MapKeyNamingType keyNamingType) {
    super(new MapIterationHandler<>(keyNamingType, new SingleResultCallback<>()));
  }
}
