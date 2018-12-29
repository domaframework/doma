package org.seasar.doma.internal.jdbc.entity;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.lang.reflect.Method;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.entity.EntityType;

/** @author taedium */
public abstract class AbstractEntityListenerContext<E> {

  protected final EntityType<E> entityType;

  protected final Method method;

  protected final Config config;

  protected E newEntity;

  protected AbstractEntityListenerContext(EntityType<E> entityType, Method method, Config config) {
    assertNotNull(entityType, method, config);
    this.entityType = entityType;
    this.method = method;
    this.config = config;
  }

  protected boolean isPropertyDefinedInternal(String propertyName) {
    assertNotNull(propertyName);
    return entityType.getEntityPropertyType(propertyName) != null;
  }

  public EntityType<E> getEntityType() {
    return entityType;
  }

  public Method getMethod() {
    return method;
  }

  public Config getConfig() {
    return config;
  }

  public E getNewEntity() {
    return this.newEntity;
  }

  public void setNewEntity(E newEntity) {
    if (newEntity == null) {
      throw new DomaNullPointerException("newEntity");
    }
    this.newEntity = newEntity;
  }
}
