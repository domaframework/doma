package org.seasar.doma.internal.jdbc.command;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.seasar.doma.MapKeyNamingType;
import org.seasar.doma.internal.jdbc.scalar.BasicScalar;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.MapKeyNaming;
import org.seasar.doma.jdbc.query.Query;
import org.seasar.doma.wrapper.ObjectWrapper;

public class MapProvider extends AbstractObjectProvider<Map<String, Object>> {

  protected final Query query;

  protected final MapKeyNamingType mapKeyNamingType;

  protected final JdbcMappingVisitor jdbcMappingVisitor;

  protected Map<Integer, String> indexMap;

  public MapProvider(Query query, MapKeyNamingType mapKeyNamingType) {
    assertNotNull(query, mapKeyNamingType);
    this.query = query;
    this.mapKeyNamingType = mapKeyNamingType;
    this.jdbcMappingVisitor = query.getConfig().getDialect().getJdbcMappingVisitor();
  }

  @Override
  public Map<String, Object> get(ResultSet resultSet) throws SQLException {
    Map<String, Object> map = new LinkedHashMap<String, Object>();
    if (indexMap == null) {
      indexMap = createIndexMap(resultSet.getMetaData());
    }
    for (Map.Entry<Integer, String> entry : indexMap.entrySet()) {
      Integer index = entry.getKey();
      String key = entry.getValue();
      BasicScalar<Object> scalar = new BasicScalar<>(() -> new ObjectWrapper(), false);
      fetch(resultSet, scalar, index, jdbcMappingVisitor);
      map.put(key, scalar.get());
    }
    return map;
  }

  protected HashMap<Integer, String> createIndexMap(ResultSetMetaData resultSetMeta)
      throws SQLException {
    MapKeyNaming naming = query.getConfig().getMapKeyNaming();
    Method method = query.getMethod();
    HashMap<Integer, String> indexMap = new HashMap<Integer, String>();
    int count = resultSetMeta.getColumnCount();
    for (int i = 1; i < count + 1; i++) {
      String columnName = resultSetMeta.getColumnLabel(i);
      String key = naming.apply(method, mapKeyNamingType, columnName);
      indexMap.put(i, key);
    }
    return indexMap;
  }
}
