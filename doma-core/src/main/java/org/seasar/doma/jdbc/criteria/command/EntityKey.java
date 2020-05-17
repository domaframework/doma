package org.seasar.doma.jdbc.criteria.command;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.criteria.metamodel.EntityMetamodel;

public final class EntityKey {
  private final EntityMetamodel<?> entityMetamodel;
  private final List<?> items;

  public EntityKey(EntityMetamodel<?> entityMetamodel, List<?> items) {
    this.entityMetamodel = Objects.requireNonNull(entityMetamodel);
    Objects.requireNonNull(items);
    this.items = Collections.unmodifiableList(items);
  }

  public EntityMetamodel<?> getEntityMetamodel() {
    return entityMetamodel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntityKey)) return false;
    EntityKey entityKey = (EntityKey) o;
    return entityMetamodel.equals(entityKey.entityMetamodel) && items.equals(entityKey.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entityMetamodel, items);
  }
}
