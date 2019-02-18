package org.seasar.doma.internal.jdbc.command;

import java.util.Map;
import java.util.stream.Collector;
import org.seasar.doma.MapKeyNamingType;

public class MapCollectorHandler<RESULT>
    extends AbstractCollectorHandler<Map<String, Object>, RESULT> {

  public MapCollectorHandler(
      MapKeyNamingType mapKeyNamingType, Collector<Map<String, Object>, ?, RESULT> collector) {
    super(new MapStreamHandler<>(mapKeyNamingType, s -> s.collect(collector)));
  }
}
