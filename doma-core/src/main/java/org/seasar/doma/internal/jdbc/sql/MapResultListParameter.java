package org.seasar.doma.internal.jdbc.sql;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.command.MapProvider;
import org.seasar.doma.jdbc.query.Query;

public class MapResultListParameter extends AbstractResultListParameter<Map<String, Object>> {

  protected final MapKeyNamingType mapKeyNamingType;

  public MapResultListParameter(MapKeyNamingType mapKeyNamingType) {
    super(new ArrayList<>());
    assertNotNull(mapKeyNamingType);
    this.mapKeyNamingType = mapKeyNamingType;
  }

  @Override
  public List<Map<String, Object>> getResult() {
    return list;
  }

  @Override
  public MapProvider createObjectProvider(Query query) {
    return new MapProvider(query, mapKeyNamingType);
  }
}
