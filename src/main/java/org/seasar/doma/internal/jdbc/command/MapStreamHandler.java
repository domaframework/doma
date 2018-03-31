package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.jdbc.query.SelectQuery;

/**
 * @author nakamura-to
 * @param <RESULT>
 */
public class MapStreamHandler<RESULT> extends AbstractStreamHandler<Map<String, Object>, RESULT> {

  private final MapKeyNamingType mapKeyNamingType;

  public MapStreamHandler(
      MapKeyNamingType keyNamingType, Function<Stream<Map<String, Object>>, RESULT> mapper) {
    super(mapper);
    assertNotNull(keyNamingType);
    this.mapKeyNamingType = keyNamingType;
  }

  @Override
  protected MapProvider createObjectProvider(SelectQuery query) {
    return new MapProvider(query, mapKeyNamingType);
  }
}
