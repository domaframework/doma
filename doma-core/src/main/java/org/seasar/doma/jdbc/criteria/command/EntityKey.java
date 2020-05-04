package org.seasar.doma.jdbc.criteria.command;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.def.EntityDef;

public final class EntityKey {
  private final EntityDef<?> entityDef;
  private final List<?> items;

  public EntityKey(EntityDef<?> entityDef, List<?> items) {
    Objects.requireNonNull(entityDef);
    Objects.requireNonNull(items);
    this.entityDef = entityDef;
    this.items = Collections.unmodifiableList(items);
  }

  public EntityDef<?> getEntityDef() {
    return entityDef;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntityKey)) return false;
    EntityKey entityKey = (EntityKey) o;
    return entityDef.equals(entityKey.entityDef) && items.equals(entityKey.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityDef, items);
  }
}
