package org.seasar.doma.jdbc.criteria.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EntityPool {
  private final Map<EntityKey, EntityData> keyDataMap = new LinkedHashMap<>();

  public void put(EntityKey key, EntityData data) {
    keyDataMap.put(key, data);
  }

  public Set<Map.Entry<EntityKey, EntityData>> entrySet() {
    return keyDataMap.entrySet();
  }
}
