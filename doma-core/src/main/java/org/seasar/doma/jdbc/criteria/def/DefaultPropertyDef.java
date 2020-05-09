package org.seasar.doma.jdbc.criteria.def;

import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityPropertyType;
import org.seasar.doma.jdbc.entity.EntityType;

public class DefaultPropertyDef<PROPERTY> implements PropertyDef<PROPERTY> {

  private final Class<?> clazz;
  private final EntityType<?> entityType;
  private final String name;

  public DefaultPropertyDef(Class<?> clazz, EntityType<?> entityType, String name) {
    this.clazz = Objects.requireNonNull(clazz);
    this.entityType = Objects.requireNonNull(entityType);
    this.name = Objects.requireNonNull(name);
  }

  @Override
  public Class<?> asClass() {
    return clazz;
  }

  @Override
  public EntityPropertyType<?, ?> asType() {
    return entityType.getEntityPropertyType(name);
  }

  @Override
  public String getName() {
    return name;
  }
}
