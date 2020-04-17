package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.Map;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.jdbc.IterationCallback;
import org.seasar.doma.jdbc.query.SelectQuery;

public class MapIterationHandler<RESULT>
    extends AbstractIterationHandler<Map<String, Object>, RESULT> {

  private final MapKeyNamingType mapKeyNamingType;

  public MapIterationHandler(
      MapKeyNamingType keyNamingType,
      IterationCallback<Map<String, Object>, RESULT> iterationCallback) {
    super(iterationCallback);
    assertNotNull(keyNamingType);
    this.mapKeyNamingType = keyNamingType;
  }

  @Override
  protected MapProvider createObjectProvider(SelectQuery query) {
    return new MapProvider(query, mapKeyNamingType);
  }
}
